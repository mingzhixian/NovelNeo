package mingzhixian.top.novelneo.ui.read

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import mingzhixian.top.novelneo.ui.getAppContext
import org.json.JSONObject
import kotlin.concurrent.thread

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadBody(navHostController: NavHostController, msg: JSONObject) {
  val sp = getAppContext().getSharedPreferences("readBackGround", Context.MODE_PRIVATE)
  val isShowMenu = rememberSaveable { mutableStateOf(false) }
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
  //菜单
  Scaffold(
    topBar = { if (isShowMenu.value) UpMenu(navHostController = navHostController) },
    bottomBar = { if (isShowMenu.value) DownMenu() },
  ) {
    val wait = "加载中"
    var item1 = rememberSaveable { mutableStateOf(wait) }
    var item2 = rememberSaveable { mutableStateOf(wait) }
    var item3 = rememberSaveable { mutableStateOf(wait) }
    thread {
      //item1.value = NETWORK.getBody(msg = msg)
    }
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(background)
    ) {
      Text(
        text = item1.value
      )
    }
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(background)
    ) {
      Text(
        text = item2.value
      )
    }
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(background)
    ) {
      Text(
        text = item3.value
      )
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

//菜单上
@Composable
fun UpMenu(navHostController: NavHostController) {

}

//菜单下
@Composable
fun DownMenu() {

}