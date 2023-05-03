package com.yosuahaloho.storiku.presentation.detail_story

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialContainerTransform
import com.yosuahaloho.storiku.R
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

        sharedElementEnterTransition = MaterialContainerTransform()

        val story = DetailStoryFragmentArgs.fromBundle(requireArguments()).story

        ViewCompat.setTransitionName(binding.ivDetailStory, story.id)

        Glide
            .with(this)
            .load(story.photoUrl)
            .placeholder(R.drawable.image_sample)
            .into(binding.ivDetailStory)

        binding.tvDetailAuthor.text = story.name
        binding.tvDetailDescription.text = story.description

        val activityFragment = (requireActivity() as AppCompatActivity)
        activityFragment.supportActionBar?.title = story.name
        activityFragment.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        return binding.root
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

}