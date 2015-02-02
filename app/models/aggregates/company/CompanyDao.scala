package models.aggregates.company

import scalikejdbc._
import org.joda.time.DateTime
import models.aggregates.CompanyId

class CompanyDao {

  import CompanyDao._

  private val c = CompanyTable.syntax("c")
  private val cc = CompanyTable.column

  def findById(id: CompanyId)(implicit session: DBSession): Option[Company] = {
    withSQL {
      select.from(CompanyTable as c).where.eq(c.id, id.value).and.isNull(c.deletedAt)
    }.map(CompanyTable(c)).single.apply()
  }

  def findByIds(ids: Traversable[CompanyId])(implicit session: DBSession): Seq[Company] = {
    withSQL {
      select.from(CompanyTable as c).where.in(c.id, ids.map(_.value).toSeq).and.isNull(c.deletedAt)
    }.map(CompanyTable(c)).list.apply()
  }

  def findAll(implicit session: DBSession): Seq[Company] = {
    withSQL {
      select.from(CompanyTable as c).where.isNull(c.deletedAt)
    }.map(CompanyTable(c)).list.apply()
  }

  def insert(name: String, url: Option[String], now: DateTime = DateTime.now)(implicit session: DBSession): Company = {
    val version = 1
    val id = withSQL {
      QueryDSL.insert.into(CompanyTable).namedValues(
        cc.name      -> name,
        cc.url       -> url,
        cc.createdAt -> now,
        cc.updatedAt -> now,
        cc.version   -> version)
    }.updateAndReturnGeneratedKey.apply()
    Company(id = CompanyId(id), name = name, url = url, createdAt = now, updatedAt = now, version = version)
  }

  def update(company: Company, now: DateTime = DateTime.now)(implicit session: DBSession): Option[Company] = {
    val version = company.version + 1
    val succeeded = withSQL {
      QueryDSL.update(CompanyTable).set(
        cc.name      -> company.name,
        cc.url       -> company.url,
        cc.updatedAt -> now,
        cc.version   -> version
      ).where.eq(cc.id, company.id.value).and.eq(cc.version, company.version)
    }.update.apply() > 0
    if (succeeded) Some(company.copy(updatedAt = now, version = version))
    else None
  }

  def delete(id: CompanyId, now: DateTime = DateTime.now)(implicit session: DBSession): Boolean = {
    withSQL {
      QueryDSL.update(CompanyTable).set(cc.deletedAt -> now).where.eq(cc.id, id.value)
    }.update.apply() > 0
  }

}
object CompanyDao {

  private[company] object CompanyTable extends SQLSyntaxSupport[Company] {

    override val tableName = "company"

    def apply(c: SyntaxProvider[Company])(rs: WrappedResultSet): Company = autoConstruct(rs, c)

  }

}
