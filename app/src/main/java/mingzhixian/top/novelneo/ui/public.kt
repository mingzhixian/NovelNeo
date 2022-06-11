package mingzhixian.top.novelneo.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mingzhixian.top.novelneo.R
import mingzhixian.top.novelneo.ui.theme.NovelNeoTheme
import org.json.JSONObject

@Composable
@Preview(showBackground = true, showSystemUi = false)
fun Pre2() {
  Column(
    modifier = Modifier
      .background(MaterialTheme.colorScheme.secondaryContainer)
      .padding(18.dp, 24.dp)
  ) {
    NovelNeoBar(true, "书架", R.drawable.search, {})
    val msg1 = JSONObject()
    msg1.put("title", "狼王1")
    msg1.put("additional", "第369章 吃葡萄不吐皮")
    msg1.put("cover", R.drawable.cover)
    BookCard(msg = msg1, {}, {})
    Spacer(modifier = Modifier.height(12.dp))
    val msg2 = JSONObject()
    msg2.put("title", "狼王2")
    msg2.put("additional", "第369章 吃葡萄不吐皮")
    msg2.put("cover", R.drawable.cover)
    BookCard(msg = msg2, {}, {})
    Spacer(modifier = Modifier.height(12.dp))
    val msg3 = JSONObject()
    msg3.put("title", "狼王3")
    msg3.put("additional", "第369章 吃葡萄不吐皮")
    msg3.put("cover", R.drawable.cover)
    BookCard(msg = msg3, {}, {})
  }
}

//标题栏
@Composable
fun NovelNeoBar(isNeedBack: Boolean, name: String, image: Int, onClick: () -> Unit) {
  NovelNeoTheme {
    Row(
      modifier = Modifier
        .height(50.dp)
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .padding(10.dp, 6.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      if (isNeedBack) {
        Image(
          painter = painterResource(id = R.drawable.back),
          contentDescription = "返回",
          modifier = Modifier
            .fillMaxHeight()
            .padding(0.dp, 4.dp)
        )
      }
      Text(
        text = name,
        fontSize = 30.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
          .padding(8.dp, 0.dp)
      )
      Spacer(modifier = Modifier.weight(1f))
      Image(
        painter = painterResource(id = image),
        contentDescription = "设置或搜索",
        modifier = Modifier
          .fillMaxHeight()
          .padding(0.dp, 4.dp)
          .clickable(onClick = onClick)
      )
    }
    
  }
}

//书架每本书的卡片
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookCard(msg: JSONObject, onClick: () -> Unit, onLongClick: () -> Unit) {
  //横向排列
  Row(
    modifier = Modifier
      //圆角
      .clip(shape = RoundedCornerShape(18.dp))
      //背景
      .background(MaterialTheme.colorScheme.surface)
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
    Image(
      painter = painterResource(id = msg.getInt("cover")), //描述
      contentDescription = "封面",
      contentScale = ContentScale.Crop,
      modifier = Modifier
        //大小
        .height(80.dp)
        .weight(0.2f)
        .clip(RoundedCornerShape(12.dp)),
      alignment = Alignment.Center,
    )
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
        text = msg.getString("additional"),
        style = MaterialTheme.typography.bodyMedium,
        overflow = TextOverflow.Ellipsis,
        maxLines = 3,
      )
    }
  }
}