package mingzhixian.top.novelneo.utils

import mingzhixian.top.novelneo.R
import org.json.JSONArray
import org.json.JSONObject

class NetworkTool {
  //联网获取书架书籍更新
  fun getBooksUpdate(): ArrayList<JSONObject> {
    //todo 对数据库操作
    val msgs = ArrayList<JSONObject>()
    for (i in 1..3) {
      val msg1 = JSONObject()
      msg1.put("title", "圣古传奇之穿越后我变秃了，也变强了")
      msg1.put("cover", R.drawable.cover)
      msg1.put("url", "https://www.exiaoshuo.com/xuanhuan/")
      msg1.put("author", "北川")
      msg1.put("sort", "东方玄幻")
      msg1.put("additional", "第1010章 大结局")
      msgs.add(msg1)
    }
    return msgs
  }
  
  //联网获取小说目录，传入Book格式json,返回目录列表
  fun getMenu(msg: JSONObject): JSONArray {
    val msg1 = JSONArray()
    for (i in 1..110) {
      val msg2 = JSONObject()
      msg2.put("title", "第110章 又穿越了？")
      msg2.put("url", "https://www.exiaoshuo.com/xuanhuan/")
      msg1.put(msg2)
    }
    return msg1
  }
  
  //联网获取小说内容,传入目录列表以及获取章节
  fun getBody(msg: JSONArray, index: Int): String {
    return "sasadafdsd"
  }
  
  //联网获取所有分类
  fun getAllSorts(): ArrayList<JSONObject> {
    val msgs = ArrayList<JSONObject>()
    for (i in 1..8) {
      val msg1 = JSONObject()
      msg1.put("name", "玄幻小说")
      msg1.put("url", "https://www.exiaoshuo.com/xuanhuan/")
      msgs.add(msg1)
    }
    return msgs
  }
  
  //联网获取分类下书籍排行榜
  fun getSortBooks(sort: JSONObject): ArrayList<JSONObject> {
    val msgs = ArrayList<JSONObject>()
    for (i in 1..20) {
      val msg1 = JSONObject()
      msg1.put("title", "圣古传奇之穿越后我变秃了，也变强了")
      msg1.put("author", "北川")
      msg1.put("cover", "https://bookcover.yuewen.com/qdbimg/349573/1032575913/90")
      msg1.put("sort", "东方玄幻")
      msg1.put("content", "asadasdasda")
      msg1.put("url", "https://www.exiaoshuo.com/xuanhuan/")
      msg1.put("current", 0)
      msg1.put("currentPage", 0)
      msg1.put("latest","大结局")
      msg1.put("status", 4)
      msgs.add(msg1)
    }
    return msgs
  }
  
  //联网获取强力推荐榜（必读榜）
  fun getMustReadBooks(): ArrayList<JSONObject> {
    val msgs = ArrayList<JSONObject>()
    for (i in 1..20) {
      val msg1 = JSONObject()
      msg1.put("title", "圣古传奇之穿越后我变秃了，也变强了")
      msg1.put("author", "北川")
      msg1.put("cover", "https://bookcover.yuewen.com/qdbimg/349573/1032575913/90")
      msg1.put("sort", "东方玄幻")
      msg1.put("content", "asadasdasda")
      msg1.put("url", "https://www.exiaoshuo.com/xuanhuan/")
      msg1.put("current", 0)
      msg1.put("currentPage", 0)
      msg1.put("latest","大结局")
      msg1.put("status", 4)
      msgs.add(msg1)
    }
    return msgs
  }
}