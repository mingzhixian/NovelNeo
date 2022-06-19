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
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import mingzhixian.top.novelneo.ui.BookCard
import mingzhixian.top.novelneo.ui.NETWORK
import mingzhixian.top.novelneo.ui.NovelNeoBar
import mingzhixian.top.novelneo.ui.theme.NovelNeoTheme
import org.json.JSONObject
import kotlin.concurrent.thread

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortsBody(navController: NavHostController) {
  //所有分类
  var sorts = ArrayList<JSONObject>()
  //列表
  var books = rememberSaveable { mutableListOf(ArrayList<JSONObject>()) }
  var selectSort by rememberSaveable  { mutableStateOf(0) }
  //var reLoadSorts by remember { mutableStateOf(false) }
  //协程获取分类(因为本项目只有一个源，此处分类是固定不变的，故不再使用协程)
  sorts.add(JSONObject("{\"name\":\"玄幻小说\",\"url\":\"https:\\/\\/www.exiaoshuo.com\\/xuanhuan\\/\"}"))
  sorts.add(JSONObject("{\"name\":\"修真小说\",\"url\":\"https:\\/\\/www.exiaoshuo.com\\/xiuzhen\\/\"}"))
  sorts.add(JSONObject("{\"name\":\"都市小说\",\"url\":\"https:\\/\\/www.exiaoshuo.com\\/dushi\\/\"}"))
  sorts.add(JSONObject("{\"name\":\"历史小说\",\"url\":\"https:\\/\\/www.exiaoshuo.com\\/lishi\\/\"}"))
  sorts.add(JSONObject("{\"name\":\"网游小说\",\"url\":\"https:\\/\\/www.exiaoshuo.com\\/wangyou\\/\"}"))
  sorts.add(JSONObject("{\"name\":\"科幻小说\",\"url\":\"https:\\/\\/www.exiaoshuo.com\\/kehuan\\/\"}"))
  sorts.add(JSONObject("{\"name\":\"言情小说\",\"url\":\"https:\\/\\/www.exiaoshuo.com\\/yanqing\\/\"}"))
  sorts.add(JSONObject("{\"name\":\"其他小说\",\"url\":\"https:\\/\\/www.exiaoshuo.com\\/qita\\/\"}"))
  sorts.add(JSONObject("{\"name\":\"全本小说\",\"url\":\"https:\\/\\/www.exiaoshuo.com\\/quanben\\/\"}"))
//  thread {
//    sorts = NETWORK.getAllSorts()
//    reLoadSorts = !reLoadSorts
//    selectSort = 0
//    thread {
//      books = NETWORK.getSortBooks(sorts[selectSort])
//      reLoadBooks = !reLoadBooks
//    }
//  }
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
          //如果加载完成则更新ui(暂停使用)
//          if (reLoadSorts || !reLoadSorts) {
          itemsIndexed(sorts) { index, sort ->
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(20.dp))
                .clickable {
                  selectSort = index
                  thread {
                    books = NETWORK.getSortBooks(sorts[selectSort])
                    reLoadBooks = !reLoadBooks
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
        //}
        //分类下图书排行榜
        LazyColumn(
          modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(18.dp, 20.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          //重新加载列表
          if (reLoadBooks || !reLoadBooks) {
            items(books) { book ->
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