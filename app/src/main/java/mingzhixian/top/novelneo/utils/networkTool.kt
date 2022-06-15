package mingzhixian.top.novelneo.utils

import mingzhixian.top.novelneo.R
import org.json.JSONArray
import org.json.JSONObject

class networkTool {
  //联网获取书架书籍更新
  fun getBooksUpdate(): ArrayList<JSONObject> {
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
  
  //联网获取小说详细
  fun getDetails(msg: JSONObject): JSONObject {
    val msgs = JSONObject()
    msgs.put(
      "content", "一个在家族中地位不高的玄气弟子，偶然在地摊上得到一块奇异玉石，里面藏著一门上古剑修传下来的绝世剑阵修炼之法！ 冰火两仪剑阵，三叠琴音剑阵，四合八级剑阵，六脉五行剑阵，七星八卦剑阵，九天雷火剑阵，十方无极剑阵，周天挪移剑阵，紫雾虚弥剑阵，道心种魔剑阵，万剑归宗剑阵…… 天下地下，唯我剑阵！ 与我作对者，一概万剑轰杀！\n" +
              "\n" +
              "各位书友要是觉得《无尽剑装》还不错的话请不要忘记向您QQ群和微博里的朋友推荐哦！"
    )
    val msgs1 =JSONArray()
    for (i in 1..110) {
      val msg2 = JSONObject()
      msg2.put("title", "第110章 又穿越了？")
      msg2.put("url", "https://www.exiaoshuo.com/xuanhuan/")
      msgs1.put(msg2)
    }
    msgs.put("menu", msgs1)
    return msgs
  }
  
  //联网获取小说内容
  fun getBody(msg: JSONObject) {
  
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
      msg1.put("cover", R.drawable.cover)
      msg1.put("url", "https://www.exiaoshuo.com/xuanhuan/")
      msg1.put("author", "北川")
      msg1.put("sort", "东方玄幻")
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
      msg1.put("url", "https://www.exiaoshuo.com/xuanhuan/")
      msg1.put("author", "北川")
      msg1.put("sort", "东方玄幻")
      msg1.put("additional", "第1010章 大结局")
      msgs.add(msg1)
    }
    return msgs
  }
}