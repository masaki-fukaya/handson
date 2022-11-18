package controllers

import dao.{NotesDao, NotesSlickDao}
import dto.NoteDto
import forms.FormObjects
import play.api.i18n.I18nSupport

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

import scala.concurrent.{ExecutionContext, Future}
import dao.{NotesSlickDao, TagsSlickDao}
import dto.{NoteDto, TagDto}
import views.html

@Singleton
class NotesController @Inject()(notesDao: NotesSlickDao, tagsDao: TagsSlickDao, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport{

  def index = Action.async { implicit request =>
    FormObjects().searchForm.bindFromRequest().fold(
      // 入力エラー
      formWithErrors => {
        Future.successful(Redirect(routes.NotesController.index()).flashing("message" -> "error.invalid"))
      },

      // 正常
      form => {
        for {
          noteRow <- notesDao.fetch(form.q)
          tagsRow <- tagsDao.fetch()
        } yield {
          val notes = noteRow.map { case (n, t) => NoteDto(n.id, n.title, n.content, t.map(t => TagDto(t.id.getOrElse(-1L), t.name))) }
          val tags = tagsRow.map { t => TagDto(t.id.getOrElse(-1L), t.name) }
          val message = request.flash.get("message")
          Ok(html.notes.index(notes, tags, FormObjects().searchForm.fill(form), message))
        }
      }
    )
  }

  def create = Action.async { implicit request =>
    FormObjects().createForm.bindFromRequest().fold(
      // 入力エラー
      formWithErrors => {
        Future.successful(Redirect(routes.NotesController.index()).flashing("message" -> "error.invalid"))
      },

      // 正常
      form => {
        for {
          _ <- notesDao.create(form.title, form.content, form.tag)
        } yield {
          Redirect(routes.NotesController.index())
        }
      }
    )
  }

  def update = Action.async { implicit request =>
    FormObjects().updateForm.bindFromRequest().fold(
      // 入力エラー
      formWithErrors => {
        Future.successful(Redirect(routes.NotesController.index()))
      },

      // 正常
      form => {
        for {
          _ <- notesDao.update(form.id, form.title, form.content, form.tag)
        } yield {
          Redirect(routes.NotesController.index())
        }
      }
    )
  }

  def delete = Action.async { implicit request =>
    FormObjects().deleteForm.bindFromRequest().fold(
      // 入力エラー
      formWithErrors => {
        Future.successful(Redirect(routes.NotesController.index()).flashing("message" -> "error.invalid"))
      },
      // 正常
      form => {
        for {
          _ <- notesDao.delete(form.id)
        } yield {
          Redirect(routes.NotesController.index())
        }
      }
    )
  }


}

