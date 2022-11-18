package generator

import slick.jdbc.JdbcProfile
import slick.jdbc.meta.MTable
import slick.model.Model

import java.net.URI
import java.net.URI
import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration
import javax.swing.table.TableModel



object SlickModelGenerator  {

  // tableNamesをNoneにすると全テーブル出力
  def run(slickDriver: String, jdbcDriver: String, url: String, user: String, password: String,
          tableNames: Option[Seq[String]], outputDir: String = "app", pkg: String = "models", topTraitName: String = "Tables", scalaFileName: String = "Tables.scala") = {
    val driver: JdbcProfile = slick.jdbc.MySQLProfile
    val db = slick.jdbc.MySQLProfile.api.Database.forURL(url, driver=jdbcDriver , user=user , password= password )
    try {

      import scala.concurrent.ExecutionContext.Implicits.global
      val mTablesAction = MTable.getTables.map{_.map{mTable=> mTable.copy(name=mTable.name.copy(catalog=None))}}
//      val mTablesF =db.run(mTablesAction)
//      val mTables = Await.result(mTablesF,Duration.Inf)

//      mTables.foreach(println)
//      mTables.foreach(x => println(x.name.catalog))

      val allModel = Await.result(db.run(driver.createModel(Some(mTablesAction), false)(ExecutionContext.global).withPinnedSession), Duration.Inf)

      val modelFiltered =tableNames.fold (allModel){ tableNames=>
        Model(tables = allModel.tables.filter { aTable =>
          tableNames.contains(aTable.name.table)
        })//.map { table => table.copy(name=table.name.copy(schema=None))})
      }
      //println(modelFiltered)

      //modelFiltered.tables.foreach(println)


      new SourceCodeGeneratorEx(modelFiltered).writeToFile(slickDriver, outputDir, pkg, topTraitName, scalaFileName)
    } finally db.close
  }

  def main(args: Array[String]): Unit = {
    // 各スキーマの設定でfunction作ってここに入れる
    exportCommonSchema
  }

  // commonスキーマ出力
  def exportCommonSchema = {
    val slickDriver = "slick.jdbc.MySQLProfile"
    val jdbcDriver = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/handson?useSSL=false"
    val user = "root"
    val password = "password"
    val outputDir = "app"
    val pkg = "models"
    val topTraitName = "Tables"
    val scalaFileName = "Tables.scala"

    // 対象テーブル
    val tableNames: Option[Seq[String]] = Some(
      Seq("notes", "taggings", "tags")
    )

    run(slickDriver, jdbcDriver, url, user, password, tableNames,
        outputDir, pkg, topTraitName, scalaFileName)
  }
}