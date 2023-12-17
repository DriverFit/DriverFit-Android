package id.ac.unri.driverfit.ui.camera

import android.graphics.Bitmap

fun Bitmap.centerCrop(desiredWidth: Int, desiredHeigh: Int): Bitmap {
    val xStart = (width - desiredWidth) / 2
    val yStart = (height - desiredHeigh) / 2

    if (xStart < 0 || yStart < 0 || desiredWidth > width || desiredHeigh > height) {
        throw IllegalArgumentException("invalid")
    }

    return Bitmap.createBitmap(this, xStart, yStart, desiredWidth, desiredHeigh)
}