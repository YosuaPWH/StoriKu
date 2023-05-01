package com.yosuahaloho.storiku.presentation.widget

import android.content.Intent
import android.widget.RemoteViewsService

/**
 * Created by Yosua on 01/05/2023
 */
class StackStoryService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return StackRemoteViewsServiceFactory(this.applicationContext)
    }

}