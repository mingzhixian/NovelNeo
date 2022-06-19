package mingzhixian.top.novelneo.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.BlurTransformation
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import mingzhixian.top.novelneo.R
import mingzhixian.top.novelneo.ui.DB
import mingzhixian.top.novelneo.ui.NETWORK
import mingzhixian.top.novelneo.ui.theme.NovelNeoTheme
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBody(navHostController: NavHostController, m: JSONObject) {
  NovelNeoTheme {
    val isShowLoading = rememberSwipeRefreshState(true)
    val msg = rememberSaveable { mutableListOf(JSONObject()) }
    var isShowMenu by remember { mutableStateOf(false) }
    var isClickBooks by remember { mutableStateOf(false) }
    SwipeRefresh(state = isShowLoading, onRefresh = {
      thread {
        msg[0] = NETWORK.getDetail(m)
        isShowLoading.isRefreshing = false
      }
    }) {
      if (isShowLoading.isRefreshing) {
        thread {
          msg[0] = NETWORK.getDetail(m)
          isShowLoading.isRefreshing = false
        }
      } else {
        Scaffold(
          //todo 立即阅读界面
          bottomBar = {
            if (!isShowMenu) DetailBottomBar(msg[0], isClickBooks, { isShowMenu = !isShowMenu }, {}, {
              if (DB.isInBooks(msg = msg[0])) {
                DB.deleteBook(msg = msg[0])
                isClickBooks = !isClickBooks
              } else {
                DB.newBook(msg = msg[0])
                isClickBooks = !isClickBooks
              }
            })
          }
        ) { innerPadding ->
          //上半部分
          Box(modifier = Modifier.padding(innerPadding)) {
            //顶部背景图片
            AsyncImage(
              //网络图片
              model = ImageRequest.Builder(LocalContext.current)
                .data(msg[0].getString("cover"))
                .transformations(BlurTransformation(LocalContext.current))
                .build(),
              contentDescription = "头图背景",
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .height(280.dp)
                .blur(20.dp)
            )
            //返回按钮
            Image(
              painter = painterResource(id = R.drawable.back),
              contentDescription = "返回",
              modifier = Modifier
                //外边距
                .padding(18.dp, 8.dp)
                .size(30.dp)
                .clickable { navHostController.navigateUp() }
            )
            //封面及书名作者
            Row(modifier = Modifier.background(MaterialTheme.colorScheme.background.copy(0f))) {
              //封面
              AsyncImage(
                //网络图片
                model = msg[0].getString("cover"),
                contentDescription = "封面",
                modifier = Modifier
                  .padding(40.dp, 80.dp, 0.dp, 40.dp)
                  .height(160.dp),
              )
              Column(
                modifier = Modifier
                  .height(280.dp)
                  .padding(10.dp, 0.dp)
              ) {
                Spacer(modifier = Modifier.weight(1f))
                //标题
                Text(
                  text = "《" + msg[0].getString("title") + "》",
                  color = Color.White,
                  overflow = TextOverflow.Ellipsis,
                  fontSize = 18.sp,
                  maxLines = 2,
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 6.dp),
                  textAlign = TextAlign.Center
                )
                //作者
                Text(
                  text = "作者:" + msg[0].getString("author"),
                  color = Color.White.copy(alpha = 0.8f),
                  overflow = TextOverflow.Ellipsis,
                  fontSize = 16.sp,
                  maxLines = 1,
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 4.dp),
                  textAlign = TextAlign.Center
                )
                //类别
                Text(
                  text = msg[0].getString("sort"),
                  color = Color.White.copy(alpha = 0.6f),
                  overflow = TextOverflow.Ellipsis,
                  fontSize = 16.sp,
                  maxLines = 1,
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 4.dp),
                  textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1f))
              }
            }
          }
          //下半部分
          LazyColumn(
            modifier = Modifier
              .padding(0.dp, 276.dp, 0.dp, 80.dp)
              .clip(shape = RoundedCornerShape(18.dp))
              .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            item {
              //详情
              Text(
                text = msg[0].getString("content"),
                fontSize = 16.sp,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(18.dp, 18.dp)
              )
            }
          }
          //目录
          if (isShowMenu) Menu(menu = msg[0].getJSONArray("menu")) { isShowMenu = !isShowMenu }
        }
      }
    }
  }
}

//底部栏
@Composable
fun DetailBottomBar(msg: JSONObject, isClickBooks: Boolean, onClick1: () -> Unit, onClick2: () -> Unit, onClick3: () -> Unit) {
  Row(modifier = Modifier.height(60.dp)) {
    Column(
      modifier = Modifier
        .weight(1f)
        .clickable(onClick = onClick1),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Image(
        painter = painterResource(R.drawable.menu),
        contentDescription = "目录",
        modifier = Modifier.height(30.dp)
      )
      Text(
        text = "目录列表",
        fontSize = 16.sp
      )
    }
    Column(
      modifier = Modifier
        .weight(1f)
        .clickable(onClick = onClick2),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Image(
        painter = painterResource(R.drawable.read_now),
        contentDescription = "阅读",
        modifier = Modifier.height(30.dp)
      )
      Text(
        text = "立即阅读",
        fontSize = 16.sp
      )
    }
    Column(
      modifier = Modifier
        .weight(1f)
        .clickable(onClick = onClick3),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Image(
        painter = painterResource(R.drawable.books),
        contentDescription = "书架",
        modifier = Modifier.height(30.dp)
      )
      if (isClickBooks || !isClickBooks) {
        Text(
          text = if (DB.isInBooks(msg = msg)) "移出书架" else "加入书架",
          fontSize = 16.sp
        )
      }
    }
  }
}

//目录
@Composable
fun Menu(menu: JSONArray, onClick: () -> Unit) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background.copy(0f)),
    contentAlignment = Alignment.BottomCenter
  ) {
    BoxWithConstraints(
      modifier = Modifier
        .clip(shape = RoundedCornerShape(20.dp))
        .background(MaterialTheme.colorScheme.background)
    ) {
      Image(
        painter = painterResource(R.drawable.pull_down), contentDescription = "返回", modifier = Modifier
          .fillMaxWidth()
          .height(40.dp)
          .clickable(onClick = onClick),
        alignment = Alignment.Center
      )
      LazyColumn(
        modifier = Modifier
          .height(maxHeight - 150.dp)
          .padding(10.dp, 30.dp, 10.dp, 0.dp)
      ) {
        item {
          for (index in 0 until menu.length()) {
            MenuItem(menu.getJSONObject(index))
            if (index < menu.length() - 1) {
              Divider(
                thickness = 1.dp,
                modifier = Modifier.padding(20.dp, 0.dp),
                color = MaterialTheme.colorScheme.onBackground.copy(0.4f)
              )
            }
          }
        }
      }
    }
  }
}

//目录用，条目
@Composable
fun MenuItem(msg: JSONObject) {
  Row(
    modifier = Modifier
      .padding(12.dp, 8.dp)
      .fillMaxWidth()
      .height(18.dp)
      .clickable {},
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = msg.getString("title"),
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      fontSize = 16.sp,
    )
    Spacer(modifier = Modifier.weight(1f))
    Image(
      painter = painterResource(R.drawable.next_thin),
      contentDescription = "查看目录",
      modifier = Modifier
        .padding(0.dp, 4.dp, 0.dp, 4.dp)
        .fillMaxHeight()
    )
  }
}