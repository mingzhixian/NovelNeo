package mingzhixian.top.novelneo.ui.set

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import mingzhixian.top.novelneo.ui.NovelNeoBar
import mingzhixian.top.novelneo.ui.theme.NovelNeoTheme

@Composable
@Preview
fun Pre1() {
  SetBody(rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetBody(navHostController: NavHostController) {
  NovelNeoTheme {
    Scaffold(
      topBar = { NovelNeoBar(isNeedBack = true, name = "设置", image = 0, onClick = {}, navController = navHostController) }
    ) { innerPadding ->
      LazyColumn(
        modifier = Modifier
          .padding(innerPadding)
      ) {}
    }
  }
}