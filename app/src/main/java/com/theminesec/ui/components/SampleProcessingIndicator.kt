package com.theminesec.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun SampleProcessingIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.size(160.dp),
        strokeCap = StrokeCap.Round,
        strokeWidth = 8.dp,
    )
}