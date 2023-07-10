package com.pmprogramms.filechooser.callbacks

import com.pmprogramms.filechooser.files.File

interface SelectedFilesCallback {
    fun onSelectedFile(file: File, position: Int)
}