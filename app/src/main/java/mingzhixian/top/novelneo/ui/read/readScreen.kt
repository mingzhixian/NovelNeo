package mingzhixian.top.novelneo.ui.read

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import mingzhixian.top.novelneo.ui.DB
import mingzhixian.top.novelneo.ui.NETWORK
import mingzhixian.top.novelneo.ui.getAppContext
import mingzhixian.top.novelneo.ui.theme.NovelNeoTheme
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadBody(navHostController: NavHostController, msg: JSONObject) {
  NovelNeoTheme {
    val sp = getAppContext().getSharedPreferences("readBackGround", Context.MODE_PRIVATE)
    //状态栏、导航栏沉浸
    val colorList = listOf(MaterialTheme.colorScheme.secondaryContainer, Color.Black, Color.White, Color.DarkGray)
    val background = colorList[sp.getInt("readBackGround", 0)]
    Immerse(background)
    //返回恢复颜色
    val isBack = remember { mutableStateOf(false) }
    if (isBack.value) Immerse(MaterialTheme.colorScheme.background)
    BackHandler(enabled = true) {
      isBack.value = true
      navHostController.navigateUp()
    }
    //时间
    var startTime = System.currentTimeMillis()
    var endTime: Long
    var word = 0
    var hour = 0.0
    //页面
    Scaffold {
      val wait = "加载中"
      val title = rememberSaveable { mutableStateOf(wait) }
      val body = rememberSaveable { mutableStateOf(wait) }
      val listState = rememberLazyListState()
      val coroutineScope = rememberCoroutineScope()
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .background(background)
          .padding(16.dp, 10.dp),
        state = listState
      ) {
        item {
          Text(
            text = title.value,
            fontSize = 40.sp,
            letterSpacing = 10.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(40.dp, 30.dp, 0.dp, 50.dp)
          )
        }
        item {
          Text(
            text = body.value,
            fontSize = 22.sp,
            lineHeight = 36.sp,
            letterSpacing = 4.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
        item {
          Row(modifier = Modifier.padding(50.dp, 40.dp)) {
            Text(
              text = "上一章",
              fontSize = 18.sp,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onTertiary,
              modifier = Modifier
                .clip(shape = RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.tertiary)
                .padding(25.dp, 16.dp)
                .clickable {
                  msg.put("current", msg.getInt("current") - 1)
                  thread {
                    //当前章字数
                    word = body.value.length
                    //获取新的一章
                    title.value =
                      JSONArray(msg.getString("menu"))
                        .getJSONObject(msg.getInt("current"))
                        .getString("title")
                    body.value = NETWORK.getBody(msg = msg)
                    //获取阅读时间
                    endTime = System.currentTimeMillis()
                    hour = (endTime-startTime).toDouble() / (1000 * 60 * 60)
                    startTime = endTime
                    //更新数据库
                    DB.updateCount(word, hour)
                    DB.updateCurrent(msg = msg)
                  }
                  coroutineScope.launch {
                    listState.animateScrollToItem(0, 0)
                  }
                },
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
              text = "下一章",
              fontSize = 18.sp,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onTertiary,
              modifier = Modifier
                .clip(shape = RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.tertiary)
                .padding(25.dp, 16.dp)
                .clickable {
                  msg.put("current", msg.getInt("current") + 1)
                  thread {
                    //当前章字数
                    word = body.value.length
                    //获取新的一章
                    title.value =
                      JSONArray(msg.getString("menu"))
                        .getJSONObject(msg.getInt("current"))
                        .getString("title")
                    body.value = NETWORK.getBody(msg = msg)
                    //获取阅读时间
                    endTime = System.currentTimeMillis()
                    hour = (endTime-startTime).toDouble() / (1000 * 60 * 60)
                    startTime = endTime
                    //更新数据库
                    Log.e(ContentValues.TAG, "ReadBody: $hour", )
                    DB.updateCount(word, hour)
                    DB.updateCurrent(msg = msg)
                  }
                  coroutineScope.launch {
                    listState.animateScrollToItem(0, 0)
                  }
                }
            )
          }
        }
      }
      thread {
        title.value =
          JSONArray(msg.getString("menu"))
            .getJSONObject(msg.getInt("current"))
            .getString("title")
        body.value = NETWORK.getBody(msg = msg)
      }
    }
  }
}

//导航栏沉浸
@Composable
fun Immerse(color: Color) {
  rememberSystemUiController().run {
    setStatusBarColor(color, !isSystemInDarkTheme())
    setSystemBarsColor(color, !isSystemInDarkTheme())
    setNavigationBarColor(color, !isSystemInDarkTheme())
  }
}