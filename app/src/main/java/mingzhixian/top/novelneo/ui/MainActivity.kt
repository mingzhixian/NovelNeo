package mingzhixian.top.novelneo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import mingzhixian.top.novelneo.utils.dbTool
import mingzhixian.top.novelneo.utils.networkTool

val DB=dbTool()
val NETWORK=networkTool()

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MainBody()
    }
  }
}