package com.theminesec.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ObjectDisplay(text: String) {
    val formatter = remember { DateTimeFormatter.ofPattern("HH:mm:ss.SSS") }

    Row {
        Text(
            style = MaterialTheme.typography.labelSmall, color = Color(0xff28fe14),
            text = formatter.format(LocalDateTime.now()),
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            style = MaterialTheme.typography.labelSmall, color = Color(0xff28fe14),
            text = text
        )
    }
}

@Composable
fun LogViewSection(viewsModel: SdkViewModel) {
    val lazyListState = rememberLazyListState()
    val messages by viewsModel.messages.collectAsState()
    // scroll to bottom when new message comes in
    LaunchedEffect(messages) {
        lazyListState.animateScrollToItem((messages.size - 1).coerceAtLeast(0))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 56.dp,
                end = 16.dp,
                bottom = 16.dp
            )
        ) {
            items(messages) { msg -> ObjectDisplay(msg) }
        }
        Button(
            onClick = { viewsModel.clearLog() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .defaultMinSize(minHeight = 40.dp, minWidth = 64.dp)
                .padding(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.background.copy(0.6f),
                contentColor = Color.White.copy(0.8f)
            ),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Clear")
        }
    }
}