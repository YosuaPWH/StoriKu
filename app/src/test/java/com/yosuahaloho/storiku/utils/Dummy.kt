package com.yosuahaloho.storiku.utils

import com.yosuahaloho.storiku.data.local.entity.StoryData

/**
 * Created by Yosua on 06/05/2023
 */
object Dummy {

    fun generateDummyStoryData(): List<StoryData> {
        val listStory = ArrayList<StoryData>()
        for (i in 1..10) {
            val storyData = StoryData(
                "story-$i",
                "yosua $i",
                "description $i",
                "https://story-api.dicoding.dev/iniadalahdummy",
                "2023-10-06T10:36:37.801Z",
                0.01,
                0.01
            )
            listStory.add(storyData)
        }
        return listStory
    }
}