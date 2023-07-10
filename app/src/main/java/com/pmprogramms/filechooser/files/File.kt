package com.pmprogramms.filechooser.files

import android.graphics.Bitmap
import android.media.Image
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class File(
    val path: String,
    var selected: Boolean,
    val type: String,
    var show: Boolean,
    var thumbnail: Bitmap?
): Parcelable
