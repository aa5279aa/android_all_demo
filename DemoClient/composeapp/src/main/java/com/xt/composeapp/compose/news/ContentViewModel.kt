package com.xt.client.activitys.compose.news

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

//UI状态
data class NaviViewState(
    var detailContent: String? = null,
    val dataList: List<String> = emptyList()
)

//定义意图
sealed class ContentIntent {
    //传递消息
    object GetContent : ContentIntent()
    class GetItemDetail(val select: Int) : ContentIntent()
}

class ContentViewModel : ViewModel() {
    //Channel信道，意图发送别ViewModel，
    val newsChannel = Channel<ContentIntent>(Channel.UNLIMITED)

    //状态管理
    var uiState by mutableStateOf(NaviViewState())

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            newsChannel.consumeAsFlow().collect {
                when (it) {
                    is ContentIntent.GetContent -> getContent()
                    is ContentIntent.GetItemDetail -> getNewsDetail(it);
                }
            }
        }
    }

    private val newsFlow: Flow<List<String>> = flow {
        val list = mutableListOf<String>()
        list.add("111")
        list.add("222")
        emit(list)
    }

    private val detailFlow: Flow<List<String>> = flow {
        val list = mutableListOf<String>()
        list.add("111-详情")
        list.add("222-详情")
        emit(list)
    }

    private fun getContent() {
        viewModelScope.launch {
            newsFlow.flowOn(Dispatchers.Default).collect { contents ->
                uiState = uiState.copy(dataList = contents)
            }
        }
    }

    private fun getNewsDetail(select: ContentIntent.GetItemDetail) {
        viewModelScope.launch {
            detailFlow.flowOn(Dispatchers.Default).collect { contents ->
                uiState = uiState.copy(detailContent = contents[select.select])
            }
        }
    }
}