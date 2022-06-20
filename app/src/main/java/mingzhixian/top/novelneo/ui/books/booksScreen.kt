package mingzhixian.top.novelneo.ui.books

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import mingzhixian.top.novelneo.R
import mingzhixian.top.novelneo.ui.BookCard
import mingzhixian.top.novelneo.ui.DB
import mingzhixian.top.novelneo.ui.NETWORK
import mingzhixian.top.novelneo.ui.NovelNeoBar
import mingzhixian.top.novelneo.ui.theme.NovelNeoTheme
import org.json.JSONObject
import kotlin.concurrent.thread

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksBody(navHostController: NavHostController) {
  //最近更新
  val items1 = rememberSaveable { mutableListOf(listOf(JSONObject().toString())) }
  //暂无更新
  val items2 = rememberSaveable { mutableListOf(listOf(JSONObject().toString())) }
  //已读完
  val items3 = rememberSaveable { mutableListOf(listOf(JSONObject().toString())) }
  //是否显示加载动画
  val isShowLoading = rememberSwipeRefreshState(false)
  //获取数据
  items1[0] = DB.getUpdateBooks()
  items2[0] = DB.getReadBooks()
  items3[0] = DB.getHaveReadBooks()
  NovelNeoTheme {
    Scaffold(
      topBar = { NovelNeoBar(isNeedBack = true, name = "书架", image = R.drawable.search, onClick = {}, navController = navHostController) }
    ) { innerPadding ->
      SwipeRefresh(state = isShowLoading, onRefresh = {
        thread {
          isShowLoading.isRefreshing = true
          NETWORK.getBooksUpdate()
          items1[0] = DB.getUpdateBooks()
          items2[0] = DB.getReadBooks()
          items3[0] = DB.getHaveReadBooks()
          isShowLoading.isRefreshing = false
        }
      }) {
        LazyColumn(
          modifier = Modifier
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
        ) {
          item {
            Spacer(modifier = Modifier.height(20.dp))
          }
          item {
            Text(
              text = "最新更新:",
              modifier = Modifier
                .padding(12.dp, 10.dp)
            )
          }
          item {
            Box(
              modifier = Modifier
                .padding(18.dp, 0.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(10.dp, 12.dp)
              ) {
                val items = items1[0]
                if(!isShowLoading.isRefreshing){
                  for (index in items.indices) {
                    BookCard(msg = JSONObject(items[index]), back = MaterialTheme.colorScheme.surface, onClick = { navHostController.navigate("read") }, onLongClick = { navHostController.navigate("detail?book=" + items[index]) })
                    if (index < items.size - 1) {
                      Spacer(modifier = Modifier.height(12.dp))
                    }
                  }
                }
              }
            }
          }
          item {
            Text(
              text = "暂无更新:",
              modifier = Modifier
                .padding(12.dp, 10.dp)
            )
          }
          item {
            Box(
              modifier = Modifier
                .padding(18.dp, 0.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(10.dp, 12.dp)
              ) {
                val items = items2[0]
                if (!isShowLoading.isRefreshing){
                  for (index in items.indices) {
                    BookCard(msg = JSONObject(items[index]), back = MaterialTheme.colorScheme.surface, onClick = { navHostController.navigate("read") }, onLongClick = { navHostController.navigate("detail?book=" + items[index]) })
                    if (index < items.size - 1) {
                      Spacer(modifier = Modifier.height(12.dp))
                    }
                  }
                }
              }
            }
          }
          item {
            Text(
              text = "已读完:",
              modifier = Modifier
                .padding(12.dp, 10.dp)
            )
          }
          item {
            Box(
              modifier = Modifier
                .padding(18.dp, 0.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(10.dp, 12.dp)
              ) {
                val items = items3[0]
                if (!isShowLoading.isRefreshing){
                  for (index in items.indices) {
                    BookCard(msg = JSONObject(items[index]), back = MaterialTheme.colorScheme.surface, onClick = { navHostController.navigate("read") }, onLongClick = { navHostController.navigate("detail?book=" + items[index]) })
                    if (index < items.size - 1) {
                      Spacer(modifier = Modifier.height(12.dp))
                    }
                  }
                }
              }
            }
          }
          item {
            Spacer(modifier = Modifier.height(80.dp))
          }
        }
      }
    }
  }
}

