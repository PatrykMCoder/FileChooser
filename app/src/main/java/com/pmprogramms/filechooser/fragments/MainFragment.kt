package com.pmprogramms.filechooser.fragments

import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pmprogramms.filechooser.R
import com.pmprogramms.filechooser.callbacks.SelectedFilesCallback
import com.pmprogramms.filechooser.enums.FileType
import com.pmprogramms.filechooser.files.File
import com.pmprogramms.filechooser.files.Files
import com.pmprogramms.filechooser.recycler.FilesRecyclerViewAdapter

class MainFragment : Fragment() {
    private val selectedFiles = ArrayList<File>()
    private var fileType = FileType.IMAGES
    private lateinit var spinner: Spinner
    private lateinit var button: Button
    private lateinit var recyclerViewAdapter: FilesRecyclerViewAdapter
    private lateinit var files: List<File>
    private var goesToPreview = false

    private var spinnerOptions = arrayOf(Pair("Images", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), Pair("Videos", MediaStore.Video.Media.EXTERNAL_CONTENT_URI))

    private val callback = object : SelectedFilesCallback {
        override fun onSelectedFile(file: File, position: Int) {

            if (selectedFiles.contains(file)) {
                selectedFiles.remove(file)
            } else {
                selectedFiles.add(file)
            }

            if (selectedFiles.isNotEmpty()) {
                button.visibility = View.VISIBLE

                if (selectedFiles.size > 1) {
                    button.text = button.context.resources.getString(R.string.show_selected_files, selectedFiles.size)
                } else {
                   button.text = button.context.resources.getString(R.string.show_selected_file)
                }

            } else {
                button.visibility = View.INVISIBLE
            }

            files[position].selected = !files[position].selected
            recyclerViewAdapter.notifyItemChanged(position)
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        button = view.findViewById(R.id.button)
        spinner = view.findViewById(R.id.spinner)

        val filesHelper = Files(requireContext())

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerOptions.map { return@map  it.first }.toTypedArray())
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                if (spinnerOptions[position].second == MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
                    fileType = FileType.IMAGES
                } else if (spinnerOptions[position].second == MediaStore.Video.Media.EXTERNAL_CONTENT_URI) {
                    fileType = FileType.VIDEOS
                }

                files = filesHelper.getAllFilesByMediaType(spinnerOptions[position].second, fileType)

                recyclerViewAdapter.setData(files)
                recyclerViewAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        files = filesHelper.getAllFilesByMediaType(spinnerOptions[spinner.selectedItemPosition].second, fileType)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler)
        recyclerViewAdapter = FilesRecyclerViewAdapter()
        recyclerViewAdapter.setData(files)
        recyclerViewAdapter.setCallback(callback)

        recyclerView.apply { adapter = recyclerViewAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 3)
        }


        button.setOnClickListener {
            goesToPreview = true
            val action = MainFragmentDirections.actionFragmentMainToPreviewFragment(selectedFiles.toTypedArray(), fileType.type)
            findNavController().navigate(action)
        }

        return view
    }

    override fun onPause() {
        super.onPause()
        if (goesToPreview) {
            val itemCount = selectedFiles.size
            selectedFiles.removeAll(selectedFiles.toSet())
            recyclerViewAdapter.notifyItemRangeRemoved(0, itemCount)
        }
    }

    override fun onResume() {
        super.onResume()
        goesToPreview = false
    }
}