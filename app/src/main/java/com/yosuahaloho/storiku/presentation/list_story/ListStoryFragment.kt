package com.yosuahaloho.storiku.presentation.list_story

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.yosuahaloho.storiku.R
import com.yosuahaloho.storiku.databinding.FragmentListStoryBinding
import com.yosuahaloho.storiku.domain.model.DetailStory
import com.yosuahaloho.storiku.utils.DataMapper.storyDataToModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListStoryFragment : Fragment() {

    private var _binding: FragmentListStoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListStoryViewModel by viewModels()
    private val listStoryAdapter by lazy { ListStoryAdapter() }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListStoryBinding.inflate(inflater, container, false)
        binding.rvListStory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListStory.adapter = listStoryAdapter
        lifecycleScope.launch {
            viewModel.getAllStories().collect {
                val data = it.map { sd ->
                    sd.storyDataToModel()
                }
                listStoryAdapter.submitData(data)
            }
        }

        listStoryAdapter.setOnStoryClickCallback(object : ListStoryAdapter.OnStoryClickCallback {
            override fun onStoryClicked(story: DetailStory) {
                val toDetailStory =
                    ListStoryFragmentDirections.actionListStoryFragmentToDetailStoryFragment(story)
                findNavController().navigate(toDetailStory)
            }
        })

        binding.fabAddStory.setOnClickListener {
            val bottomSheet = BottomSheetDialog(requireContext())
            val bottomSheetView = layoutInflater.inflate(R.layout.layout_bottom_camera, null)

            bottomSheetView.findViewById<MaterialButton>(R.id.btn_camera).setOnClickListener {
                bottomSheet.dismiss()

                requestPermissionCamera()
            }

            bottomSheetView.findViewById<MaterialButton>(R.id.btn_gallery).setOnClickListener {
                bottomSheet.dismiss()
            }

            bottomSheet.setContentView(bottomSheetView)
            bottomSheet.show()
        }

        return binding.root

    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCameraX()
        } else {
            Toast.makeText(requireContext(), "Tidak diijinkan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestPermissionCamera() {
        requestPermission.launch(Manifest.permission.CAMERA)
    }

    private fun startCameraX() {
        val toCamera = ListStoryFragmentDirections.actionListStoryFragmentToCameraFragment()
        findNavController().navigate(toCamera)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}