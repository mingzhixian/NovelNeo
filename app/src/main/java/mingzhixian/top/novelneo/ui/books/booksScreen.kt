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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import mingzhixian.top.novelneo.R
import mingzhixian.top.novelneo.ui.BookCard
import mingzhixian.top.novelneo.ui.DB
import mingzhixian.top.novelneo.ui.NovelNeoBar
import mingzhixian.top.novelneo.ui.theme.NovelNeoTheme
import org.json.JSONObject

@Composable
@Preview
fun Pre1() {
  BooksBody(navHostController = rememberNavController())
}

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksBody(navHostController: NavHostController) {
  NovelNeoTheme {
    Scaffold(
      topBar = { NovelNeoBar(isNeedBack = true, name = "书架", image = R.drawable.search, onClick = {}, navController = navHostController) }
    ) { innerPadding ->
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
              var items by remember { mutableStateOf(ArrayList<JSONObject>()) }
              LaunchedEffect(1) {
                items = DB.getUpdateBooks()
              }
              for (index in items.indices) {
                BookCard(msg = items[index], back = MaterialTheme.colorScheme.surface, onClick = { navHostController.navigate("read") }, onLongClick = { navHostController.navigate("detail") })
                if (index < items.size - 1) {
                  Spacer(modifier = Modifier.height(12.dp))
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
              var items by remember { mutableStateOf(ArrayList<JSONObject>()) }
              LaunchedEffect(1) {
                items = DB.getReadBooks()
              }
              for (index in items.indices) {
                BookCard(msg = items[index], back = MaterialTheme.colorScheme.surface, onClick = { navHostController.navigate("read") }, onLongClick = { navHostController.navigate("detail") })
                if (index < items.size - 1) {
                  Spacer(modifier = Modifier.height(12.dp))
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
              val  items = DB.getHaveReadBooks()
              for (index in items.indices) {
                BookCard(msg = items[index], back = MaterialTheme.colorScheme.surface, onClick = { navHostController.navigate("read") }, onLongClick = { navHostController.navigate("detail") })
                if (index < items.size - 1) {
                  Spacer(modifier = Modifier.height(12.dp))
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

