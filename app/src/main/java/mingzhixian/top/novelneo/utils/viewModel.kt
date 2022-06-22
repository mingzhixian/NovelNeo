package mingzhixian.top.novelneo.utils

import org.json.JSONObject

//懒得用viewModel,自己实现的全局变量管理
class Data {
  private var book = JSONObject()
  fun setDataBook(b: JSONObject) {
    book = b
  }
  
  fun getDataBook(): JSONObject {
    return book
  }
}