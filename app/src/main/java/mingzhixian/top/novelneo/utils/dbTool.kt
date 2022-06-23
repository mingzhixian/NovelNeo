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
  //书的网络地址
  @ColumnInfo var url: String,
  //当前阅读章
  @ColumnInfo var current: Int,
  //当前阅读章内页
  @ColumnInfo var currentPage: Int,
  //状态（1为今日更新，2为未看完但今日未更新的，3为已看完最新章的,4为未在书架的）
  @ColumnInfo var status: Int,
  //最新章节名
  @ColumnInfo var latest: String,
  //目录
  @ColumnInfo var menu: String,
)

@Dao
interface BooksDao {
  //获取书架所有书
  @Query("select * from books where status=:status")
  fun getBooks(status: Int): List<Book>
  
  //书架新加书
  @Insert
  fun insertBook(book: Book)
  
  //书架移除书
  @Query("delete from books where title=:bookTitle")
  fun deleteBook(bookTitle: String)
  
  //更新阅读进度
  @Query("update books set current=:current,currentPage=:currentPage where title=:bookTitle")
  fun updateBook(bookTitle: String, current: Int, currentPage: Int)
  
  //更新书本状态
  @Query("update books set latest=:latest,status=:status where title=:bookTitle")
  fun updateBookStatus(bookTitle: String,latest: String,status: Int)
}

//阅读统计
@Entity(tableName = "count")
class Count(
  //月
  @ColumnInfo var month: Int,
  //天
  @PrimaryKey var day: Int,
  //当天字数统计
  @ColumnInfo var wordCount: Int,
  //当天阅读时长统计
  @ColumnInfo var hourCount: Double,
)

@Dao
interface CountDao {
  //获取统计所有记录
  @Query("select * from count")
  fun getCounts(): List<Count>
  
  //清除旧的数据
  @Query("delete from count")
  fun deleteOld()
  
  //新的一天
  @Insert
  fun newDay(day: Count)
  
  //更新今天的数据
  @Query("update count set wordCount=:word,hourCount=:hour where day=:day")
  fun update(day: Int, word: Int, hour: Double)
}

@Database(entities = [Book::class, Count::class], version = 2)
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
          "novel.db" //数据库名称
        ).allowMainThreadQueries().build()
      }
      return instance as AppDatabase
    }
  }
}

class DbTool(context: Context) {
  //获取数据库
  private val instance = AppDatabase.getInstance(context)
  private val booksDao = instance.booksDao()
  private val countDao = instance.countDao()
  
  //获取数据库最新更新的所有书籍
  fun getUpdateBooks(): ArrayList<String> {
    return getBooks(1)
  }
  
  //获取数据库所有暂无更新的书籍
  fun getReadBooks(): ArrayList<String> {
    return getBooks(2)
  }
  
  //获取数据库所有已看完的书籍
  fun getHaveReadBooks(): ArrayList<String> {
    return getBooks(3)
  }
  
  //获取数据库书本信息
  private fun getBooks(status: Int): ArrayList<String> {
    val books = booksDao.getBooks(status = status)
    val msgs = ArrayList<String>()
    books.forEach { item ->
      val msg1 = JSONObject()
      msg1.put("title", item.title)
      msg1.put("author", item.author)
      msg1.put("cover", item.cover)
      msg1.put("sort", item.sort)
      msg1.put("content", item.content)
      msg1.put("url", item.url)
      msg1.put("current", item.current)
      msg1.put("currentPage", item.currentPage)
      msg1.put("status", item.status)
      msg1.put("latest", item.latest)
      msg1.put("menu", item.menu)
      msgs.add(msg1.toString())
    }
    return msgs
  }
  
  //新加一本书到书架
  fun newBook(msg: JSONObject) {
    booksDao.insertBook(Book(msg.getString("title"), msg.getString("author"), msg.getString("cover"), msg.getString("sort"), msg.getString("content"), msg.getString("url"), msg.getInt("current"), msg.getInt("currentPage"), 2, msg.getString("latest"), msg.getJSONArray("menu").toString()))
  }
  
  //书架移除一本书
  fun deleteBook(msg: JSONObject) {
    booksDao.deleteBook(msg.getString("title"))
  }
  
  //更新一本书的阅读进度
  fun updateCurrent(msg: JSONObject) {
    booksDao.updateBook(msg.getString("title"), msg.getInt("current"), msg.getInt("currentPage"))
  }
  
  //更新一本书的最新章节，书本状态
  fun updateBook(msg: JSONObject){
    booksDao.updateBookStatus(msg.getString("title"), msg.getString("latest"), msg.getInt("status"))
  }
  
  //获取数据库本月阅读统计
  fun getStatistics(): String {
    val count = countDao.getCounts()
    //新建json对象
    var wordCount = 0
    var hourCount = 0.0
    var month = 0
    val info = JSONObject()
    val ary = JSONArray()
    //添加数据
    for (i in count.indices) {
      val item = JSONObject()
      month = count[i].month
      wordCount += count[i].wordCount
      hourCount += count[i].hourCount
      item.put("wordCount", count[i].wordCount)
      item.put("hourCount", count[i].hourCount)
      ary.put(item)
    }
    info.put("month", month)
    info.put("wordCount", wordCount)
    info.put("hourCount", hourCount)
    info.put("heatMap", ary)
    return info.toString()
  }
  
  //更新当天的阅读统计
  fun updateCount(word: Int, hour: Double) {
    val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    countDao.update(day, word = word, hour = countDao.getCounts()[day - 1].hourCount + hour)
  }
  
  //数据库统计清空重建（下一个月）
  fun reSetCount() {
    //删除旧数据
    countDao.deleteOld()
    //重建新月
    val month = Calendar.getInstance().get(Calendar.MONTH) + 1
    val days = if (month in listOf(2, 4, 6, 9, 11)) 30 else 31
    for (i in 1..days) {
      countDao.newDay(Count(month, i, 0, 0.0))
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