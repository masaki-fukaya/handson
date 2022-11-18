package forms

import forms.FormObjects.{Create, Delete, Search, Update}
import play.api.data.Forms._
import play.api.data._

object FormObjects {
  case class Search(q: Option[String])
  case class Create(title: String, content: String, tag: Seq[Long])
  case class Update(id: Long, title: String, content: String, tag: Seq[Long])
  case class Delete(id: Long)

  def apply() = new FormObjects()
}

class FormObjects {
  val searchForm: Form[Search] = {
    Form(
      mapping(
        "q" -> optional(text)
      )(Search.apply)(Search.unapply))
  }

  val createForm: Form[Create] = {
    Form(
      mapping(
        "title" -> nonEmptyText,
        "content" -> text,
        "tag" -> seq(longNumber)
      )(Create.apply)(Create.unapply))
  }

  val updateForm: Form[Update] = {
    Form(
      mapping(
        "id" -> longNumber,
        "title" -> nonEmptyText,
        "content" -> text,
        "tag" -> seq(longNumber)
      )(Update.apply)(Update.unapply))
  }

  val deleteForm: Form[Delete] = {
    Form(
      mapping(
        "id" -> longNumber
      )(Delete.apply)(Delete.unapply))
  }

}
