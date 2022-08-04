package com.xt.composeapp.compose.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * @author lxl
 * compose入门课程，简单布局
 */
class ComposeDataBindingActivity : ComponentActivity() {
    var uiState by mutableStateOf(Student())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val student = Student()
        setContent {
            DataContent(uiState.name)
        }

//        val list = mutableStateListOf<String>().apply {
//            for (i in 0..100) {
//                add("text$i")
//            }
//        }
//        setContent {
//            val state = rememberLazyListState()
//            LazyColumn(state = state) {
//                items(list) {
//                    Text(text = it)
//                }
//            }
//            LoadMoreListHandler(listState = state) {
////                load()
//            }
//        }

        Thread {
            Thread.sleep(20000)
            val student1 = Student()
            student1.name = "bbbb"
            var uiState2 by mutableStateOf(student1)
            uiState = uiState2
//            uiState.name = "bbb"
            Log.i(
                "test",
                "student.name:${student.name}, mutableStateOf.name:${uiState.name}"
            )
        }.start()


    }
}

@Composable
fun DataContent(name: String) {

    Column(Modifier.padding(10.dp)) {
        Text(text = "")
        Text(text = "Hello $name!")
    }
}

data class Student(
    var name: String = "a",
    val id: Int = 0
)