package com.theminesec.ui

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.nfc.NfcAdapter
import android.nfc.tech.IsoDep
import android.nfc.tech.NfcA
import android.os.Bundle
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.theminesec.example.sdk.softpos.ui.theme.MsExampleSdkSoftPOSTheme
import com.theminesec.ui.components.SplitSection

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

    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var audioManager: AudioManager
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isNfcEnable) {
            nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        }
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

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
                                upperContent = { ExampleSection(viewsModel = sdkViewModel) },
                                lowerContent = {
                                    LogViewSection(viewsModel = sdkViewModel)
                                })
                        }
                    }
                }
            }
        }

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

    override fun onNewIntent(intent: Intent?) {
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
