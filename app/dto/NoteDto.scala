package dto

case class NoteDto(id: Long, title: String, content: String, tag: Seq[TagDto])


