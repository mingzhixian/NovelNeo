package mingzhixian.top.novelneo.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import mingzhixian.top.novelneo.utils.DbTool
import mingzhixian.top.novelneo.utils.NetworkTool

val DB = DbTool(getAppContext())
val NETWORK = NetworkTool()

class MainActivity : ComponentActivity() {
  companion object {
    @SuppressLint("StaticFieldLeak")
    lateinit var mContext: Context
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mContext = applicationContext
    //检测是否为新的一个月
    //val month= Calendar.getInstance().get(Calendar.MONTH)
    //val count=DB.getStatistics()
    //if(count.getJSONArray("heatMap").length()==0||count.getInt("month")!=month) DB.reSetCount()
    DB.reSetCount()
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