package com.shreyasmp.blankproject.view

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.shreyasmp.blankproject.ui.theme.BlankProjectTheme
import java.io.File


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlankProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen(this@MainActivity)
                }
            }
        }
    }
}

@Composable
fun MainScreen(activity: Activity) {
    val dcimFilePath =
        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath + "/Camera")
    Log.d("MainActivity", "Path of DCIM: $dcimFilePath")
    val photoList = dcimFilePath.listFiles()
    val imageList = mutableListOf<String>()
    photoList?.forEach { file ->
        imageList.add(file.absolutePath)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Local Photo Gallery") },
                backgroundColor = MaterialTheme.colors.primary
            )
            requestPermissions(activity.applicationContext, activity)
        },
        content = { paddingValues ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = paddingValues,
                content = {
                    items(imageList.size) { index ->
                        Card(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            elevation = 0.dp
                        ) {
                            Log.d("TAG", "Inside Compose Async Image: $index")
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(imageList[index])
                                    .memoryCachePolicy(CachePolicy.ENABLED)
                                    .diskCachePolicy(CachePolicy.ENABLED)
                                    .size(Size.ORIGINAL)
                                    .build(),
                                contentDescription = "Photo"
                            )
                        }
                    }
                }
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BlankProjectTheme {

    }
}

private fun checkPermission(ctx: Context): Boolean {
    // in this method we are checking if the permissions are granted or not and returning the result.
    val result = ContextCompat.checkSelfPermission(ctx, READ_EXTERNAL_STORAGE)
    return result == PackageManager.PERMISSION_GRANTED
}

private fun requestPermissions(ctx: Context, activity: Activity) {
    if (checkPermission(ctx)) {
        // if the permissions are already granted we are calling
        // a method to get all images from our external storage.
        Toast.makeText(ctx, "Permissions granted..", Toast.LENGTH_SHORT).show()
    } else {
        // if the permissions are not granted we are
        // requesting permissions on below line
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(READ_EXTERNAL_STORAGE), 101
        )
    }
}