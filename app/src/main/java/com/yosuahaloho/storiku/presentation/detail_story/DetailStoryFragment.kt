package com.yosuahaloho.storiku.presentation.detail_story

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.yosuahaloho.storiku.databinding.FragmentDetailStoryBinding

class DetailStoryFragment : Fragment() {

    private var _binding: FragmentDetailStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailStoryBinding.inflate(inflater, container, false)

        val story = DetailStoryFragmentArgs.fromBundle(requireArguments()).story

        Glide
            .with(this)
            .load(story.photoUrl)
            .into(binding.ivDetailStory)

        binding.tvDetailAuthor.text = story.name
        binding.tvDetailDescription.text = story.description


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}