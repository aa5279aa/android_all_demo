package com.xt.client.activitys.compose.news

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@Composable
fun ContentView(viewModel: ContentViewModel) {
    //请求数据
    val viewState = viewModel.uiState

    LaunchedEffect(key1 = true) {
        viewModel.newsChannel.send(ContentIntent.GetContent)
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        viewState.dataList!!.forEachIndexed { index, it ->
            NewsItem(viewModel, position = index, news = it)
        }
    }
}

@Composable
fun NewsItem(viewModel: ContentViewModel, position: Int, news: String) {
    Card(modifier = Modifier.padding(10.dp), elevation = 10.dp) {
        val modifier = Modifier
            .clickable(
                onClick = {
                    viewModel.viewModelScope.launch {
                        viewModel.newsChannel.send(ContentIntent.GetItemDetail(position))
                    }
                }
            )
        Row(
            modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Title${news}")
        }
    }
}