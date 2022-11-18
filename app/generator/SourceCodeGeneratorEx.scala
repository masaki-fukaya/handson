package generator

import slick.codegen.SourceCodeGenerator
import slick.model.Model

class SourceCodeGeneratorEx(model:Model) extends SourceCodeGenerator(model)
{
  def caseClassFinal = false
  override def Table = new Table(_){
    //auto_incrementを識別できるようにする
    //生成されるモデルはOption型になる
    override def autoIncLastAsOption=true
    override def Column=new Column(_)
    {
      override def rawType=model.tpe match
      {
        case "java.sql.Blob"=>
          "Array[Byte]"
        case _=>
          super.rawType
      }
    }
  }
}