package mingzhixian.top.novelneo.ui.find

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import mingzhixian.top.novelneo.R
import mingzhixian.top.novelneo.ui.BookItem
import mingzhixian.top.novelneo.ui.DATA
import mingzhixian.top.novelneo.ui.NETWORK
import mingzhixian.top.novelneo.ui.NovelNeoBar
import mingzhixian.top.novelneo.ui.theme.NovelNeoTheme
import org.json.JSONObject
import kotlin.concurrent.thread

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindBody(navController: NavHostController) {
  val mustReadBooks = rememberSaveable { mutableListOf(listOf(JSONObject().toString())) }
  val isShowLoading = rememberSwipeRefreshState(false)
  NovelNeoTheme {
    Scaffold(
      topBar = { NovelNeoBar(isNeedBack = true, name = "发现", image = R.drawable.search, onClick = {navController.navigate("search")}, navController = navController) }
    ) { innerPadding ->
      SwipeRefresh(state = isShowLoading, onRefresh = {
        thread {
          mustReadBooks[0] = NETWORK.getMustReadBooks()
          isShowLoading.isRefreshing = false
        }
      }, modifier = Modifier.padding(innerPadding)) {
        LazyColumn {
          //必读榜图片
          item {
            Image(
              painter = painterResource(id = R.drawable.bidubang),
              contentDescription = "必读榜",
              modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp, 20.dp)
                .clip(shape = RoundedCornerShape(12.dp))
            )
          }
          //网络加载
          if (!isShowLoading.isRefreshing && mustReadBooks[0].size == 1) {
            isShowLoading.isRefreshing = true
            thread {
              mustReadBooks[0] = NETWORK.getMustReadBooks()
              isShowLoading.isRefreshing = false
            }
          } else {
            //榜前三
            item {
              Row(
                modifier = Modifier
                  .padding(18.dp, 0.dp)
              ) {
                for (i in 0..2) {
                  Box(modifier = Modifier.weight(0.28f)) {
                    ThreeCard(msg = JSONObject(mustReadBooks[0][i]), onClick = {
                      DATA.setDataBook(JSONObject(mustReadBooks[0][i]))
                      navController.navigate("detail")})
                  }
                  if (i < 2) {
                    Spacer(modifier = Modifier.weight(0.08f))
                  }
                }
              }
            }
            //空白
            item {
              Spacer(modifier = Modifier.height(20.dp))
            }
            //榜单
            itemsIndexed(mustReadBooks[0].slice(3 until mustReadBooks[0].size)) { index, msg ->
              BookItem(msg = JSONObject(msg), onClick = {
                DATA.setDataBook(JSONObject(msg))
                navController.navigate("detail") })
              if (index < mustReadBooks.size - 4) {
                Divider(
                  thickness = 1.dp,
                  modifier = Modifier.padding(30.dp, 2.dp),
                  color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                )
              }
            }
          }
          //前往所有分类
          item {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(18.dp, 20.dp)
                .clip(shape = RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable { navController.navigate("sorts") }
            ) {
              Text(
                text = "前往所有分类",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                  .fillMaxHeight()
                  .padding(50.dp, 36.dp, 0.dp, 36.dp),
              )
              Spacer(modifier = Modifier.weight(1f))
              Image(
                painter = painterResource(id = R.drawable.next),
                contentDescription = "前往所有分类",
                modifier = Modifier
                  .fillMaxHeight()
                  .padding(0.dp, 34.dp, 30.dp, 34.dp)
              )
            }
          }
          //空白
          item {
            Spacer(modifier = Modifier.height(50.dp))
          }
        }
      }
    }
  }
}

//必读榜前三卡片
@Composable
fun ThreeCard(msg: JSONObject, onClick: () -> Unit) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
  ) {
    //封面
    if (msg.getString("cover") == "") {
      Image(
        painter = painterResource(R.drawable.cover),
        contentDescription = "榜单前三",
        modifier = Modifier
          .fillMaxWidth()
          .padding(4.dp, 2.dp)
          .clip(shape = RoundedCornerShape(12.dp))
          .clickable(onClick = onClick)
      )
    } else {
      AsyncImage(
        model = msg.getString("cover"), //描述
        contentDescription = "榜单前三",
        modifier = Modifier
          .fillMaxWidth()
          .padding(4.dp, 2.dp)
          .clip(shape = RoundedCornerShape(12.dp))
          .clickable(onClick = onClick)
      )
    }
    //标题
    Text(
      text = "《" + msg.getString("title") + "》",
      color = MaterialTheme.colorScheme.onSurface,
      overflow = TextOverflow.Ellipsis,
      fontSize = 16.sp,
      maxLines = 1,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .fillMaxWidth()
    )
    //作者
    Text(
      text = "作者:" + msg.getString("author"),
      color = MaterialTheme.colorScheme.onSurface,
      overflow = TextOverflow.Ellipsis,
      fontSize = 14.sp,
      maxLines = 1,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .fillMaxWidth()
    )
  }
}