package mingzhixian.top.novelneo.ui.set

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import mingzhixian.top.novelneo.R
import mingzhixian.top.novelneo.ui.NovelNeoBar
import mingzhixian.top.novelneo.ui.getAppContext
import mingzhixian.top.novelneo.ui.theme.NovelNeoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetBody(navHostController: NavHostController) {
  NovelNeoTheme {
    Scaffold(
      topBar = { NovelNeoBar(isNeedBack = true, name = "设置", image = 0, onClick = {}, navController = navHostController) }
    ) { innerPadding ->
      Column(
        modifier = Modifier
          .padding(innerPadding)
          .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(
          text = "关于NovelNeo",
          fontSize = 24.sp,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onBackground,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 4.dp)
        )
        Text(
          text = "NovelNeo是一款使用kotlin开发的开源软件，使用了jetpack compose、jetpack room等技术，体积小巧，简洁无广。",
          fontSize = 16.sp,
          color = MaterialTheme.colorScheme.onBackground,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 2.dp)
        )
        Image(
          painter = painterResource(R.drawable.github), contentDescription = "github",
          modifier = Modifier
            .padding(0.dp, 20.dp)
            .size(28.dp)
            .clickable {
              val uri = Uri.parse("https://github.com/mingzhixian/NovelNeo")
              val intent = Intent(Intent.ACTION_VIEW, uri)
              getAppContext().startActivity(intent)
            }
        )
        Text(
          text = "我写了半天，发现没啥可以设置的，所有就没有设置。。。",
          fontSize = 14.sp,
          color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
          textAlign = TextAlign.Center,
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 80.dp, 20.dp, 2.dp)
        )
      }
    }
  }
}