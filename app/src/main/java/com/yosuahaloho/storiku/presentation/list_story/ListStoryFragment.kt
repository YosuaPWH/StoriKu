package com.yosuahaloho.storiku.presentation.list_story

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.yosuahaloho.storiku.R
import com.yosuahaloho.storiku.databinding.FragmentListStoryBinding
import com.yosuahaloho.storiku.domain.model.DetailStory
import com.yosuahaloho.storiku.presentation.auth.AuthViewModel
import com.yosuahaloho.storiku.presentation.started.StartedActivity
import com.yosuahaloho.storiku.utils.DataMapper.storyDataToModel
import com.yosuahaloho.storiku.utils.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListStoryFragment : Fragment() {

    private var _binding: FragmentListStoryBinding? = null
    private val binding get() = _binding!!
    private val listStoryViewModel: ListStoryViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private val listStoryAdapter by lazy { ListStoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListStoryBinding.inflate(inflater, container, false)
        binding.rvListStory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListStory.adapter = listStoryAdapter
        setupView()
        setupButton()
        setupMenu()

        return binding.root
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.logout, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.btn_logout -> {
                        logout()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun logout() {
        lifecycleScope.launch {
            val logout = authViewModel.logout()
            if (logout) {
                Toast.makeText(requireActivity(), "Berhasil Logout", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireActivity(), StartedActivity::class.java))
                activity?.finish()
            } else {
                Toast.makeText(requireActivity(), "Gagal Logout, terdapat kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupView() {
        lifecycleScope.launch {
            listStoryViewModel.getAllStories().collect {
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
    }

    @SuppressLint("InflateParams")
    private fun setupButton() {
        binding.fabAddStory.setOnClickListener {
            Log.d("YAHADISATA", "YAHAHAH")
            val bottomSheet = BottomSheetDialog(requireContext())
            val bottomSheetView = layoutInflater.inflate(R.layout.layout_bottom_camera, null)

            bottomSheet.setContentView(bottomSheetView)
            bottomSheet.show()

            bottomSheetView.findViewById<MaterialButton>(R.id.btn_camera).setOnClickListener {
                Log.d("DIKLIK BERAPA", "INI BERAPA")
                bottomSheet.dismiss()
                requestPermissionCamera()
            }

            bottomSheetView.findViewById<MaterialButton>(R.id.btn_gallery).setOnClickListener {
                bottomSheet.dismiss()
                launchGallery()
            }
        }
    }

    private fun launchGallery() {
        pickPhotoFromGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val pickPhotoFromGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Toast.makeText(requireActivity(), "SELECTED $uri", Toast.LENGTH_SHORT).show()
                val file = uriToFile(uri, requireActivity())
                val toAddStory =
                    ListStoryFragmentDirections.actionListStoryFragmentToAddStoryFragment(
                        file.absolutePath,
                        false,
                        false
                    )
                findNavController().navigate(toAddStory)
            } else {
                Toast.makeText(requireActivity(), "NOTHING LAST FOREVER", Toast.LENGTH_SHORT).show()
            }
        }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
//            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
//            actionBar.hide()
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