package com.theminesec.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.nfc.NfcAdapter
import android.nfc.tech.IsoDep
import android.nfc.tech.NfcA
import android.os.Bundle
import android.os.Vibrator
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.theminesec.MineHades.activity.MineSecPaymentActivity
import com.theminesec.MineHades.activity.pinpad.ButtonsColor
import com.theminesec.MineHades.activity.pinpad.PinEntryDetails
import com.theminesec.MineHades.activity.pinpad.PinPadConfig
import com.theminesec.MineHades.activity.pinpad.PinPadConstants
import com.theminesec.MineHades.activity.pinpad.PinPadStyle
import com.theminesec.MineHades.activity.pinpad.TextStyleConfig
import com.theminesec.MineHades.models.MPoCResult
import com.theminesec.MineHades.models.MhdEmvTransactionDto
import com.theminesec.MineHades.models.PinEntryDto
import com.theminesec.MineHades.models.PinEntryMode
import com.theminesec.MineHades.models.PinEntryRequest
import com.theminesec.MineHades.models.PinEntryResponse
import com.theminesec.example.sdk.softpos.ui.theme.MsExampleSdkSoftPOSTheme
import com.theminesec.mpocsample.R
import com.theminesec.ui.components.BrandedButton
import com.theminesec.ui.components.SplitSection
import com.theminesec.utils.BytesUtils
import com.theminesec.utils.GsonUtils.gson
import com.theminesec.utils.PinPadStylesUtil
import com.theminesec.utils.toX509Certificate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Base64
import javax.crypto.Cipher

@Composable
fun ScreenContent() {
    Column(modifier = Modifier.background(Color.Yellow)) {
        Text(text = "Hello, First Compose")

    }
}

@Preview(
    showBackground = true,
    device = "id:pixel_7_pro",
    showSystemUi = true
)
@Composable
fun DefaultPreview() {
    MsExampleSdkSoftPOSTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Yellow)
        ) {

        }
    }
}


class MainActivity : ComponentActivity() {
    val INTENT_FILTER: Array<IntentFilter> = arrayOf(
        IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED),
        IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
    )

    val isNfcEnable: Boolean = true

    /**
     * Technical process list
     */
    val TECH_LIST: Array<Array<String>> = arrayOf(
        arrayOf(
            NfcA::class.java.name,
            IsoDep::class.java.name
        )
    )
    private val sdkViewModel: SdkViewModel by viewModels()
    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var audioManager: AudioManager
    private lateinit var vibrator: Vibrator
    private lateinit var launcher: ActivityResultLauncher<MhdEmvTransactionDto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isNfcEnable) {
            nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        }
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator


        launcher = registerForActivityResult(
            MineSecPaymentActivity.contract(CustomPaymentActivity::class.java)
        ) {
            when (it) {
                is MPoCResult.Success -> {
                    sdkViewModel.writeMessage("${it.javaClass.simpleName} \n" + gson.toJson(it))
                }
                is MPoCResult.Failure -> {
                    sdkViewModel.writeMessage("transaction fails ${it.code} ${it.message} contextu=${it.contextual}")
                }
            }
        }

        setContent {
            MsExampleSdkSoftPOSTheme {
                val sdkViewModel = viewModel(modelClass = SdkViewModel::class.java)

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Scaffold { paddingValues ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            SplitSection(
                                upperContent = {
                                    ExampleSection(
                                        viewsModel = sdkViewModel,
                                        onPaymentRequested = { payment() }
                                    )
                                },
                                lowerContent = {
                                    LogViewSection(viewsModel = sdkViewModel)
                                })
                        }
                    }
                }
            }
        }

    }

    private fun payment() {
        val validAmount = sdkViewModel.amount.toLongOrNull()
        if (validAmount == null || validAmount <= 0) {
            sdkViewModel.writeMessage("Invalid amount. Transaction cannot proceed.")
            return
        }
        val plainTextKey = "11223344556677881122334455999999"
        val publicKey = sdkViewModel.readPublicKeyCert().toX509Certificate()
        val wrappedKey = sdkViewModel.rsaCipher(publicKey.publicKey, BytesUtils.fromString(plainTextKey))

        val transactionDto = MhdEmvTransactionDto(
            txnAmount = sdkViewModel.amount.toLong(),
            txnCurrencyText = "USD",
            timeout = 80,
            wrappedHMacKey = Base64.getEncoder().encodeToString(wrappedKey)
        )
        // with customized PIN entry UI
        val pinPadConfig = PinPadConfig(
            pinPadStyle = PinPadStyle(
                backgroundColor = Color.LightGray.toArgb(),
                amountStyle = getOswaldBold30(this@MainActivity),
                digitStyle = getOswaldBold30(this@MainActivity),
                descriptionStyle = getOswaldBold30(this@MainActivity),
                buttonsColor = ButtonsColor(
                    abortBtn = Color.DarkGray.toArgb(),
                    confirmBtn = Color.Blue.toArgb(),
                    clearBtn = Color.Red.toArgb(),
                )
            ),
            pinEntryMode = PinEntryMode.FIXED,
            pinEntryDetails = PinEntryDetails(
                pinDescription = "Your PIN Please!",
                amount = "100.75 USD",
                description = "Please pay MineSec monthly invoice",
                supportPINBypass = false
            )
        )
        val transactionDtoWithCustomizedPinPadUI = MhdEmvTransactionDto(
            txnAmount = sdkViewModel.amount.toLong(),
            txnCurrencyText = "USD",
            pinpadConfig = gson.toJson(pinPadConfig)
        )
//        launcher.launch(transactionDtoWithCustomizedPinPadUI)
        launcher.launch(transactionDto)
    }

    override fun onResume() {
        super.onResume()
        if (isNfcEnable) {
            val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            nfcAdapter.enableReaderMode(
                this, { tag -> {} },
                NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B or
                        NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK or NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS, Bundle()
            )
        }
    }


    override fun onPause() {
        super.onPause()
        // 在 onPause 中禁用前台分发
        if(isNfcEnable)
        {
            nfcAdapter.disableForegroundDispatch(this)
        }

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        try {
            // 禁用声音
            audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT

            // 禁用震动
            vibrator.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}

@Composable
fun OpenSecurePinPad() {
    val context = LocalContext.current
    val json = Json { encodeDefaults = true }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val response = result.data?.getStringExtra(PinPadConstants.EXTRA_PIN_CAPTURE_RESPONSE)
            response?.let {
                val pinResponse = json.decodeFromString<PinEntryResponse>(it)
                Toast.makeText(context, pinResponse.pinBlock, Toast.LENGTH_SHORT).show()
            }
        } else if (result.resultCode == Activity.RESULT_CANCELED){
            val errorCode = result.data?.getIntExtra(PinPadConstants.EXTRA_PIN_CAPTURE_RESPONSE_CODE, 0)
            val errorMsg = result.data?.getStringExtra(PinPadConstants.EXTRA_PIN_CAPTURE_RESPONSE_MSG)
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
        }
    }

    val selectedMode = remember { mutableStateOf(PinEntryMode.FIXED) }

    PinEntryModeSelector(selectedMode.value) {
        selectedMode.value = it
    }

    BrandedButton(onClick = {
        val requestSCA = json.encodeToString(PinEntryDto("1122334455667788"))
        val bundle = Bundle()
        bundle.apply {
            putString(PinPadConstants.EXTRA_PIN_SCA_CAPTURE, requestSCA)
        }

        //TODO for internal SDK calling please use the following
        val requestTAP = json.encodeToString(
            PinEntryRequest("encrypted_pan", "wrapped_key")
        )

        bundle.apply {
            //putString(PinPadConfig.EXTRA_PIN_TAP_CAPTURE, requestTAP)
        }

//        val intent = Intent(context, SecurePinPadActivity::class.java).apply {
//            putExtra(PinPadConstants.EXTRA_PIN_PAD_CONFIG, getPinPadConfig(context, selectedMode.value))
//            putExtra(PinPadConstants.BUNDLE_PIN_REQUEST, bundle)
//        }


        val intent2=Intent("com.theminesec.minehades.PIN_ACTIVITY").apply {
            // addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) Note: YOU CAN NOT ADD THIS !!
            putExtra(PinPadConstants.EXTRA_PIN_PAD_CONFIG, getPinPadConfig(context, selectedMode.value))
            putExtra(PinPadConstants.BUNDLE_PIN_REQUEST, bundle)
        }

        launcher.launch(intent2)
    }, label = "Open Secure PinPad")
}

@Composable
fun PinEntryModeSelector(
    selectedMode: PinEntryMode,
    onModeSelected: (PinEntryMode) -> Unit
) {
    Column {
        Text(text = "Select Pin Entry Mode")
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            val modes = PinEntryMode.entries.toTypedArray()

            modes.forEach { mode ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onModeSelected(mode) }
                        .padding(8.dp)
                ) {
                    RadioButton(
                        selected = (mode == selectedMode),
                        onClick = { onModeSelected(mode) }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = mode.name)
                }
            }
        }
    }
}

fun getPinPadConfig(context: Context, mode: PinEntryMode) =
    PinPadConfig(
        pinEntryMode = mode,
        pinEntryDetails = PinEntryDetails(
            pinDescription = "Your PIN Please!",
            amount = "100.75 USD",
            description = "Please pay MineSec monthly invoice",
            supportPINBypass = false
        ),
        pinPadStyle = PinPadStyle(
            backgroundColor = Color.White.toArgb(),
            amountStyle = getOswaldBold30(context),
            digitStyle = getOswaldBold30(context),
            descriptionStyle = getOswaldBold30(context),
            buttonsColor = ButtonsColor(
                abortBtn = Color.DarkGray.toArgb(),
                confirmBtn = Color.Blue.toArgb(),
                clearBtn = Color.Red.toArgb(),
            )
        )
    )

fun getOswaldBold30(context: Context) = TextStyleConfig(
    fontSize = 30f,
    fontFileUri = PinPadStylesUtil.getFontUri(
        context = context,
        fontResId = R.font.oswald_bold,
        fileName = "oswald_bold.ttf"
    )?.toString()
)