package com.xt.composeapp.compose.handler

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun LoadMoreListHandler(listState: LazyListState, buffer: Int = 1, onLoaderMore: () -> Unit) {

    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1
            val b = lastVisibleItemIndex > (totalItemsCount - buffer)
            b
        }
    }

    LaunchedEffect(loadMore)
    {
        snapshotFlow { loadMore.value }
            .distinctUntilChanged()
            .collect {
                if (it) {
                    onLoaderMore()
                }
            }
    }
}