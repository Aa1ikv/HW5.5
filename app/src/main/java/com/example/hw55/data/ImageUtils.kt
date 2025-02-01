package com.example.hw55.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.CoroutineStart
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.Base64

class ImageUtils {
    object ImageUtils {
        fun bitmapToBase64(bitmap: Bitmap): String {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(),
                CoroutineStart.DEFAULT
            )
        }

        fun base64ToBitmap(base64String: String): Bitmap {
            val decodedBytes = Base64.decode(base64String, CoroutineStart.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }
    }
}