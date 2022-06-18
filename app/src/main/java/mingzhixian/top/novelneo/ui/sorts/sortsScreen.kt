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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import mingzhixian.top.novelneo.R
import mingzhixian.top.novelneo.ui.BookCard
import mingzhixian.top.novelneo.ui.NETWORK
import mingzhixian.top.novelneo.ui.NovelNeoBar
import mingzhixian.top.novelneo.ui.theme.NovelNeoTheme

@Composable
@Preview
fun Pre() {
  SortsBody(navController = rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortsBody(navController: NavHostController) {
  NovelNeoTheme {
    Scaffold(
      //todo 添加搜索函数
      topBar = { NovelNeoBar(isNeedBack = true, name = "分类", image = R.drawable.search, onClick = {}, navController = navController) }
    ) { innerPadding ->
      Row(modifier = Modifier.padding(innerPadding)) {
        //所有分类
        val sorts = NETWORK.getAllSorts()
        var selectSort by remember { mutableStateOf(0) }
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
                .clickable { selectSort = index }
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
        LazyColumn(
          modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(18.dp, 20.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          //列表
          val books = NETWORK.getSortBooks(sorts[selectSort])
          items(books) { book ->
            BookCard(book, MaterialTheme.colorScheme.surfaceVariant, {navController.navigate("detail?book=$book")}, {})
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