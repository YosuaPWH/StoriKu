package com.yosuahaloho.storiku.presentation.add_story

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yosuahaloho.storiku.R
import com.yosuahaloho.storiku.databinding.FragmentAddStoryBinding
import com.yosuahaloho.storiku.utils.Result
import com.yosuahaloho.storiku.utils.reduceFileImage
import com.yosuahaloho.storiku.utils.rotateFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class AddStoryFragment : Fragment() {

    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!
    private var getFile: File? = null
    private val viewModel: AddStoryViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState)
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        val activityFragment = (requireActivity() as AppCompatActivity)
        activityFragment.supportActionBar?.title = "Add Story"
        activityFragment.supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val args = AddStoryFragmentArgs.fromBundle(requireArguments())

        getPicture(args)
        setupButton()

        return binding.root
    }

    private fun setupButton() {
        binding.btnSend.setOnClickListener {
            uploadStory()
        }

    }

    private fun getPicture(args: AddStoryFragmentArgs) {
        val picturePath = args.picture
        val isBackCamera = args.isBackCamera
        val isFromCamera = args.isFromCamera

        val imageFile = File(picturePath)

        imageFile.also {
            if (isFromCamera) {
                rotateFile(it, isBackCamera)
            }
            getFile = it
            binding.previewImage.setImageBitmap(BitmapFactory.decodeFile(it.path))
        }
    }

    private fun uploadStory() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val description =
                binding.inputDescription.text.toString().toRequestBody("text/plain".toMediaType())

            val requestImageFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val imageMultiPart =
                MultipartBody.Part.createFormData("photo", file.name, requestImageFile)

            lifecycleScope.launch {
                viewModel.uploadStory(imageMultiPart, description).collect {
                    when (it) {
                        is Result.Success -> {
                            binding.progressLayout.visibility = View.INVISIBLE
                            Toast.makeText(
                                requireActivity(),
                                resources.getString(R.string.success_add_new_story),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            val toListFragment =
                                AddStoryFragmentDirections.actionAddStoryFragmentToListStoryFragment()
                            toListFragment.isFromAddStory = true
                            findNavController().navigate(toListFragment)
                        }

                        is Result.Loading -> {
                            binding.progressLayout.visibility = View.VISIBLE
                        }

                        is Result.Error -> {
                            binding.progressLayout.visibility = View.INVISIBLE
                            val errorMessage =
                                if (it.error == "\"description\" is not allowed to be empty") {
                                    resources.getString(R.string.error_fill_description)
                                } else {
                                    it.error
                                }
                            Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                (requireActivity() as AppCompatActivity).onBackPressedDispatcher.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}