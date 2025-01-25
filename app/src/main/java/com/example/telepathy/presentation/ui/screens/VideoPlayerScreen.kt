import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.telepathy.R

@Composable
fun VideoPlayerScreen(navController: NavHostController) {
    val context = LocalContext.current
    val videoUri = Uri.parse("android.resource://${context.packageName}/${R.raw.film}")


    Column(
        modifier = Modifier.testTag("VideoScreen")
    ) {
        Text(text = "Video")
    }

    AndroidView(
        factory = { ctx ->
            VideoView(ctx).apply {
                setVideoURI(videoUri)
                setMediaController(MediaController(ctx).apply {
                    setAnchorView(this@apply)
                })
                setOnCompletionListener {
                    navController.popBackStack()
                }
                start()
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
