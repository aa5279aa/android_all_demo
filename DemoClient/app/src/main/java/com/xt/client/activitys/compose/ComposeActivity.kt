package com.xt.client.activitys.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xt.client.activitys.compose.theme.DemoClientTheme

/**
 * @author lxl
 * compose入门课程，简单布局
 */
class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DemoClientTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("A")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Column(
        modifier = Modifier
            .background(Color.Blue)
            .padding(10.dp, 50.dp).absoluteOffset(10.dp,0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hello $name!")
        Text(text = "Hello1 !")
        Text(text = "Hello2 !")
        Text(text = "Hello3 !")
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text("横向布局1")
//            Text(text = "横向布局2")
//        }
        Row(
            modifier = Modifier.background(Color.Yellow),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text("横向1")
            Text("横向2")

        }

        var list = ArrayList<String>()
        list.add("Android")
        list.add("Compose")
        list.add("XMLLayout")


        LazyColumn {
            items(list){
                name-> messageCard(name)
            }

        }
    }
}

@Composable
fun messageCard(name: String) {
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

            Text(
                text = "这里是文本内容",
                fontSize = 11.sp
            )
        }
    }
}

//@Composable
//fun messageCard(name:String):Unit{
//
//}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DemoClientTheme {
        Greeting("IOS and Android")
    }
}