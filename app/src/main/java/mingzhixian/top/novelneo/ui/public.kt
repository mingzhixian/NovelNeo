package mingzhixian.top.novelneo.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import mingzhixian.top.novelneo.R
import mingzhixian.top.novelneo.ui.books.BooksBody
import mingzhixian.top.novelneo.ui.detail.DetailBody
import mingzhixian.top.novelneo.ui.find.FindBody
import mingzhixian.top.novelneo.ui.read.ReadBody
import mingzhixian.top.novelneo.ui.search.SearchBody
import mingzhixian.top.novelneo.ui.set.SetBody
import mingzhixian.top.novelneo.ui.sorts.SortsBody
import mingzhixian.top.novelneo.ui.theme.NovelNeoTheme
import org.json.JSONObject

//路由表
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NovelHost() {
  val navController = rememberAnimatedNavController()
  AnimatedNavHost(navController, startDestination = "main") {
    composable("main",
      popEnterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(360)) },
      exitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(360)) }
    ) {
      MainBody(navController = navController)
    }
    composable("find",
      popEnterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(360)) },
      enterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(360)) },
      popExitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(360)) },
      exitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(360)) }
    ) {
      FindBody(navController = navController)
    }
    composable(
      "sorts",
      popEnterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(360)) },
      enterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(360)) },
      popExitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(360)) },
      exitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(360)) },
    ) {
      SortsBody(navController = navController)
    }
    composable("books",
      popEnterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(360)) },
      enterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(360)) },
      popExitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(360)) },
      exitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(360)) }
    ) {
      BooksBody(navHostController = navController)
    }
    composable("set",
      popEnterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(360)) },
      enterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(360)) },
      popExitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(360)) },
      exitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(360)) }
    ) {
      SetBody(navHostController = navController)
    }
    composable("detail?book={book}",
      popEnterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(360)) },
      enterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(360)) },
      popExitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(360)) },
      exitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(360)) }
    ) { backStackEntry ->
      DetailBody(navHostController = navController, JSONObject(backStackEntry.arguments?.getString("book").toString()))
    }
    composable("read?book={book}",
      popEnterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(360)) },
      enterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(360)) },
      popExitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(360)) },
      exitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(360)) }
    ) { backStackEntry ->
      ReadBody(navHostController = navController, JSONObject(backStackEntry.arguments?.getString("book").toString()))
    }
    composable("search",
      popEnterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(360)) },
      enterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(360)) },
      popExitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(360)) },
      exitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(360)) }
    ) {
      SearchBody(navController = navController)
    }
  }
}

//标题栏
@Composable
fun NovelNeoBar(isNeedBack: Boolean, name: String, image: Int, onClick: () -> Unit, navController: NavHostController) {
  NovelNeoTheme {
    TopAppBar(
      backgroundColor = MaterialTheme.colorScheme.surface,
      modifier = Modifier
        .height(50.dp),
      elevation = 8.dp
    ) {
      Row(
        modifier = Modifier
          .height(50.dp)
          .fillMaxWidth()
          .padding(10.dp, 6.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        if (isNeedBack) {
          Icon(
            painter = painterResource(id = R.drawable.back),
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = "返回",
            modifier = Modifier
              .fillMaxHeight()
              .padding(0.dp, 4.dp)
              .clickable { navController.navigateUp() }
          )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
          text = name,
          fontSize = 30.sp,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier
            .padding(8.dp, 0.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        if (image != 0) {
          Icon(
            painter = painterResource(id = image),
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = "设置或搜索",
            modifier = Modifier
              .fillMaxHeight()
              .padding(0.dp, 6.dp)
              .clickable(onClick = onClick)
          )
        }
      }
    }
  }
}

//书架每本书的卡片
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookCard(msg: JSONObject, back: Color, onClick: () -> Unit, onLongClick: () -> Unit) {
  //横向排列
  Row(
    modifier = Modifier
      //圆角
      .clip(shape = RoundedCornerShape(18.dp))
      //背景
      .background(back)
      //高度
      .height(100.dp)
      //内边距
      .padding(12.dp, 9.dp)
      //点击事件
      .combinedClickable(
        onLongClick = onLongClick,
        onClick = onClick
      ),
    verticalAlignment = Alignment.CenterVertically
  ) {
    //封面
    if (msg.getString("cover") == "") {
      Image(
        painter = painterResource(R.drawable.cover),
        contentDescription = "封面",
        contentScale = ContentScale.Crop,
        modifier = Modifier
          //大小
          .height(80.dp)
          .weight(0.2f)
          .clip(RoundedCornerShape(12.dp)),
        alignment = Alignment.Center,
      )
    } else {
      AsyncImage(
        model = msg.getString("cover"), //描述
        contentDescription = "封面",
        contentScale = ContentScale.Crop,
        modifier = Modifier
          //大小
          .height(80.dp)
          .weight(0.2f)
          .clip(RoundedCornerShape(12.dp)),
        alignment = Alignment.Center,
      )
    }
    //文字
    Column(
      modifier = Modifier
        .padding(14.dp, 0.dp, 0.dp, 0.dp)
        .weight(0.8f)
    ) {
      //标题
      Text(
        text = msg.getString("title"),
        style = MaterialTheme.typography.bodyLarge,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        fontWeight = FontWeight.SemiBold,
      )
      //附加信息
      Text(
        text = msg.getString("latest"),
        style = MaterialTheme.typography.bodyMedium,
        overflow = TextOverflow.Ellipsis,
        maxLines = 3,
      )
    }
  }
}

//发现页用每本书的列表项
@Composable
fun BookItem(msg: JSONObject, onClick: () -> Unit) {
//横向排列
  Row(
    modifier = Modifier
      //内边距
      .padding(18.dp, 6.dp)
      .clickable(onClick = onClick)
  ) {
    //标题
    Text(
      text = "《" + msg.getString("title") + "》",
      color = MaterialTheme.colorScheme.onSurface,
      overflow = TextOverflow.Ellipsis,
      fontSize = 16.sp,
      maxLines = 1,
      modifier = Modifier
        .weight(0.5f)
    )
    //作者
    Text(
      text = "作者:" + msg.getString("author"),
      color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
      overflow = TextOverflow.Ellipsis,
      fontSize = 14.sp,
      maxLines = 1,
      modifier = Modifier
        .weight(0.3f)
        .padding(0.dp, 1.dp)
    )
    //类别
    Text(
      text = msg.getString("sort"),
      color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
      overflow = TextOverflow.Ellipsis,
      fontSize = 14.sp,
      maxLines = 1,
      modifier = Modifier
        .weight(0.2f)
        .padding(0.dp, 1.dp)
    )
  }
}
