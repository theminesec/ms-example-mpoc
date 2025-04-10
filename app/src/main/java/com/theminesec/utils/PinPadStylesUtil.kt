package com.theminesec.utils

import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.theminesec.MineHades.activity.pinpad.TextStyleConfig
import java.io.File
import java.io.FileOutputStream

object PinPadStylesUtil {


    fun TextStyleConfig.getTextStyle(): TextStyle? {
        var textStyle: TextStyle? = null
        fontSize?.let {
            textStyle = TextStyle(fontSize = it.sp)
        }
        fontFileUri?.let { path ->
            val uri = Uri.parse(path)
            val fontFamily = loadFontFamilyFromUri(uri)
            textStyle =
                textStyle?.copy(fontFamily = fontFamily) ?: TextStyle(fontFamily = fontFamily)
        }
        return textStyle
    }

    fun TextStyleConfig.getTypeface(): Typeface? {
        return fontFileUri?.let { path ->
            val uri = Uri.parse(path)
            loadTypeFaceFromUri(uri)
        }
    }

    fun getFontUri(context: Context, fontResId: Int, fileName: String): Uri? {
        val file = File(context.cacheDir, fileName)

        return try {
            context.resources.openRawResource(fontResId).use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun loadFontFamilyFromUri(uri: Uri): FontFamily {
        return try {
            val file = File(uri.path!!)
            val typeface = Typeface.createFromFile(file)
            FontFamily(typeface)
        } catch (e: Exception) {
            e.printStackTrace()
            FontFamily.Default
        }
    }

    private fun loadTypeFaceFromUri(uri: Uri): Typeface {
        return try {
            val file = File(uri.path!!)
            Typeface.createFromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            Typeface.DEFAULT
        }
    }

}
