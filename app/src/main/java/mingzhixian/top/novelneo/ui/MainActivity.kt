package mingzhixian.top.novelneo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import mingzhixian.top.novelneo.utils.dbTool

val DB=dbTool()

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MainBody()
    }
  }
}