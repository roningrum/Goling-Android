package dev.antasource.goling.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat

object Util {
    fun formatCurrency(amount: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(amount)
    }

    fun getResizedBitmap(filePath: String, maxSize: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)
        options.inSampleSize = calculateInSampleSize(options, maxSize, maxSize)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath, options)
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun bitmapToFile(context: Context, bitmap: Bitmap): File {
        val file = File(context.cacheDir, "resized_image.jpg")
        file.createNewFile()

        var byteArrayOutputStream: ByteArrayOutputStream
        var byteArray: ByteArray
        var fileSizeKB: Int

        var currentQuality = 90

        do {
            byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, currentQuality, byteArrayOutputStream)
            byteArray = byteArrayOutputStream.toByteArray()
            fileSizeKB = byteArray.size / (1024)

            println("Ukuran file setelah kompresi: $fileSizeKB KB")

            currentQuality -= 5 // Kurangi kualitas jika masih terlalu besar
        } while (fileSizeKB > 100 && currentQuality > 10) // Misalnya, target maksimal 100 KB

        FileOutputStream(file).use { it.write(byteArray) }
        return file
    }



}