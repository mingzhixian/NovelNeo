package mingzhixian.top.novelneo.ui.sorts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import mingzhixian.top.novelneo.ui.BookCard
import mingzhixian.top.novelneo.ui.NETWORK
import mingzhixian.top.novelneo.ui.NovelNeoBar
import mingzhixian.top.novelneo.ui.theme.NovelNeoTheme
import org.json.JSONObject
import kotlin.concurrent.thread

@Preview
@Composable
fun Pre() {
  SortsBody(rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortsBody(navController: NavHostController) {
  //所有分类(本项目只有一个源，数量固定，所以不再从网络加载)
  val sorts = listOf(
    JSONObject("{\"name\":\"玄幻小说\",\"url\":\"https:\\/\\/www.exiaoshuo.com\\/xuanhuan\\/\"}"),
    JSONObject("{\"name\":\"修真小说\",\"url\":\"https:\\/\\/www.exiaoshuo.com\\/xiuzhen\\/\"}"),
    JSONObject("{\"name\":\"都市小说\",\"url\":\"https:\\/\\/www.exiaoshuo.com\\/dushi\\/\"}"),
    JSONObject("{\"name\":\"历史小说\",\"url\":\"https:\\/\\/www.exiaoshuo.com\\/lishi\\/\"}"),
    JSONObject("{\"name\":\"网游小说\",\"url\":\"https:\\/\\/www.exiaoshuo.com\\/wangyou\\/\"}"),
    JSONObject("{\"name\":\"科幻小说\",\"url\":\"https:\\/\\/www.exiaoshuo.com\\/kehuan\\/\"}"),
    JSONObject("{\"name\":\"言情小说\",\"url\":\"https:\\/\\/www.exiaoshuo.com\\/yanqing\\/\"}"),
    JSONObject("{\"name\":\"其他小说\",\"url\":\"https:\\/\\/www.exiaoshuo.com\\/qita\\/\"}"),
    JSONObject("{\"name\":\"全本小说\",\"url\":\"https:\\/\\/www.exiaoshuo.com\\/quanben\\/\"}")
  )
  //列表
  val books = rememberSaveable { mutableListOf(ArrayList<JSONObject>(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList()) }
  var selectSort by rememberSaveable { mutableStateOf(-1) }
  val isShowLoading = rememberSwipeRefreshState(false)
  NovelNeoTheme {
    Scaffold(
      //todo 添加搜索函数
      topBar = { NovelNeoBar(isNeedBack = true, name = "分类", image = mingzhixian.top.novelneo.R.drawable.search, onClick = {}, navController = navController) }
    ) { innerPadding ->
      Row(modifier = Modifier.padding(innerPadding)) {
        //所有分类
        LazyColumn(
          modifier = Modifier
            //宽度
            .width(100.dp)
            //撑起全部高度
            .fillMaxHeight()
            //背景颜色
            .background(MaterialTheme.colorScheme.surface)
            //内边距
            .padding(8.dp, 12.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          itemsIndexed(sorts) { index, sort ->
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(20.dp))
                .clickable {
                  selectSort = index
                  if (books[selectSort].size == 0) {
                    isShowLoading.isRefreshing = true
                    thread {
                      books[selectSort] = NETWORK.getSortBooks(sorts[selectSort])
                      isShowLoading.isRefreshing = false
                    }
                  }
                }
                .background(if (selectSort == index) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surface)
            ) {
              Text(
                text = sort.getString("name"),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = (if (selectSort == index) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurface),
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(0.dp, 8.dp),
              )
            }
          }
        }
        //分类下图书排行榜
        SwipeRefresh(state = isShowLoading, onRefresh = {
          thread {
            books[selectSort] = NETWORK.getSortBooks(sorts[selectSort])
            isShowLoading.isRefreshing = false
          }
        }, modifier = Modifier.weight(1f)) {
          LazyColumn(
            modifier = Modifier
              .fillMaxWidth()
              .fillMaxHeight()
              .background(MaterialTheme.colorScheme.background)
              .padding(18.dp, 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            if (!isShowLoading.isRefreshing && selectSort == -1) {
              isShowLoading.isRefreshing = true
              thread {
                books[0] = NETWORK.getSortBooks(sorts[0])
                selectSort = 0
                isShowLoading.isRefreshing = false
              }
            } else {
              //列表
              items(books[selectSort]) { book ->
                BookCard(book, MaterialTheme.colorScheme.surfaceVariant, { navController.navigate("detail?book=$book") }, {})
              }
            }
            //底部空白
            item {
              Spacer(modifier = Modifier.height(100.dp))
            }
          }
        }
      }
    }
  }
}