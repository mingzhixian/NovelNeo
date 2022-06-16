package mingzhixian.top.novelneo.utils

import android.content.Context
import androidx.room.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

//书架
@Entity(tableName = "books")
class Book(
  //书名
  @PrimaryKey var title: String,
  //作者
  @ColumnInfo var author: String,
  //封面本地地址或网络地址
  @ColumnInfo var cover: String,
  //分类
  @ColumnInfo var sort: String,
  //简介
  @ColumnInfo var content: String,
  //目录
  @ColumnInfo var menu: JSONArray,
  //书的网络地址
  @ColumnInfo var url: String,
  //当前阅读章
  @ColumnInfo var current: Int,
  //当前阅读章内页
  @ColumnInfo var currentPage: Int,
  //状态（1为今日更新，2为未看完但今日未更新的，3为已看完最新章的）
  @ColumnInfo var status: Int
)

//阅读统计
@Entity(tableName = "count")
class Count(
  //天
  @PrimaryKey var day: Int,
  //当天字数统计
  @ColumnInfo var wordCount: Int,
  //当天阅读时长统计
  @ColumnInfo var hourCount: Int,
)

@Dao
interface BooksDao {
  
  //获取书架所有书
  @Query("select * from books where status=:status")
  fun getBooks(status: Int): List<Book>
  
  //书架新加书
  @Insert
  suspend fun insertBook(book: Book)
  
  //书架移除书
  @Query("DELETE FROM books where title=:bookTitle")
  suspend fun deleteBook(bookTitle: String)
}

@Dao
interface CountDao {
  //获取统计所有记录
  @Query("select * from count")
  fun getCounts(): List<Count>
  
  //清除旧的数据
  @Query("delete from count")
  suspend fun deleteOld()
  
  //新的一天
  @Insert
  suspend fun newDay(day: Count)
  
  //更新今天的数据
  @Query("update count set wordCount=:word,hourCount=:hour where day=:day")
  suspend fun update(day: Int, word: Int, hour: Int)
}

@Database(entities = [Book::class, Count::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
  
  abstract fun booksDao(): BooksDao
  abstract fun countDao(): CountDao
  
  companion object {
    private var instance: AppDatabase? = null
    fun getInstance(context: Context): AppDatabase {
      if (instance == null) {
        instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "novelNeo.db"
        ).allowMainThreadQueries().build()
      }
      return instance as AppDatabase
    }
  }
}

class DbTool(context: Context) {
  //获取数据库
  private val booksDao = AppDatabase.getInstance(context = context).booksDao()
  private val countDao = AppDatabase.getInstance(context = context).countDao()
  
  //获取数据库最新更新的所有书籍
  fun getUpdateBooks(): ArrayList<JSONObject> {
    return getBooks(1)
  }
  
  //获取数据库所有暂无更新的书籍
  fun getReadBooks(): ArrayList<JSONObject> {
    return getBooks(2)
  }
  
  //获取数据库所有已看完的书籍
  fun getHaveReadBooks(): ArrayList<JSONObject> {
    return getBooks(3)
  }
  
  //获取数据库书本信息
  private fun getBooks(status: Int): ArrayList<JSONObject> {
    val books = booksDao.getBooks(status = status)
    val msgs = ArrayList<JSONObject>()
    books.forEach { item ->
      val msg1 = JSONObject()
      msg1.put("title", item.title)
      msg1.put("cover", item.cover)
      msg1.put("url", item.url)
      msg1.put("author", item.author)
      msg1.put("sort", item.sort)
      msg1.put("additional", item.menu.getJSONObject(item.menu.length() - 1).getString("title"))
      msg1.put("status", item.status)
      msg1.put("content", item.content)
      msg1.put("current", item.current)
      msg1.put("currentPage", item.currentPage)
      msgs.add(msg1)
    }
    return msgs
  }
  
  //新加一本书到书架
  suspend fun newBook(book: Book) {
    booksDao.insertBook(book)
  }
  
  //书架移除一本书
  suspend fun deleteBook(msg: JSONObject) {
    booksDao.deleteBook(msg.getString("title"))
  }
  
  //获取数据库本月阅读统计
  fun getStatistics(): JSONObject {
    val count = countDao.getCounts()
    //新建json对象
    var wordCount = 0
    var hourCount = 0
    val info = JSONObject()
    val ary = JSONArray()
    //添加数据
    for (i in count.indices) {
      val item = JSONObject()
      wordCount += count[i].wordCount
      hourCount += count[i].hourCount
      item.put("wordCount", count[i].wordCount)
      item.put("hourCount", count[i].hourCount)
      ary.put(item)
    }
    info.put("wordCount", wordCount)
    info.put("hourCount", hourCount)
    info.put("heatMap", ary)
    return info
  }
  
  //更新当天的阅读统计
  suspend fun updateCount(word: Int, hour: Int) {
    countDao.update(Calendar.getInstance().get(Calendar.DAY_OF_MONTH), word = word, hour = hour)
  }
  
  //数据库统计清空重建（下一个月）
  suspend fun reSetCount() {
    //删除旧数据
    countDao.deleteOld()
    //重建新月
    val month = if (Calendar.getInstance().get(Calendar.MONTH) in listOf(2, 4, 6, 9, 11)) 30 else 31
    for (i in 1..month) {
      countDao.newDay(Count(i, 0, 0))
    }
  }
  
  //检查某书是否在书架中
  fun isInBooks(msg: JSONObject): Boolean {
    val title = msg.getString("title")
    booksDao.getBooks(1).forEach { item ->
      if (title == item.title) return true
    }
    booksDao.getBooks(2).forEach { item ->
      if (title == item.title) return true
    }
    booksDao.getBooks(3).forEach { item ->
      if (title == item.title) return true
    }
    return false
  }
}