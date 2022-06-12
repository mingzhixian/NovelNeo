package mingzhixian.top.novelneo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mingzhixian.top.novelneo.R
import mingzhixian.top.novelneo.ui.theme.NovelNeoTheme
import java.util.*

@Preview(showBackground = false, showSystemUi = false)
@Composable
fun Pre1() {
  MainBody()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainBody() {
  NovelNeoTheme {
    Scaffold(
      //todo 添加设置界面
      topBar = { NovelNeoBar(isNeedBack = false, name = "NovelNeo", image = R.drawable.set, onClick = {}) }
    ) { innerPadding ->
      LazyColumn(
        modifier = Modifier
          .padding(innerPadding)
          .background(MaterialTheme.colorScheme.background)
      ) {
        item {
          Spacer(modifier = Modifier.height(30.dp))
        }
        //最近更新
        //todo 添加点击事件,以及下拉更新
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
              Text(
                text = "最新更新:",
                modifier = Modifier
                  .padding(6.dp, 4.dp, 0.dp, 12.dp)
              )
              val items = DB.getUpdateBooks()
              for (index in items.indices) {
                BookCard(msg = items[index], onClick = {}, onLongClick = {})
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
          val info = DB.getStatistics()
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
                val heatMap = info.getJSONArray("heatMap")
                Row(
                  modifier = Modifier
                    .padding(14.dp, 16.dp)
                    .background(MaterialTheme.colorScheme.surface)
                ) {
                  //月份
                  Column {
                    Text(text = Calendar.getInstance().get(Calendar.MONTH).toString() + "月")
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
                    text = formatNum(info.getInt("wordCount")) + "字",
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
                    text = formatNum(info.getInt("hourCount")) + "小时",
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
        //空白
        item {
          Spacer(modifier = Modifier.height(80.dp))
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
