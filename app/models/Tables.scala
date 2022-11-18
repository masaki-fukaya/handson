package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends Tables {
  val profile = slick.jdbc.MySQLProfile
}

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Notes.schema ++ Taggings.schema ++ Tags.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Notes
   *  @param title Database column title SqlType(VARCHAR), Length(255,true)
   *  @param content Database column content SqlType(VARCHAR), Length(255,true)
   *  @param createdAt Database column created_at SqlType(DATETIME)
   *  @param updatedAt Database column updated_at SqlType(DATETIME)
   *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  case class NotesRow(title: String, content: String, createdAt: java.sql.Timestamp, updatedAt: java.sql.Timestamp, id: Option[Long] = None)
  /** GetResult implicit for fetching NotesRow objects using plain SQL queries */
  implicit def GetResultNotesRow(implicit e0: GR[String], e1: GR[java.sql.Timestamp], e2: GR[Option[Long]]): GR[NotesRow] = GR{
    prs => import prs._
    val r = (<<?[Long], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp])
    import r._
    NotesRow.tupled((_2, _3, _4, _5, _1)) // putting AutoInc last
  }
  /** Table description of table notes. Objects of this class serve as prototypes for rows in queries. */
  class Notes(_tableTag: Tag) extends profile.api.Table[NotesRow](_tableTag, "notes") {
    def * = (title, content, createdAt, updatedAt, Rep.Some(id)).<>(NotesRow.tupled, NotesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(title), Rep.Some(content), Rep.Some(createdAt), Rep.Some(updatedAt), Rep.Some(id))).shaped.<>({r=>import r._; _1.map(_=> NotesRow.tupled((_1.get, _2.get, _3.get, _4.get, _5)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column title SqlType(VARCHAR), Length(255,true) */
    val title: Rep[String] = column[String]("title", O.Length(255,varying=true))
    /** Database column content SqlType(VARCHAR), Length(255,true) */
    val content: Rep[String] = column[String]("content", O.Length(255,varying=true))
    /** Database column created_at SqlType(DATETIME) */
    val createdAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")
    /** Database column updated_at SqlType(DATETIME) */
    val updatedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updated_at")
    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
  }
  /** Collection-like TableQuery object for table Notes */
  lazy val Notes = new TableQuery(tag => new Notes(tag))

  /** Entity class storing rows of table Taggings
   *  @param noteId Database column note_id SqlType(BIGINT UNSIGNED)
   *  @param tagId Database column tag_id SqlType(BIGINT UNSIGNED)
   *  @param createdAt Database column created_at SqlType(DATETIME)
   *  @param updatedAt Database column updated_at SqlType(DATETIME)
   *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  case class TaggingsRow(noteId: Long, tagId: Long, createdAt: java.sql.Timestamp, updatedAt: java.sql.Timestamp, id: Option[Long] = None)
  /** GetResult implicit for fetching TaggingsRow objects using plain SQL queries */
  implicit def GetResultTaggingsRow(implicit e0: GR[Long], e1: GR[java.sql.Timestamp], e2: GR[Option[Long]]): GR[TaggingsRow] = GR{
    prs => import prs._
    val r = (<<?[Long], <<[Long], <<[Long], <<[java.sql.Timestamp], <<[java.sql.Timestamp])
    import r._
    TaggingsRow.tupled((_2, _3, _4, _5, _1)) // putting AutoInc last
  }
  /** Table description of table taggings. Objects of this class serve as prototypes for rows in queries. */
  class Taggings(_tableTag: Tag) extends profile.api.Table[TaggingsRow](_tableTag, "taggings") {
    def * = (noteId, tagId, createdAt, updatedAt, Rep.Some(id)).<>(TaggingsRow.tupled, TaggingsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(noteId), Rep.Some(tagId), Rep.Some(createdAt), Rep.Some(updatedAt), Rep.Some(id))).shaped.<>({r=>import r._; _1.map(_=> TaggingsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column note_id SqlType(BIGINT UNSIGNED) */
    val noteId: Rep[Long] = column[Long]("note_id")
    /** Database column tag_id SqlType(BIGINT UNSIGNED) */
    val tagId: Rep[Long] = column[Long]("tag_id")
    /** Database column created_at SqlType(DATETIME) */
    val createdAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")
    /** Database column updated_at SqlType(DATETIME) */
    val updatedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updated_at")
    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
  }
  /** Collection-like TableQuery object for table Taggings */
  lazy val Taggings = new TableQuery(tag => new Taggings(tag))

  /** Entity class storing rows of table Tags
   *  @param name Database column name SqlType(VARCHAR), Length(255,true)
   *  @param createdAt Database column created_at SqlType(DATETIME)
   *  @param updatedAt Database column updated_at SqlType(DATETIME)
   *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
  case class TagsRow(name: String, createdAt: java.sql.Timestamp, updatedAt: java.sql.Timestamp, id: Option[Long] = None)
  /** GetResult implicit for fetching TagsRow objects using plain SQL queries */
  implicit def GetResultTagsRow(implicit e0: GR[String], e1: GR[java.sql.Timestamp], e2: GR[Option[Long]]): GR[TagsRow] = GR{
    prs => import prs._
    val r = (<<?[Long], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp])
    import r._
    TagsRow.tupled((_2, _3, _4, _1)) // putting AutoInc last
  }
  /** Table description of table tags. Objects of this class serve as prototypes for rows in queries. */
  class Tags(_tableTag: Tag) extends profile.api.Table[TagsRow](_tableTag, "tags") {
    def * = (name, createdAt, updatedAt, Rep.Some(id)).<>(TagsRow.tupled, TagsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(name), Rep.Some(createdAt), Rep.Some(updatedAt), Rep.Some(id))).shaped.<>({r=>import r._; _1.map(_=> TagsRow.tupled((_1.get, _2.get, _3.get, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column name SqlType(VARCHAR), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column created_at SqlType(DATETIME) */
    val createdAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")
    /** Database column updated_at SqlType(DATETIME) */
    val updatedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updated_at")
    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
  }
  /** Collection-like TableQuery object for table Tags */
  lazy val Tags = new TableQuery(tag => new Tags(tag))
}
