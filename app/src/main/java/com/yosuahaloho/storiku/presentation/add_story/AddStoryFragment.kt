package com.yosuahaloho.storiku.presentation.add_story

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.yosuahaloho.storiku.R
import com.yosuahaloho.storiku.databinding.FragmentAddStoryBinding
import java.io.File

class AddStoryFragment : Fragment() {

    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState)
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)


        return binding.root
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            Toast.makeText(requireContext(), myFile?.name ?: "TEDET", Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        const val CAMERA_RESULT = 200
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}