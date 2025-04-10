package com.theminesec.ui.components

import android.content.ContentResolver
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.view.Surface
import android.view.TextureView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.theminesec.MineHades.R

@Composable
fun SampleAwaitCardIndicator() {
    val isNight = isSystemInDarkTheme()
    val mediaPlayer = remember { MediaPlayer() }
    AndroidView(
        { ctx ->
            TextureView(ctx).apply {
                surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                        val videoUri = Uri.Builder()
                            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                            .authority(context.packageName)
                            .appendPath(
                                if (isNight) "${R.raw.anim_await_card_night}"
                                else "${R.raw.anim_await_card_day}"
                            ).build()

                        mediaPlayer.apply {
                            setDataSource(ctx, videoUri)
                            setSurface(Surface(surface))
                            isLooping = true
                            prepareAsync()
                            setOnPreparedListener { start() }
                        }
                    }

                    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}
                    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean = true
                    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
                }
            }
        },
        modifier = Modifier
            .height(280.dp)
            .width(280.dp)
    )
    DisposableEffect(Unit) {
        onDispose { mediaPlayer.release() }
    }
}
