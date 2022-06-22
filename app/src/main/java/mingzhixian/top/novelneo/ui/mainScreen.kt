package mingzhixian.top.novelneo.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import mingzhixian.top.novelneo.R
import mingzhixian.top.novelneo.ui.theme.NovelNeoTheme
import org.json.JSONObject
import kotlin.concurrent.thread

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainBody(navController: NavHostController) {
  NovelNeoTheme {
    //状态栏、导航栏
    val statusbarColor = MaterialTheme.colorScheme.background
    rememberSystemUiController().run {
      setStatusBarColor(statusbarColor, !isSystemInDarkTheme())
      setSystemBarsColor(statusbarColor, !isSystemInDarkTheme())
      setNavigationBarColor(statusbarColor, !isSystemInDarkTheme())
    }
    //最近更新
    val items = rememberSaveable { mutableListOf(listOf(JSONObject().toString())) }
    //获取数据库数据
    val info = rememberSaveable { mutableListOf(JSONObject().toString()) }
    //是否显示加载动画
    val isShowLoading = rememberSwipeRefreshState(false)
    SwipeRefresh(state = isShowLoading, onRefresh = {
      thread {
        isShowLoading.isRefreshing = true
        NETWORK.getBooksUpdate()
        items[0] = DB.getUpdateBooks()
        info[0] = DB.getStatistics()
        isShowLoading.isRefreshing = false
      }
    }) {
      Scaffold { innerPadding ->
        LazyColumn(
          modifier = Modifier
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.background)
        ) {
          item {
            Spacer(modifier = Modifier.height(60.dp))
          }
          stickyHeader {
            Box(
              modifier = Modifier
                .height(60.dp)
                .background(MaterialTheme.colorScheme.surface),
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(10.dp, 0.dp, 10.dp, 0.dp)
                  .height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Text(
                  text = "NovelNeo",
                  fontSize = 30.sp,
                  fontWeight = FontWeight.SemiBold,
                  modifier = Modifier
                    .padding(8.dp, 0.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                  painter = painterResource(id = R.drawable.set),
                  tint = MaterialTheme.colorScheme.onBackground,
                  contentDescription = "设置或搜索",
                  modifier = Modifier
                    .fillMaxHeight()
                    .padding(0.dp, 6.dp)
                    .clickable(onClick = { navController.navigate("set") })
                )
              }
            }
          }
          item {
            Spacer(modifier = Modifier.height(30.dp))
          }
          if (!isShowLoading.isRefreshing && !JSONObject(info[0]).has("month")) {
            thread {
              isShowLoading.isRefreshing = true
              NETWORK.getBooksUpdate()
              items[0] = DB.getUpdateBooks()
              info[0] = DB.getStatistics()
              isShowLoading.isRefreshing = false
            }
          } else {
            //最近更新
            item {
              Box(
                modifier = Modifier
                  .padding(18.dp, 0.dp)
                  .clip(shape = RoundedCornerShape(16.dp))
                  .background(MaterialTheme.colorScheme.surfaceVariant)
                  .clickable { navController.navigate("books") }
              ) {
                Column(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 12.dp)
                ) {
                  Text(
                    text = "最新更新:",
                    modifier = Modifier
                      .padding(6.dp, 4.dp, 0.dp, 12.dp)
                  )
                  for (index in items[0].indices) {
                    BookCard(msg = JSONObject(items[0][index]), back = MaterialTheme.colorScheme.surface, onClick = {
                      DATA.setDataBook(JSONObject(items[0][index]))
                      navController.navigate("read")
                                                                                                                    }, onLongClick = {
                      DATA.setDataBook(JSONObject(items[0][index]))
                      navController.navigate("detail")
                                                                                                                    })
                    if (index < items.size - 1) {
                      Spacer(modifier = Modifier.height(12.dp))
                    }
                  }
                }
              }
            }
            //空白
            item {
              Spacer(modifier = Modifier.height(20.dp))
            }
            //本月阅读统计
            item {
              Box(
                modifier = Modifier
                  .padding(18.dp, 0.dp)
                  .clip(shape = RoundedCornerShape(16.dp))
                  .background(MaterialTheme.colorScheme.surfaceVariant)
              ) {
                Column(
                  modifier = Modifier
                    .padding(10.dp, 12.dp)
                ) {
                  Text(
                    text = "本月阅读统计:",
                    modifier = Modifier
                      .padding(6.dp, 4.dp, 0.dp, 12.dp)
                  )
                  //本月阅读热力图
                  Box(
                    modifier = Modifier
                      .padding(0.dp, 0.dp, 0.dp, 12.dp)
                      .clip(shape = RoundedCornerShape(12.dp))
                      .background(MaterialTheme.colorScheme.surface)
                  ) {
                    val heatMap = JSONObject(info[0]).getJSONArray("heatMap")
                    Row(
                      modifier = Modifier
                        .padding(14.dp, 16.dp)
                        .background(MaterialTheme.colorScheme.surface)
                    ) {
                      //月份
                      Column {
                        Text(text = JSONObject(info[0]).getInt("month").toString() + "月")
                      }
                      //热力图
                      Column(
                        modifier = Modifier
                          .padding(18.dp, 0.dp, 8.dp, 0.dp)
                      ) {
                        var index = 0
                        //前4行肯定有，一直到28号
                        for (i1 in 1..4) {
                          Row(
                            modifier = Modifier
                              .padding(0.dp, 2.dp),
                          ) {
                            for (i2 in 1..7) {
                              val ope = String.format("%.1f", (heatMap.getJSONObject(index).getInt("hourCount").toFloat() / 18)).toFloat()
                              index++
                              Box(
                                modifier = Modifier
                                  .size(20.dp)
                                  .clip(shape = RoundedCornerShape(4.dp))
                                  .border(1.dp, MaterialTheme.colorScheme.surfaceVariant)
                                  .background(if (ope == 0f) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary.copy(alpha = ope))
                              ) {}
                              if (i2 < 7) Spacer(modifier = Modifier.weight(1f))
                            }
                          }
                        }
                        //28号之后的几天
                        Row(
                          modifier = Modifier
                            .padding(0.dp, 2.dp),
                        ) {
                          for (i2 in 1..7) {
                            //结束
                            if (index >= heatMap.length()) {
                              Box(modifier = Modifier.size(20.dp)) {}
                            } else {
                              val ope = String.format("%.1f", (heatMap.getJSONObject(index).getInt("hourCount").toFloat() / 18)).toFloat()
                              index++
                              Box(
                                modifier = Modifier
                                  .size(20.dp)
                                  .clip(shape = RoundedCornerShape(4.dp))
                                  .border(1.dp, MaterialTheme.colorScheme.surfaceVariant)
                                  .background(if (ope == 0f) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary.copy(alpha = ope))
                              ) {}
                            }
                            if (i2 < 7) Spacer(modifier = Modifier.weight(1f))
                          }
                        }
                      }
                    }
                  }
                  //阅读字数和时长统计
                  Row {
                    //最近阅读字数
                    Column(
                      modifier = Modifier
                        .padding(0.dp, 0.dp, 8.dp, 0.dp)
                        .weight(0.5f)
                        .height(120.dp)
                        .clip(shape = RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                    ) {
                      Text(
                        text = "阅读字数:",
                        fontSize = 14.sp,
                        modifier = Modifier
                          .padding(14.dp, 8.dp, 0.dp, 0.dp)
                      )
                      Text(
                        text = formatNum(JSONObject(info[0]).getInt("wordCount")) + "字",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier
                          .fillMaxWidth()
                          .padding(0.dp, 28.dp, 0.dp, 0.dp),
                      )
                    }
                    //最近阅读时长
                    Column(
                      modifier = Modifier
                        .padding(8.dp, 0.dp, 0.dp, 0.dp)
                        .weight(0.5f)
                        .height(120.dp)
                        .clip(shape = RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                    ) {
                      Text(
                        text = "阅读时长:",
                        fontSize = 14.sp,
                        modifier = Modifier
                          .padding(14.dp, 8.dp, 0.dp, 0.dp)
                      )
                      Text(
                        text = formatNum(JSONObject(info[0]).getInt("hourCount")) + "小时",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                          .fillMaxWidth()
                          .padding(0.dp, 28.dp, 0.dp, 0.dp),
                      )
                    }
                  }
                }
              }
            }
          }
          //空白
          item {
            Spacer(modifier = Modifier.height(30.dp))
          }
          //前往推荐榜
          item {
            Row {
              Spacer(modifier = Modifier.weight(1f))
              Button(
                onClick = { navController.navigate("find") },
                modifier = Modifier
                  .width(200.dp)
              ) {
                Text(
                  text = "去看看推荐榜",
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 6.dp),
                  textAlign = TextAlign.Center,
                  fontSize = 20.sp,
                  fontWeight = FontWeight.Bold
                )
                Image(
                  painter = painterResource(id = R.drawable.next),
                  contentDescription = "前往推荐榜",
                  modifier = Modifier
                    .size(20.dp)
                )
              }
              Spacer(modifier = Modifier.weight(1f))
            }
          }
          //空白
          item {
            Spacer(modifier = Modifier.height(80.dp))
          }
        }
      }
    }
  }
}

//大数字格式化为千、万单位
fun formatNum(num: Int): String {
  //千万
  if (num >= 10000000) {
    return String.format("%.1f", (num.toFloat() / 10000000.0)) + "千万"
  }
  //百万
  if (num >= 1000000) {
    return String.format("%.1f", (num.toFloat() / 1000000.0)) + "百万"
  }
  //万
  if (num >= 10000) {
    return String.format("%.1f", (num.toFloat() / 10000.0)) + "万"
  }
  //千
  if (num >= 1000) {
    return String.format("%.1f", (num.toFloat() / 1000.0)) + "千"
  }
  return num.toString()
}