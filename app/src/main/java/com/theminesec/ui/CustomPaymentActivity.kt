package com.theminesec.ui

import android.content.Context
import android.graphics.Color.RED
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.theminesec.MineHades.models.Amount
import com.theminesec.MineHades.models.PaymentMethod
import com.theminesec.MineHades.models.TranType
import com.theminesec.MineHades.models.WalletType
import com.theminesec.MineHades.models.toDisplayString
import com.theminesec.MineHades.activity.MineSecPaymentActivity
import com.theminesec.MineHades.activity.ScreenProvider
import com.theminesec.MineHades.activity.ui.AmountView
import com.theminesec.MineHades.activity.ui.AwaitCardIndicatorView
import com.theminesec.MineHades.activity.ui.ProgressIndicatorView
import com.theminesec.MineHades.activity.ui.ThemeProvider
import com.theminesec.MineHades.activity.ui.UiProvider
import com.theminesec.MineHades.activity.ui.UiState
import com.theminesec.MineHades.activity.ui.UiStateDisplayView
import com.theminesec.MineHades.activity.ui.components.resources.Icon
import com.theminesec.MineHades.activity.ui.getDescription
import com.theminesec.MineHades.activity.ui.getTitle
import com.theminesec.MineHades.activity.ui.toComposeColor
import com.theminesec.MineHades.models.MhdEmvTransactionDto
import com.theminesec.mpocsample.R
import com.theminesec.ui.components.SampleAwaitCardIndicator
import com.theminesec.ui.components.SampleProcessingIndicator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Currency


class CustomPaymentActivity : MineSecPaymentActivity() {



    override fun provideTheme() = CustomThemeProvider
    //    override val experimentalScreenProvider = false
    override val screenProvider = MsaScreenProvider
//    override fun provideUi() : UiProvider{
//        return CustomUiProvider(
//            customSupportedPayments = listOf(PaymentMethod.VISA, PaymentMethod.MASTERCARD),
//            customShowWallet = true
//        )
//    }
}

object MsaScreenProvider : ScreenProvider() {

    private const val customerFontFeature = "ss01,ss04,cv10"

    private val shellPadding
        @Composable
        get() = PaddingValues(SampleTheme.spacing.md)
    @Composable
    override fun PreparationScreen(
        request: MhdEmvTransactionDto,
        preparingFlow: Flow<UiState.Preparing>,
        countdownFlow: StateFlow<Int>
    ) {
        val uiState by preparingFlow.collectAsStateWithLifecycle(UiState.Preparing.Idle)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(shellPadding),
        ) {
            // render animation first to lay it in the back
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(modifier = Modifier.size(280.dp), contentAlignment = Alignment.Center) {
                    SampleProcessingIndicator()
                }
                Spacer(Modifier.height(SampleTheme.spacing.lg))
                CustomStateDisplay(uiState)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
            ) {
                CustomTopBar(countdownFlow)
                CustomAmountDisplay(request)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                CustomCopyright()
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun AwaitingCardScreen(
        request: MhdEmvTransactionDto,
        awaitingFlow: Flow<UiState.Awaiting>,
        supportedMethods: List<PaymentMethod>,
        countdownFlow: StateFlow<Int>,
        onAbort: () -> Unit
    ) {
        val uiState by awaitingFlow.collectAsStateWithLifecycle(UiState.Preparing.Idle)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(shellPadding),
        ) {
            // render animation first to lay it in the back
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SampleAwaitCardIndicator()
                Spacer(Modifier.height(SampleTheme.spacing.lg))
                CustomStateDisplay(uiState)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
            ) {
                CustomTopBar(countdownFlow, onAbort)
                CustomAmountDisplay(request)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(SampleTheme.spacing.xs2),
                    horizontalArrangement = Arrangement.spacedBy(
                        SampleTheme.spacing.xs,
                        Alignment.CenterHorizontally
                    ),
                ) { supportedMethods.filter { it == PaymentMethod.VISA || it == PaymentMethod.MASTERCARD  }.forEach { it.Icon() } }
                Spacer(modifier = Modifier.size(SampleTheme.spacing.xs2))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(SampleTheme.spacing.xs2),
                    horizontalArrangement = Arrangement.spacedBy(
                        SampleTheme.spacing.xs,
                        Alignment.CenterHorizontally
                    ),
                ) { WalletType.entries.forEach { it.Icon() } }
                Spacer(modifier = Modifier.size(SampleTheme.spacing.lg))
                CustomCopyright()
            }
        }
    }


    @Composable
    fun CustomTopBar(
        countdownFlow: StateFlow<Int>,
        onAbort: (() -> Unit)? = null,
    ) {
        val countdownSec by countdownFlow.collectAsStateWithLifecycle()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            onAbort?.let {
                IconButton(
                    modifier = Modifier
                        .size(56.dp)
                        .requiredSize(56.dp)
                        .offset(x = (-16).dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = SampleTheme.mpocColors.mutedForeground.toComposeColor(),
                    ),
                    onClick = it,
                ) {
                    Icon(
                        painter = painterResource(id = com.theminesec.MineHades.R.drawable.ico_close),
                        contentDescription = stringResource(com.theminesec.MineHades.R.string.action_abort)
                    )
                }
            }
            Spacer(Modifier.weight(1f, true))
            Text(
                text = stringResource(com.theminesec.MineHades.R.string.var_countdown_second, countdownSec),
                style = SampleTheme.typography.bodyLarge,
                color = SampleTheme.mpocColors.primary.toComposeColor()
            )
        }
    }

    @Composable
    fun CustomAmountDisplay(request: MhdEmvTransactionDto) {
        val amount = Amount(
            BigDecimal.valueOf(request.txnAmount).movePointLeft(Currency.getInstance(request.txnCurrencyText).defaultFractionDigits),
            Currency.getInstance(request.txnCurrencyText)
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(SampleTheme.spacing.sm))
            Text(
                text= when(request.txnType) {
                    TranType.SALE -> "Sale"
                    TranType.REFUND -> "Refund"
                },
                style = SampleTheme.typography.titleSmall,
                color = SampleTheme.mpocColors.mutedForeground.toComposeColor(),
            )
            Text(
                text = amount.toDisplayString(),
                style = SampleTheme.typography.headlineLarge.copy(fontFeatureSettings = "$customerFontFeature,tnum"),
            )
            request.description?.let {
                Text(it)
            }
        }
    }

    @Composable
    fun CustomStateDisplay(uiState: UiState) {
        Text(
            text = uiState.getTitle(),
            textAlign = TextAlign.Center,
            style = SampleTheme.typography.titleLarge,
        )
        Spacer(Modifier.size(SampleTheme.spacing.xs2))
        Text(
            text = uiState.getDescription(),
            textAlign = TextAlign.Center,
            style = SampleTheme.typography.bodyMedium,
            color = SampleTheme.mpocColors.accentForeground.toComposeColor()
        )
    }

    @Composable
    fun CustomCopyright() {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.var_copyright, LocalDate.now().year),
            style = SampleTheme.typography.bodySmall.copy(fontFeatureSettings = "$baseFontFeature,tnum"),
            color = SampleTheme.mpocColors.mutedForeground.toComposeColor(),
        )
    }

}




object CustomThemeProvider : ThemeProvider() {
    override fun provideColors(darkTheme: Boolean) =
        if (darkTheme) MPoCColorsDark().copy(
            primary = Color(RED).toArgb(),
            primaryForeground = Color(0xFFE4F8EE).toArgb(),
        )
        else MPoCColorsLight().copy(
            primary = Color(RED).toArgb(),
            primaryForeground = Color(0xFFFFFFFF).toArgb(),
        )
}

class CustomUiProvider(
    private val customSupportedPayments: List<PaymentMethod>,
    private val customShowWallet: Boolean
) : UiProvider(
    amountView = CustomAmountView,
    awaitCardIndicatorView = CustomAwaitCardIndicatorView,
    progressIndicatorView = CustomProgressIndicatorView
) {
    object CustomAmountView : AmountView {
        override fun createAmountView(context: Context, amount: Amount, description: String?): View {
            return TextView(context).apply {
                text = "${amount.toDisplayString()} - ${description ?: "No Description"}"
                textSize = 18f
                setTextColor(Color.Black.toArgb())
            }
        }
    }

    object CustomAwaitCardIndicatorView : AwaitCardIndicatorView {
        override fun createAwaitCardIndicatorView(context: Context): View {
            return TextView(context).apply {
                text = "Please insert or tap your card..."
                textSize = 16f
                setTextColor(Color.Blue.toArgb())
            }
        }
    }

    object CustomProgressIndicatorView : ProgressIndicatorView {
        override fun createProgressIndicatorView(context: Context): View {
            return ProgressBar(context).apply {
                isIndeterminate = true
            }
        }
    }

    object CustomUiStateDisplayView : UiStateDisplayView {
        override fun createUiStateDisplayView(context: Context, uiState: UiState): View {
            return TextView(context).apply {
                text = when (uiState) {
                    is UiState.Preparing -> "Loading..."
                    is UiState.Awaiting -> "Transaction Successful"
//                    is UiState.Processing -> "Transaction Failed: "
                    else -> "Awaiting Transaction..."
                }
                textSize = 18f
                setTextColor(
                    when (uiState) {
                        is UiState.Preparing -> Color.Blue.toArgb()
                        is UiState.Awaiting -> Color.Green.toArgb()
//                        is UiState.Processing -> Color.Red.toArgb()
                        else -> Color.Gray.toArgb()
                    }
                )

                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 16, 16, 16)
                }
            }
        }
    }

    @Composable
    override fun AcceptanceMarkDisplay(
        supportedPayments: List<PaymentMethod>,
        showWallet: Boolean
    ) {
        //Also using the default payment methods display
        super.AcceptanceMarkDisplay(customSupportedPayments, customShowWallet)
    }



}