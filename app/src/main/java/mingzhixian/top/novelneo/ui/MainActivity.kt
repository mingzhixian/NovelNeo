package mingzhixian.top.novelneo.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kotlinx.coroutines.runBlocking
import mingzhixian.top.novelneo.utils.Data
import mingzhixian.top.novelneo.utils.DbTool
import mingzhixian.top.novelneo.utils.NetworkTool
import org.json.JSONObject
import java.util.*

val DB = DbTool(getAppContext())
val NETWORK = NetworkTool()
val DATA = Data()

class MainActivity : ComponentActivity() {
  companion object {
    @SuppressLint("StaticFieldLeak")
    lateinit var mContext: Context
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mContext = applicationContext
    //检测是否为新的一个月
    val month = Calendar.getInstance().get(Calendar.MONTH) + 1
    val count = JSONObject(DB.getStatistics())
    if (count.getJSONArray("heatMap").length() == 0 || count.getInt("month") != month) DB.reSetCount()
    //使界面延展至所有导航栏和状态栏
    //WindowCompat.setDecorFitsSystemWindows(window, false)
    runBlocking {
      setContent {
        NovelHost()
      }
    }
  }
}

fun getAppContext(): Context {
  return MainActivity.mContext
}