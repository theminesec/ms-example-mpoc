package com.theminesec.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow


@Composable
fun CircleLoadingIndicator(
    isLoading: StateFlow<Boolean>,
    modifier: Modifier = Modifier
) {
//    if (isLoading.collectAsState().value) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp,0.dp)
    ) {
        if (isLoading.collectAsState().value)
            CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
    }

    //}
}