package mingzhixian.top.novelneo.utils

import androidx.lifecycle.ViewModel
import org.json.JSONObject

class Data : ViewModel() {
  private var book = JSONObject()
  fun setDataBook(b: JSONObject) {
    book = b
  }
  
  fun getDataBook(): JSONObject {
    return book
  }
}