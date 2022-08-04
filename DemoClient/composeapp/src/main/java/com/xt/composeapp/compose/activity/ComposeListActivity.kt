package com.xt.composeapp.compose.activity


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xt.client.activitys.compose.theme.DemoClientTheme
import com.xt.composeapp.compose.handler.LoadMoreListHandler

/**
 * @author lxl
 * compose带下拉加载更多功能的lazyColumn
 */
class ComposeListActivity : ComponentActivity() {
    private val list = mutableStateListOf<String>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadMore()
        setContent {
            DemoClientTheme {
                // A surface container using the 'background' color from the theme
                ListGreeting()
            }
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun ListGreeting() {
        Log.i("lxltest", "ListGreeting")
        val state = rememberLazyListState()
        Row(
            Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            LazyColumn(Modifier.padding(5.dp), state = state) {
                items(list) { name ->
                    ListMessageCard(name)
                }
            }
            LoadMoreListHandler(listState = state) {
                Toast.makeText(this@ComposeListActivity, "加载中，请稍后", Toast.LENGTH_SHORT).show()
                loadMore()
            }
        }
    }

    @Composable
    fun ListMessageCard(name: String) {
        Row(modifier = Modifier.padding(all = 12.dp)) {
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = name,
                    color = MaterialTheme.colors.secondaryVariant,
                    style = MaterialTheme.typography.subtitle2,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }

    private fun loadMore() {
        val start = list.size
        Log.i("lxltest", "加载start:${start}到${start + 30}")
        list.apply {
            for (i in 0 until 30) {
                add("text${start + i}")
            }
        }
    }
}

