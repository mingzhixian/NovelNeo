package mingzhixian.top.novelneo.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import mingzhixian.top.novelneo.R
import mingzhixian.top.novelneo.ui.BookCard
import mingzhixian.top.novelneo.ui.NETWORK
import mingzhixian.top.novelneo.ui.NovelNeoBar
import mingzhixian.top.novelneo.ui.theme.NovelNeoTheme
import org.json.JSONObject
import kotlin.concurrent.thread

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBody(navController: NavHostController) {
  val searchBooks = rememberSaveable { mutableListOf(listOf(JSONObject().toString())) }
  var search by remember { mutableStateOf("") }
  val isShowLoading = rememberSwipeRefreshState(false)
  NovelNeoTheme {
    Scaffold(
      topBar = { NovelNeoBar(isNeedBack = true, name = "搜索", image = 0, onClick = {}, navController = navController) }
    ) { innerPadding ->
      Box(modifier = Modifier.padding(innerPadding)) {
        //搜索框
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
          search, onValueChange = { search = it }, enabled = true, readOnly = false,
          modifier = Modifier
            .padding(20.dp, 20.dp)
            .fillMaxWidth()
            .height(60.dp),
          singleLine = true,
          shape = RoundedCornerShape(20.dp),
          trailingIcon = {
            Icon(
              painter = painterResource(R.drawable.search), contentDescription = "搜索",
              tint = MaterialTheme.colorScheme.onBackground.copy(0.5f),
              modifier = Modifier
                .padding(12.dp, 15.dp, 22.dp, 15.dp)
                .clickable {
                  focusManager.clearFocus()
                  isShowLoading.isRefreshing = true
                  thread {
                    searchBooks[0] = NETWORK.getNetworkSearchBooks(search)
                    isShowLoading.isRefreshing = false
                  }
                }
            )
          },
          colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.5f),
            textColor = MaterialTheme.colorScheme.onBackground,
            disabledBorderColor = Color.White.copy(0f),
            focusedBorderColor = Color.White.copy(0f),
            unfocusedBorderColor = Color.White.copy(0f),
          ),
          textStyle = MaterialTheme.typography.bodyLarge
        )
        //搜索结果
        SwipeRefresh(
          state = isShowLoading, onRefresh = {
            thread {
              isShowLoading.isRefreshing = true
              searchBooks[0] = NETWORK.getNetworkSearchBooks(search)
              isShowLoading.isRefreshing = false
            }
          },
          modifier = Modifier
            .padding(22.dp, 100.dp, 22.dp, 0.dp)
            .fillMaxSize()
        ) {
          LazyColumn(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            if (!isShowLoading.isRefreshing && searchBooks[0].size != 1) {
              itemsIndexed(searchBooks[0]) { _, msg ->
                BookCard(msg = JSONObject(msg), MaterialTheme.colorScheme.surfaceVariant, onClick = { navController.navigate("detail?book=$msg") }, onLongClick = {})
              }
            }
            item {
              Spacer(modifier = Modifier.height(100.dp))
            }
          }
        }
      }
    }
  }
}