package mingzhixian.top.novelneo.utils

import mingzhixian.top.novelneo.R
import org.json.JSONObject

class networkTool {
  //联网获取书架书籍更新
  fun getBooksUpdate(): ArrayList<JSONObject> {
    val msgs = ArrayList<JSONObject>()
    for (i in 1..3) {
      val msg1 = JSONObject()
      msg1.put("title", "圣古传奇之穿越后我变秃了，也变强了")
      msg1.put("cover", R.drawable.cover)
      msg1.put("additional", "第1010章 大结局")
      msgs.add(msg1)
    }
    return msgs
  }
  
  //联网获取小说内容
  fun getBody() {
  
  }
  
  //联网获取所有分类
  fun getAllSorts(): ArrayList<JSONObject> {
    val msgs = ArrayList<JSONObject>()
    for (i in 1..3) {
      val msg1 = JSONObject()
      msg1.put("title", "圣古传奇之穿越后我变秃了，也变强了")
      msg1.put("cover", R.drawable.cover)
      msg1.put("additional", "第1010章 大结局")
      msgs.add(msg1)
    }
    return msgs
  }
  
  //联网获取分类下书籍排行榜
  fun getSortBooks(): ArrayList<JSONObject> {
    val msgs = ArrayList<JSONObject>()
    for (i in 1..3) {
      val msg1 = JSONObject()
      msg1.put("title", "圣古传奇之穿越后我变秃了，也变强了")
      msg1.put("cover", R.drawable.cover)
      msg1.put("additional", "第1010章 大结局")
      msgs.add(msg1)
    }
    return msgs
  }
  
  //联网获取强力推荐榜（必读榜）
  fun getMustReadBooks(): ArrayList<JSONObject> {
    val msgs = ArrayList<JSONObject>()
    for (i in 1..20) {
      val msg1 = JSONObject()
      msg1.put("title", "剑门第一仙")
      //只有前三需要获取封面，其余的都是默认的封面或者直接填空值
      msg1.put("cover", R.drawable.cover)
      msg1.put("author", "北川")
      msg1.put("sort", "东方玄幻")
      msgs.add(msg1)
    }
    return msgs
  }
}