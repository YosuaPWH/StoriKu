package com.yosuahaloho.storiku.presentation.camera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yosuahaloho.storiku.databinding.FragmentCameraBinding
import com.yosuahaloho.storiku.presentation.add_story.AddStoryFragment
import com.yosuahaloho.storiku.utils.createFile

/**
 * Created by Yosua on 27/04/2023
 */
class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        hideSystemUI()
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCamera()
        binding.btnCaptureImage.setOnClickListener {
            takePhoto()
        }
        binding.btnSwitchCamera.setOnClickListener {
            cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }
    }

    private fun startCamera() {
        val cameraProvide = ProcessCameraProvider.getInstance(requireContext())

        cameraProvide.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProvide.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewCamera.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Gagal memunculkan kamera", Toast.LENGTH_SHORT)
                    .show()
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createFile(requireContext())

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val toAddStory =
                        CameraFragmentDirections.actionCameraFragmentToAddStoryFragment(
                            photoFile.absolutePath,
                            cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA,
                            true
                        )
                    Toast.makeText(requireActivity(), "SELECTED ${photoFile.absolutePath}", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(toAddStory)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        requireContext(),
                        "Gagal mengambil gambar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }


    private fun hideSystemUI() {
        val activityFrag = (requireActivity() as AppCompatActivity)
        activityFrag.supportActionBar?.hide()

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activityFrag.window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            activityFrag.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val activityFrag = (requireActivity() as AppCompatActivity)
        activityFrag.supportActionBar?.show()
        _binding = null
    }
}