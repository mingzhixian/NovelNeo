package mingzhixian.top.novelneo.utils

import android.content.ContentValues.TAG
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.concurrent.TimeUnit

class NetworkTool {
  private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(50L, TimeUnit.SECONDS)
    .readTimeout(60L, TimeUnit.SECONDS)
    .build()
  val requestBuilder = Request.Builder()
  
  //联网获取书架书籍更新
  fun getBooksUpdate() {
    //todo 对数据库操作
  }
  
  //联网获取小说详情
  fun getDetail(m: JSONObject): JSONObject {
    val url = m.getString("url")
    val body = getDoc(url)?.select("body")?.get(0)
    val element = body!!.select("div[id=maininfo]")
    val msg = JSONObject()
    msg.put("title", m.getString("title"))
    msg.put("author", element.select("div[id=info] > p")[2].text())
    msg.put("cover", "https://www.exiaoshuo.com" + element.select("div[id=fmimg] > img").attr("src"))
    msg.put("sort", element.select("div[id=info] > p")[0].text())
    msg.put("content", element.select("div[id=intro] > p").text())
    msg.put("url", m.getString("url"))
    msg.put("current", m.getString("current"))
    msg.put("currentPage", m.getString("currentPage"))
    msg.put("status", m.getString("status"))
    
    val msg1 = JSONArray()
    val menu = body.select("div[class=listmain] > dl > dd")
    for (i in menu.indices) {
      val element1 = menu[i]
      val msg2 = JSONObject()
      msg2.put("title", element1.select("a").text())
      msg2.put("url", "https://www.exiaoshuo.com" + element1.select("a").attr("href"))
      msg1.put(msg2)
    }
    msg.put("menu", msg1)
    msg.put("latest", msg1.getJSONObject(msg1.length() - 1).getString("title"))
    return msg
  }
  
  //联网获取小说内容,传入目录列表以及获取章节
  fun getBody(msg: JSONArray, index: Int): String {
    return "sasadafdsd"
  }
  
//  //联网获取所有分类
//  @SuppressLint("RestrictedApi")
//  fun getAllSorts(): ArrayList<JSONObject> {
//    val url = "https://www.exiaoshuo.com"
//    val lis: Elements? = getDoc(url)?.select("div[class=nav] > ul > li")
//    val msgs = ArrayList<JSONObject>()
//    if (lis != null) {
//      for (index in 1..lis.size - 2) {
//        val element = lis[index]
//        val msg1 = JSONObject()
//        msg1.put("name", element.select("a").text())
//        msg1.put("url", url + element.select("a").attr("href"))
//        msgs.add(msg1)
//      }
//    }
//    return msgs
//  }
  
  //联网获取分类下书籍排行榜
  fun getSortBooks(sort: JSONObject): ArrayList<String> {
    val url = sort.getString("url")
    val lis: Elements? = getDoc(url)?.select("div[class=l bd] > ul > li")
    val msgs = ArrayList<String>()
    if (lis != null) {
      for (index in lis.indices) {
        val element = lis[index]
        val msg1 = JSONObject()
        msg1.put("title", element.select("span[class=s2] > a").text())
        msg1.put("author", element.select("span[class=s4]").text())
        msg1.put("cover", "")
        msg1.put("sort", element.select("span[class=s3]").text())
        msg1.put("content", "简介")
        msg1.put("url", "https://www.exiaoshuo.com" + element.select("span[class=s2] > a").attr("href"))
        msg1.put("current", 0)
        msg1.put("currentPage", 0)
        msg1.put("latest", element.select("span[class=s3] > a").text())
        msg1.put("status", 4)
        msgs.add(msg1.toString())
      }
    }
    return msgs
  }
  
  //联网获取强力推荐榜（必读榜）
  fun getMustReadBooks(): ArrayList<String> {
    val url = "https://www.exiaoshuo.com"
    val lis: Elements? = getDoc(url)?.select("div[class=r bd] > ul > div[class=list_col2] > ol")
    val l1 = lis?.get(0)?.select("li")
    val l2 = lis?.get(1)?.select("li")
    val msgs = ArrayList<String>()
    if (l1 != null) {
      for (index in l1.indices) {
        val element = l1[index]
        val msg1 = JSONObject()
        msg1.put("title", element.select("a").text())
        msg1.put("author", "北川")
        msg1.put("cover", "")
        msg1.put("sort", "东方玄幻")
        msg1.put("content", "简介")
        msg1.put("url", "https://www.exiaoshuo.com" + element.select("a").attr("href"))
        msg1.put("current", 0)
        msg1.put("currentPage", 0)
        msg1.put("latest", "")
        msg1.put("status", 4)
        msgs.add(msg1.toString())
      }
    }
    if (l2 != null) {
      for (index in l2.indices) {
        val element = l2[index]
        val msg1 = JSONObject()
        msg1.put("title", element.select("a").text())
        msg1.put("author", "北川")
        msg1.put("cover", "")
        msg1.put("sort", "东方玄幻")
        msg1.put("content", "简介")
        msg1.put("url", "https://www.exiaoshuo.com" + element.select("a").attr("href"))
        msg1.put("current", 0)
        msg1.put("currentPage", 0)
        msg1.put("latest", "")
        msg1.put("status", 4)
        msgs.add(msg1.toString())
      }
    }
    return msgs
  }
  
  //网络请求
  private fun getDoc(url: String): Document? {
    val request: Request = requestBuilder.url(url = url).build()
    var str=""
    for (i in 1..3){
      try {
        val response = okHttpClient.newCall(request).execute()
        str = response.body!!.string()
        response.body!!.close()
        break
      }catch (t: Throwable) {
        Log.e(TAG, "getDoc: when requesting $url", t)
      }
    }
    //把字符串内容转换成Document节点内容
    return Jsoup.parse(str)
  }
}