package com.pmprogramms.filechooser.recycler

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pmprogramms.filechooser.R
import com.pmprogramms.filechooser.callbacks.OnClickImageItemCallback
import com.pmprogramms.filechooser.files.File
import com.pmprogramms.filechooser.fragments.PreviewFragment

class SelectedFilesRecyclerViewAdapter : RecyclerView.Adapter<SelectedFilesRecyclerViewAdapter.MyViewHolder>(){
    private lateinit var data: List<File>
    private lateinit var onClickImageItemCallback: OnClickImageItemCallback
    private var oldImageSelectedPosition = 0

    inner class MyViewHolder(v: View) : ViewHolder(v) {
        private val imageView = v.findViewById<ImageView>(R.id.image)
        private val showIconImageView = v.findViewById<ImageView>(R.id.show_icon)
        fun bind(file: File) {

            if (file.show) {
                showIconImageView.visibility = View.VISIBLE
            } else {
                showIconImageView.visibility = View.INVISIBLE
            }

            val uri = Uri.parse(file.path)
            imageView.setImageURI(uri)

            imageView.setOnClickListener {
                onClickImageItemCallback.onChangeImage(adapterPosition)
                oldImageSelectedPosition = adapterPosition
            }

            imageView.setOnLongClickListener {
                onClickImageItemCallback.onDeleteImage(adapterPosition)
                return@setOnLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.selected_image_item, parent, false)
        view.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: List<File>) {
        this.data = data
    }

    fun setOnClickImageItemCallback(onClickImageItemCallback: OnClickImageItemCallback) {
        this.onClickImageItemCallback = onClickImageItemCallback
    }
}