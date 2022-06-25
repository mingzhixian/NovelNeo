package mingzhixian.top.novelneo.ui.detail

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
import mingzhixian.top.novelneo.ui.DATA
import mingzhixian.top.novelneo.ui.DB
import mingzhixian.top.novelneo.ui.NETWORK
import mingzhixian.top.novelneo.ui.NovelNeoBar
import mingzhixian.top.novelneo.ui.theme.NovelNeoTheme
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBody(navHostController: NavHostController, m: JSONObject) {
  //compose会重复绘制，用此避免重复获取
  var isFirst by rememberSaveable { mutableStateOf(true) }
  val isShowLoading = rememberSwipeRefreshState(true)
  var isShowMenu by rememberSaveable { mutableStateOf(false) }
  var isClickBooks by remember { mutableStateOf(false) }
  NovelNeoTheme {
    Scaffold(
      topBar = { NovelNeoBar(isNeedBack = true, name = "详情", image = 0, onClick = {}, navController = navHostController) },
      bottomBar = {
        if (!isShowMenu) {
          if (!isShowLoading.isRefreshing) {
            DetailBottomBar(isClickBooks, { isShowMenu = !isShowMenu }, { navHostController.navigate("read") }) {
              isClickBooks = if (DB.isInBooks(msg = DATA.getDataBook())) {
                DB.deleteBook(msg = DATA.getDataBook())
                !isClickBooks
              } else {
                DB.newBook(msg = DATA.getDataBook())
                !isClickBooks
              }
            }
          } else {
            //在未加载完成之前不允许点击
            DetailBottomBar(isClickBooks, { }, { }, {})
          }
        }
      }
    ) { innerPadding ->
      SwipeRefresh(state = isShowLoading, onRefresh = {
        isShowLoading.isRefreshing = true
        thread {
          DATA.setDataBook(NETWORK.getDetail(m))
          isShowLoading.isRefreshing = false
        }
      }, modifier = Modifier.padding(innerPadding)) {
        if (!isShowLoading.isRefreshing) {
          val msg = DATA.getDataBook()
          //上半部分
          Box {
            AsyncImage(
              //网络图片
              model = ImageRequest.Builder(LocalContext.current)
                .data(msg.getString("cover"))
                .transformations(BlurTransformation(LocalContext.current))
                .build(),
              contentDescription = "头图背景",
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .height(240.dp)
                .padding(10.dp)
                .clip(shape = RoundedCornerShape(12.dp)),
            )
            //封面及书名作者
            Row(
              modifier = Modifier
                .height(240.dp)
                .padding(10.dp)
                .clip(shape = RoundedCornerShape(12.dp))
                .background(Color.DarkGray.copy(0.4f))
            ) {
              //封面
              AsyncImage(
                //网络图片
                model = msg.getString("cover"),
                contentDescription = "封面",
                modifier = Modifier
                  .padding(28.dp, 40.dp, 0.dp, 40.dp)
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
                  text = "《" + msg.getString("title") + "》",
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
                  text = "作者:" + msg.getString("author"),
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
                  text = msg.getString("sort"),
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
              .padding(0.dp, 240.dp, 0.dp, 0.dp)
              .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            item {
              //详情
              Text(
                text = msg.getString("content"),
                fontSize = 16.sp,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(18.dp, 18.dp)
              )
            }
          }
          //目录
          if (isShowMenu) Menu(msg, { isShowMenu = !isShowMenu }, navHostController)
        }
      }
    }
  }
  if (!DATA.getDataBook().has("menu") && isFirst) {
    isFirst = false
    thread {
      DATA.setDataBook(NETWORK.getDetail(m))
      isShowLoading.isRefreshing = false
    }
  } else if (DATA.getDataBook().has("menu")) {
    isShowLoading.isRefreshing = false
  }
}

//底部栏
@Composable
fun DetailBottomBar(isClickBooks: Boolean, onClick1: () -> Unit, onClick2: () -> Unit, onClick3: () -> Unit) {
  Row(modifier = Modifier.height(60.dp)) {
    Column(
      modifier = Modifier
        .weight(1f)
        .clickable(onClick = onClick1),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Icon(
        painter = painterResource(R.drawable.menu),
        tint = MaterialTheme.colorScheme.onBackground,
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
      Icon(
        painter = painterResource(R.drawable.read_now),
        tint = MaterialTheme.colorScheme.onBackground,
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
      Icon(
        painter = painterResource(R.drawable.books),
        tint = MaterialTheme.colorScheme.onBackground,
        contentDescription = "书架",
        modifier = Modifier.height(30.dp)
      )
      if (isClickBooks || !isClickBooks) {
        Text(
          text = if (DB.isInBooks(msg = DATA.getDataBook())) "移出书架" else "加入书架",
          fontSize = 16.sp
        )
      }
    }
  }
}

//目录
@Composable
fun Menu(msg: JSONObject, onClick: () -> Unit, navHostController: NavHostController) {
  val menu: JSONArray = msg.getJSONArray("menu")
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
      Icon(
        tint = MaterialTheme.colorScheme.onBackground,
        painter = painterResource(R.drawable.pull_down), contentDescription = "返回",
        modifier = Modifier
          .fillMaxWidth()
          .height(40.dp)
          .clickable(onClick = onClick)
          .align(Alignment.TopCenter),
      )
      LazyColumn(
        modifier = Modifier
          .height(maxHeight - 150.dp)
          .padding(10.dp, 30.dp, 10.dp, 0.dp)
      ) {
        for (index in 0 until menu.length()) {
          item {
            MenuItem(menu.getJSONObject(index)) {
              msg.put("current", index)
              DATA.setDataBook(msg)
              navHostController.navigate("read")
            }
          }
          if (index < menu.length() - 1) {
            item {
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
fun MenuItem(msg: JSONObject, onClick: () -> Unit) {
  Row(
    modifier = Modifier
      .padding(12.dp, 8.dp)
      .fillMaxWidth()
      .height(18.dp)
      .clickable { onClick() },
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = msg.getString("title"),
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      fontSize = 16.sp,
    )
    Spacer(modifier = Modifier.weight(1f))
    Icon(
      painter = painterResource(R.drawable.next_thin),
      tint = MaterialTheme.colorScheme.onBackground,
      contentDescription = "查看目录",
      modifier = Modifier
        .padding(0.dp, 4.dp, 0.dp, 4.dp)
        .fillMaxHeight()
    )
  }
}
