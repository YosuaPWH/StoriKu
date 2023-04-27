package com.yosuahaloho.storiku.presentation.list_story

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yosuahaloho.storiku.R
import com.yosuahaloho.storiku.domain.model.DetailStory
import com.yosuahaloho.storiku.databinding.ItemStoryBinding

/**
 * Created by Yosua on 20/04/2023
 */
class ListStoryAdapter :
    PagingDataAdapter<DetailStory, ListStoryAdapter.ListStoryViewHolder>(COMPARATOR) {

    private lateinit var onStoryClickCallback: OnStoryClickCallback

    inner class ListStoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: DetailStory) {
            binding.apply {
                Glide
                    .with(root)
                    .load(story.photoUrl)
                    .placeholder(R.drawable.image_sample)
                    .into(ivStory)

                val nama = root.resources.getString(R.string.photo_by, story.name)
                tvPhotoBy.text = Html.fromHtml(nama, Html.FROM_HTML_MODE_LEGACY)

                root.setOnClickListener {
                    onStoryClickCallback.onStoryClicked(story)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoryViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListStoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListStoryViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    interface OnStoryClickCallback {
        fun onStoryClicked(story: DetailStory)
    }

    fun setOnStoryClickCallback(onStoryClickCallback: OnStoryClickCallback) {
        this.onStoryClickCallback = onStoryClickCallback
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<DetailStory>() {
            override fun areItemsTheSame(oldItem: DetailStory, newItem: DetailStory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DetailStory, newItem: DetailStory): Boolean {
                return oldItem == newItem
            }
        }
    }
}