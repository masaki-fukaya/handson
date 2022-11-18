package dao

import javax.inject.{Inject, Singleton}
import models.Tables
import models.Tables._
import play.api.Logger
import play.api.db.slick._
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

@Singleton
class TagsSlickDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  private val logger = Logger(this.getClass)

  def fetch(): Future[Seq[TagsRow]] = {
    db.run(Tables.Tags.result).recover {
      case NonFatal(e) =>
        logger.error("DB操作で例外が発生しました。", e)
        throw e
    }
  }
}
