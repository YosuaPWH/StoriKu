package com.yosuahaloho.storiku.presentation.list_story

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yosuahaloho.storiku.R
import com.yosuahaloho.storiku.databinding.FragmentListStoryBinding
import com.yosuahaloho.storiku.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ListStoryFragment : Fragment() {

    private var _binding: FragmentListStoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListStoryViewModel by viewModels()
    private val listStoryAdapter by lazy { ListStoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListStoryBinding.inflate(inflater, container, false)
        binding.rvListStory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListStory.adapter = listStoryAdapter
        viewModel.getAllStories().observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> {
                    listStoryAdapter.submitList(it.data?.listStory ?: emptyList())
                    listStoryAdapter.notifyDataSetChanged()
                }
                is Result.Loading -> {

                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    Log.d("GAGAL", it.error)
                }
            }
        }


        return binding.root

    }
}