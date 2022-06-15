package mingzhixian.top.novelneo.utils

import mingzhixian.top.novelneo.R
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

class dbTool {
  
  //todo 修改为数据库访问
  //获取数据库最新更新的所有书籍
  fun getUpdateBooks(): ArrayList<JSONObject> {
    val msgs = ArrayList<JSONObject>()
    for (i in 1..3) {
      val msg1 = JSONObject()
      msg1.put("title", "冥界")
      msg1.put("cover", R.drawable.cover)
      msg1.put("additional", "第302章 深海之战，万里奔袭，两肋插刀")
      msgs.add(msg1)
    }
    return msgs
  }
  
  //获取数据库所有暂无更新的书籍
  fun getReadBooks(): ArrayList<JSONObject> {
    val msgs = ArrayList<JSONObject>()
    for (i in 1..3) {
      val msg1 = JSONObject()
      msg1.put("title", "贩罪")
      msg1.put("cover", R.drawable.cover)
      msg1.put("url", "https://www.exiaoshuo.com/xuanhuan/")
      msg1.put("author", "北川")
      msg1.put("sort", "东方玄幻")
      msg1.put("additional", "第145章 请假")
      msgs.add(msg1)
    }
    return msgs
  }
  
  //获取数据库所有已看完的书籍
  fun getHaveReadBooks(): ArrayList<JSONObject> {
    val msgs = ArrayList<JSONObject>()
    for (i in 1..3) {
      val msg1 = JSONObject()
      msg1.put("title", "圣古传奇之穿越后我变秃了，也变强了")
      msg1.put("url", "https://www.exiaoshuo.com/xuanhuan/")
      msg1.put("author", "北川")
      msg1.put("sort", "东方玄幻")
      msg1.put("cover", R.drawable.cover)
      msg1.put("additional", "第1010章 大结局")
      msgs.add(msg1)
    }
    return msgs
  }
  
  //获取数据库本月阅读统计
  fun getStatistics(): JSONObject {
    val info = JSONObject()
    info.put("wordCount", 126437)
    info.put("hourCount", 51)
    val ary = JSONArray()
    for (i in 1..31) {
      val item = JSONObject()
      item.put("wordCount", 4321)
      item.put("hourCount", Random.nextInt(0, 14))
      ary.put(item)
    }
    info.put("heatMap", ary)
    return info
  }
  
  //检查某书是否在书架中
  fun isInBooks(msg:JSONObject):Boolean{
    return false
  }
}