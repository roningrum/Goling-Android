package dev.antasource.goling.util

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

    fun compressImageToLimit(file: File, maxSizeKB: Int = 1024): File {
        // Mengidentifikasi ekstensi file
        val fileExtension = file.extension.lowercase()

        // Membaca file gambar menjadi Bitmap
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        var quality = 100  // Mulai dengan kualitas maksimal
        val stream = ByteArrayOutputStream()

        // Tentukan format kompresi berdasarkan ekstensi file
        val compressFormat = when (fileExtension) {
            "jpg", "jpeg" -> Bitmap.CompressFormat.JPEG
            "png" -> Bitmap.CompressFormat.PNG
            else -> Bitmap.CompressFormat.JPEG // Default ke JPEG jika format lain tidak ditemukan
        }

        // Proses kompresi hingga ukuran file kurang dari batas maksimal
        do {
            stream.reset()
            bitmap.compress(compressFormat, quality, stream)
            quality -= 5  // Kurangi kualitas 5% setiap iterasi
        } while (stream.size() / 1024 > maxSizeKB && quality > 5)

        // Menyimpan hasil kompresi ke file baru
        val compressedFile = File(file.parent, "compressed_${file.name}")
        val fos = FileOutputStream(compressedFile)
        fos.write(stream.toByteArray())
        fos.flush()
        fos.close()

        return compressedFile
    }


    // Batasi ukuran ke 1 MB
    fun resizeBitmap(file: File, targetWidth: Int, targetHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath, options)

        val originalWidth = options.outWidth
        val originalHeight = options.outHeight

        val scaleFactor = minOf(originalWidth / targetWidth, originalHeight / targetHeight)
        options.inJustDecodeBounds = false
        options.inSampleSize = scaleFactor

        return BitmapFactory.decodeFile(file.absolutePath, options)
    }


}