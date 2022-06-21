package com.xt.client.activitys.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.xt.client.activitys.compose.news.ContentView
import com.xt.client.activitys.compose.news.ContentViewModel

/**
 * @author lxl
 * mvi的方式写Compose
 */
class MVIComposeActivity : ComponentActivity() {
    private val viewModel by viewModels<ContentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContentView(viewModel)
        }
    }
}
