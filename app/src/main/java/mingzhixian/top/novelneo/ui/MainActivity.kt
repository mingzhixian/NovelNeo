package mingzhixian.top.novelneo.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import mingzhixian.top.novelneo.utils.dbTool
import mingzhixian.top.novelneo.utils.networkTool

val DB = dbTool()
val NETWORK = networkTool()

class MainActivity : ComponentActivity() {
  companion object {
    @SuppressLint("StaticFieldLeak")
    lateinit var mContext: Context
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mContext = applicationContext
    //使界面延展至所有导航栏和状态栏
    //WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      NovelHost()
    }
  }
}

fun getAppContext(): Context {
  return MainActivity.mContext
}