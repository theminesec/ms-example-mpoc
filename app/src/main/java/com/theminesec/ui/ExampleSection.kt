package com.theminesec.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theminesec.example.sdk.softpos.ui.component.Title
import com.theminesec.ui.components.AmountInput
import com.theminesec.ui.components.BrandedButton
import com.theminesec.ui.components.CircleLoadingIndicator

@Composable
fun ExampleSection(viewsModel: SdkViewModel,onPaymentRequested: () -> Unit) {
    val sdkInitialized by viewsModel.isSdkInitialized.collectAsState()


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Title(text = "1.init SDK")
        Text(text = "initialization required to be done before invoking all other APIs. init has to be done every application launch")
        Row(modifier = Modifier.fillMaxWidth()) {
            BrandedButton(
                onClick = { viewsModel.initSdk() },
                modifier = Modifier.weight(0.7f),
                label = "Initialize SDK",
                //enabled = !sdkInitialized
            )
            CircleLoadingIndicator(viewsModel.isLoading, modifier = Modifier.weight(0.3f))
        }

        Title(text = "2. registration")
        Text(text = "when sdk is initialized. the sdk should be registered before calling payment function.")
        Row(modifier = Modifier.fillMaxWidth()) {
            BrandedButton(
                onClick = { viewsModel.registerSdk() },
                modifier = Modifier.weight(0.7f),
                label = "Register SDK",
                enabled = sdkInitialized
            )
            CircleLoadingIndicator(viewsModel.isDoingRegistration, modifier = Modifier.weight(0.3f))
        }

        Title(text = "2.EMV configuration")
        Text(text = "EMV Configuration setup")
        BrandedButton(
            onClick = { viewsModel.setupEmv() },
            label = "setup EMV profile"
        )

        Title(text = "3.input amount")
        Text(text = "input amount to start the transaction")
        AmountInput {
            viewsModel.setTranAmount(it)
        }
        //BrandedButton(onClick = {viewsModel.submitTransaction()}, label = "start transaction")
        BrandedButton(onClick = {onPaymentRequested()}, label = "start transaction(New)")

        Title(text = "4.Entry Pin (opt)")
        Text(text = "PinEntry SCA")
        //BrandedButton(onClick = {viewsModel.pinEntry()}, label = "PIN Entry")
        OpenSecurePinPad()

        Title(text = "4.submit transaction")
        Text(text = "submit the transaction to backend")


    }

}
