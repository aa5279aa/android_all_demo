package com.xt.composeapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xt.composeapp.compose.activity.ComposeDataBindingActivity
import com.xt.composeapp.compose.activity.ComposeListActivity
import com.xt.composeapp.compose.activity.ComposeMVIActivity

class ComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainContent()
        }
    }


    private fun jumpActivity(c: Class<out Activity?>) {
        val intent = Intent(this, c)
        startActivity(intent)
    }

    @Composable
    fun MainContent() {
        Column(Modifier.padding(10.dp)) {
            Button(onClick = {
                jumpActivity(ComposeDataBindingActivity::class.java)
            }) {
                Text(text = "Compose_DataBinding")
            }
            Button(onClick = {
                jumpActivity(ComposeListActivity::class.java);
            }) {
                Text(text = "Compose_List")
            }
            Button(onClick = {
                jumpActivity(ComposeMVIActivity::class.java);
            }) {
                Text(text = "Compose_MVI")
            }

        }
    }

}

