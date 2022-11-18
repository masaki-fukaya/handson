package dao

import javax.inject.{Inject, Singleton}
import play.api.db.Database
import dao.NotesDao.NotesRow
import play.api.Logger

import java.sql.Connection
import scala.util.control.NonFatal

object NotesDao {
  case class NotesRow(id: Long, title: String, content: String)
}

@Singleton
class NotesDao @Inject()(db:Database) {

  def fetchAll(): Seq[NotesRow] = {
    val conn = db.getConnection()

    try {
      // クエリ実行
      val rs = conn.createStatement.executeQuery("SELECT id, title, content FROM notes")

      // クエリ結果を case class に設定
      new Iterator[NotesRow] {
        def hasNext: Boolean = rs.next()
        def next(): NotesRow = {
          NotesRow(rs.getLong("id"), rs.getString("title"), rs.getString("content"))
        }
      }.toList
    } finally {
      conn.close()
    }
  }

  def fetch(q: String): Seq[NotesRow] = {
    val conn = db.getConnection()

    try {
      // エスケープ処理
      val condition = q.map {
        case '%' => "\\%"
        case '_' => "\\_"
        case char => char
      }.mkString

      // クエリ生成
      val stmt = conn.prepareStatement("SELECT id, title, content FROM notes WHERE title LIKE ? OR content LIKE ?")
      stmt.setString(1, s"%$condition%")
      stmt.setString(2, s"%$condition%")

      // クエリ実行
      val rs = stmt.executeQuery()

      // クエリ結果を case class に設定
      new Iterator[NotesRow] {
        def hasNext: Boolean = rs.next()

        def next(): NotesRow = {
          NotesRow(rs.getLong("id"), rs.getString("title"), rs.getString("content"))
        }
      }.toList
    } finally {
      conn.close()
    }
  }

  def create(title: String, content: String): Int = {
    operateConnection { conn =>
      val stmt = conn.prepareStatement("INSERT INTO notes (title, content,created_at, updated_at) VALUES (?, ?,20221108,20221108)")
      stmt.setString(1, title)
      stmt.setString(2, content)
      stmt.executeUpdate()
    }
  }

  def update(id: Long, title: String, content: String): Int = {
    operateConnection { conn =>
      val stmt = conn.prepareStatement("UPDATE notes SET title = ?, content = ? WHERE id = ?")
      stmt.setString(1, title)
      stmt.setString(2, content)
      stmt.setLong(3, id)
      stmt.executeUpdate()
    }
  }

  def delete(id: Long): Int = {
    operateConnection { conn =>
      val stmt = conn.prepareStatement("DELETE FROM notes WHERE id = ?")
      stmt.setLong(1, id)
      stmt.executeUpdate()
    }
  }

  private def operateConnection[A](op: Connection => A): A = {
    val conn = db.getConnection()
    val logger: Logger = Logger(this.getClass)
    try {
      op(conn)
    } catch {
      case NonFatal(e) =>
        logger.error("DB操作で例外が発生しました。", e)
        throw e
    }
    finally {
      conn.close()
    }
  }
}


