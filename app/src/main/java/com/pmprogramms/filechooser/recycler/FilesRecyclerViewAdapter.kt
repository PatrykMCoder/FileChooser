package com.pmprogramms.filechooser.recycler

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pmprogramms.filechooser.R
import com.pmprogramms.filechooser.callbacks.SelectedFilesCallback
import com.pmprogramms.filechooser.enums.FileType
import com.pmprogramms.filechooser.files.File
import com.pmprogramms.filechooser.fragments.MainFragment

class FilesRecyclerViewAdapter : RecyclerView.Adapter<FilesRecyclerViewAdapter.MyViewHolder>() {
    private lateinit var data: List<File>
    private lateinit var callback: SelectedFilesCallback

    inner class MyViewHolder(private val v: View) : ViewHolder(v) {
        fun bind(file: File) {
            val imageView = v.findViewById<ImageView>(R.id.image)
            val selectedIconImageView = v.findViewById<ImageView>(R.id.selected_icon)

            if (file.type == FileType.VIDEOS.type) {
                imageView.setImageBitmap(file.thumbnail)
            } else if (file.type == FileType.IMAGES.type){
                val uri = Uri.parse(file.path)
                imageView.setImageURI(uri)
            }

            if (file.selected) {
               selectedIconImageView.visibility = View.VISIBLE
            } else {
                selectedIconImageView.visibility = View.INVISIBLE
            }

            imageView.setOnClickListener {
                callback.onSelectedFile(data[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        view.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      holder.bind(data[position])
    }

    fun setData(data: List<File>) {
        this.data = data
    }

    fun setCallback(callback: SelectedFilesCallback) {
        this.callback = callback
    }
}