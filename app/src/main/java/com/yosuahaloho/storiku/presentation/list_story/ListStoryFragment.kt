package com.yosuahaloho.storiku.presentation.list_story

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yosuahaloho.storiku.R
import com.yosuahaloho.storiku.databinding.FragmentListStoryBinding
import com.yosuahaloho.storiku.domain.model.DetailStory
import com.yosuahaloho.storiku.presentation.detail_story.DetailStoryFragment
import com.yosuahaloho.storiku.utils.DataMapper.storyDataToModel
import com.yosuahaloho.storiku.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
                val toDetailStory = ListStoryFragmentDirections.actionListStoryFragmentToDetailStoryFragment(story)
                findNavController().navigate(toDetailStory)
            }
        })
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}