package dao


import dao.NotesDao.NotesRow
import models.Tables

import javax.inject.{Inject, Singleton}
import models.Tables._
import play.api.Logger
import play.api.db.slick._
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import java.sql.Timestamp
import java.time.LocalDateTime
import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal


@Singleton
class NotesSlickDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val logger = Logger(this.getClass)

  // TODO: CRUD処理実装
  def fetch(condition: Option[String]): Future[Seq[(NotesRow, Seq[TagsRow])]] = {
    val queryBase = condition.fold(Notes.to[Seq]) { c => Notes.filter(n => (n.title like s"%$c%") || (n.content like s"%$c%")) }

    // SELECTクエリ
    val action = for {
      // Noteを取得
      notesRows <- queryBase.result
      // Noteに関連付くTagを取得
      tagsRows <- queryBase
        .join(Taggings).on { case (notes, taggings) => notes.id === taggings.noteId }
        .join(Tags).on { case ((_, taggings), tags) => taggings.tagId === tags.id }
        .sortBy { case ((notes, _), _) => notes.id }
        .result
    } yield notesRows.map { notes =>
      // 返却値のTupleを生成
      (notes, tagsRows
        .filter { case ((_, taggings), _) => taggings.noteId == notes.id.getOrElse(-1L) }
        .map { case ((_, _), tags) => tags })
    }

    // クエリ実行
    db.run(action)
      .map(_.map{ case (n, v) =>
        (NotesDao.NotesRow(n.id.getOrElse(0), n.title, n.content), v)
      })
      .recover {
      case NonFatal(e) =>
        logger.error("DB操作で例外が発生しました。", e)
        throw e
    }
  }

  def create(title: String, content: String, tag: Seq[Long]): Future[Unit] = {
    val currentTime = Timestamp.valueOf(LocalDateTime.now)

    // INSERTクエリ
    val action = (for {
      // Noteを作成
      noteId <- Notes returning Notes.map(_.id) += Tables.NotesRow(title, content, currentTime, currentTime)
      // Tagのマスタ存在チェック
      _ <- Tags.filter(_.id inSetBind tag).length.result.map { tags =>
        tags == tag.length match {
          case true => DBIO.successful(())
          case _ => DBIO.failed(new RuntimeException(s"不正なtagIdが含まれています。[tagId = $tag]"))
        }
      }.flatten
      // Noteに関連付くTagを作成
      _ <- DBIO.sequence(tag.map(t => Taggings += TaggingsRow(noteId, t, currentTime, currentTime)))
    } yield ()).transactionally

    // クエリ実行
    db.run(action).recover {
      case NonFatal(e) =>
        logger.error("DB操作で例外が発生しました。", e)
        throw e
    }
  }

  def update(id: Long, title: String, content: String, tag: Seq[Long]): Future[Unit] = {
    val currentTime = Timestamp.valueOf(LocalDateTime.now)

    // UPDATEクエリ
    val action = (for {
      // Noteを更新
      _ <- Notes.filter(_.id === id).map(t => (t.title, t.content, t.updatedAt)).update((title, content, currentTime))
      // Noteに関連付くTagを削除
      _ <- Taggings.filter(_.noteId === id).delete
      // Tagのマスタ存在チェック
      _ <- Tags.filter(_.id inSetBind tag).length.result.map { tags =>
        tags == tag.length match {
          case true => DBIO.successful(())
          case _ => DBIO.failed(new RuntimeException(s"不正なtagIdが含まれています。[tagId = $tag]"))
        }
      }.flatten
      // Noteに関連付くTaggingを作成
      _ <- DBIO.sequence(tag.map(t => Taggings += TaggingsRow(id, t, currentTime, currentTime)))
    } yield ()).transactionally

    // クエリ実行
    db.run(action).recover {
      case NonFatal(e) =>
        logger.error("DB操作で例外が発生しました。", e)
        throw e
    }
  }

  def delete(id: Long): Future[Unit] = {
    // DELETEクエリ
    val action = (for {
      // Noteを削除
      _ <- Notes.filter(_.id === id).delete
      // Noteに関連付くTaggingを削除
      _ <- Taggings.filter(_.noteId === id).delete
    } yield ()).transactionally

    // クエリ実行
    db.run(action).recover {
      case NonFatal(e) =>
        logger.error("DB操作で例外が発生しました。", e)
        throw e
    }
  }

}