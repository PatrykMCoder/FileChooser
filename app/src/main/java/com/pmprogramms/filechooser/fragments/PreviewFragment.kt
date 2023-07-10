package com.pmprogramms.filechooser.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pmprogramms.filechooser.R
import com.pmprogramms.filechooser.callbacks.OnClickImageItemCallback
import com.pmprogramms.filechooser.enums.FileType
import com.pmprogramms.filechooser.files.File
import com.pmprogramms.filechooser.recycler.SelectedFilesRecyclerViewAdapter
import java.lang.IllegalArgumentException

class PreviewFragment : Fragment() {
    private val args: PreviewFragmentArgs by navArgs()
    private lateinit var selectedFiles: ArrayList<File>
    private lateinit var fileType: String

    private lateinit var view: View
    private lateinit var imageView: ImageView
    private lateinit var videoView: VideoView
    private lateinit var selectedFilesRecyclerAdapter: SelectedFilesRecyclerViewAdapter

    private val callbackClickImageItem = object : OnClickImageItemCallback {
        override fun onChangeImage(position: Int) {
            selectedFiles.map { element -> element.show = false}
            selectedFiles[position].show = true
            val uri = Uri.parse(selectedFiles[position].path)

            when (fileType) {
                FileType.IMAGES.type -> {
                    setupImages(uri)
                }
                FileType.VIDEOS.type -> {
                    setupVideos(uri)
                }
                else -> {
                    throw IllegalArgumentException("Wrong file type")
                }
            }

            selectedFilesRecyclerAdapter.notifyDataSetChanged()
        }

        override fun onDeleteImage(position: Int) {
            val show = selectedFiles[position].show

            selectedFiles.removeAt(position)
            selectedFilesRecyclerAdapter.notifyItemRemoved(position)
            selectedFilesRecyclerAdapter.notifyDataSetChanged()

            if (selectedFiles.isNotEmpty() && show) {
                selectedFiles.map { element -> element.show = false}
                selectedFiles[0].show = true

                val uri = Uri.parse(selectedFiles[0].path)

                if (fileType == FileType.IMAGES.name) {
                   setupImages(uri)
                } else if (fileType == FileType.VIDEOS.name) {
                    setupVideos(uri)
                } else {
                    throw IllegalArgumentException("Wrong file type")
                }
            }

            if (selectedFiles.isEmpty()) {
                activity?.onBackPressedDispatcher?.onBackPressed() ?: imageView.setImageResource(R.drawable.baseline_hide_image_24)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_preview, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler)

        imageView = view.findViewById(R.id.preview_image)
        videoView = view.findViewById(R.id.preview_video)

        fileType = args.fileType

        selectedFiles = ArrayList()
        selectedFiles.addAll(args.selectedFiles.toCollection(ArrayList()))

        selectedFilesRecyclerAdapter = SelectedFilesRecyclerViewAdapter()
        selectedFilesRecyclerAdapter.setData(selectedFiles)
        selectedFilesRecyclerAdapter.setOnClickImageItemCallback(callbackClickImageItem)

        val mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        val uri = Uri.parse(selectedFiles[0].path)
        if (fileType == FileType.IMAGES.type) {
            imageView.visibility = View.VISIBLE
            setupImages(uri)
        } else if (fileType == FileType.VIDEOS.type) {
            videoView.visibility = View.VISIBLE
            setupVideos(uri)
        } else {
            throw IllegalArgumentException("Wrong file type")
        }

        selectedFiles[0].show = true
        selectedFilesRecyclerAdapter.notifyItemChanged(0)

        val recyclerLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.apply {
            adapter = selectedFilesRecyclerAdapter
            layoutManager = recyclerLayoutManager
        }

        return view
    }

    private fun setupImages(uri: Uri) {
        imageView.visibility = View.VISIBLE
        imageView.setImageURI(uri)
    }

    private fun setupVideos(uri: Uri) {
        videoView.visibility = View.VISIBLE
        videoView.setVideoURI(uri)
    }
}