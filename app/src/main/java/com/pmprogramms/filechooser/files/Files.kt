package com.pmprogramms.filechooser.files

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import com.pmprogramms.filechooser.enums.FileType
import java.lang.Exception


class Files(private val context: Context) {
    fun getAllFilesByMediaType(mediaType: Uri, fileType: FileType) : List<File> {
        val files = ArrayList<File>()
        val query = context.contentResolver.query(mediaType, null, null, null, null)
        query?.use { cursor ->
            while (cursor.moveToNext()) {
                try {
                    val columnName = when (mediaType) {
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI -> {
                            MediaStore.Images.Media._ID
                        }
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI -> {
                            MediaStore.Video.Media._ID
                        }
                        else -> {
                            ""
                        }
                    }
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(columnName))
                    val contentUri = ContentUris.withAppendedId(mediaType, id)

                    val file = when (fileType) {
                        FileType.VIDEOS -> {
                            val thumbnail = context.contentResolver.loadThumbnail(contentUri, Size(150, 150), null)
                            File(contentUri.toString(),
                                selected = false,
                                show = false,
                                type = FileType.VIDEOS.type,
                                thumbnail = thumbnail
                            )
                        }
                        FileType.IMAGES -> {
                            File(contentUri.toString(),
                                selected = false,
                                show = false,
                                type = FileType.IMAGES.type,
                                thumbnail = null
                            )
                        }
                    }

                    files.add(file)
                } catch (_: Exception) {
                }
            }
        }

        return files
    }

}