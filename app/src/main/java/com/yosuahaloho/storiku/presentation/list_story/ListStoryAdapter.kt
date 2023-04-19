package com.yosuahaloho.storiku.presentation.list_story

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yosuahaloho.storiku.data.remote.response.DetailStory
import com.yosuahaloho.storiku.databinding.ItemStoryBinding
import com.yosuahaloho.storiku.domain.model.User

/**
 * Created by Yosua on 20/04/2023
 */
class ListStoryAdapter : RecyclerView.Adapter<ListStoryAdapter.ListStoryViewHolder>() {

    private var items = arrayListOf<DetailStory>()

    inner class ListStoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: DetailStory) {
            binding.apply {
                Glide
                    .with(root)
                    .load(story.photoUrl)
                    .into(ivStory)

                tvAuthor.text = story.name
                tvShortDesc.text = story.description
            }
        }
    }

    fun submitList(list: List<DetailStory>) {
        items = list as ArrayList<DetailStory>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoryViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListStoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListStoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}