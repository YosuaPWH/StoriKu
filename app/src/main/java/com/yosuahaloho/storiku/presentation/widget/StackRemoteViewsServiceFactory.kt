package com.yosuahaloho.storiku.presentation.widget

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.yosuahaloho.storiku.R
import java.net.URL

/**
 * Created by Yosua on 01/05/2023
 */
class StackRemoteViewsServiceFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val storyItems = arrayListOf<Bitmap>()
    private lateinit var viewModel: WidgetViewModel

    override fun onCreate() {
        viewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application)
                .create(WidgetViewModel::class.java)
    }

    override fun onDataSetChanged() {
        try {
            val data = viewModel.getTenLatestStory().value

            data?.let {
                it.map { ds ->
                    ds.photoUrl
                }.map { url ->
                    BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
                }
            }
//            val data = viewModel.getTenLatestStory().value?.map { it.photoUrl }?.map {
//                BitmapFactory.decodeStream(URL(it).openConnection().getInputStream())
//            }


        }
//        val data = db.storyDataDao().getTenLatestStory().map { it.storyDataToModel() }
//        storyItems.addAll(data)
//        storyItems.add(BitmapFactory.decodeResource(context.resources, R.drawable.starwars))
//        val data =
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = storyItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.layout_item_widget)
        rv.setImageViewBitmap(R.id.imageView, storyItems[position])

        val extras = bundleOf(
            ImageStoryWidget.STORY_IMAGE to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}