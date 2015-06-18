package models.aggregates.programmer

import scalikejdbc._
import models.aggregates.{SkillId, CompanyId, ProgrammerId}
import org.joda.time.DateTime

class ProgrammerDao {

  import ProgrammerDao._
  import ProgrammerSkillDao._

  private val p = ProgrammerTable.syntax("p")
  private val ps = ProgrammerSkillTable.syntax("ps")
  private val pc = ProgrammerTable.column

  def findAll(implicit session: DBSession): Seq[Programmer] = {
    withSQL {
      select
        .from(ProgrammerTable as p)
        .leftJoin(ProgrammerSkillTable as ps).on(ps.programmerId, p.id)
      .where.isNull(p.deletedAt)
        .orderBy(p.id)
    }.one(ProgrammerTable(p))
      .toMany(_.get[Option[SkillId]](ps.resultName.skillId))
      .map { (programmer, skills) => programmer.copy(skillIds = skills) }
      .list.apply()
  }

  def findSubset(limit: Int, offset: Int)(implicit session: DBSession): Seq[Programmer] = {
    withSQL {
      select
        .from(ProgrammerTable as p)
        .leftJoin(ProgrammerSkillTable as ps).on(ps.programmerId, p.id)
      .where.isNull(p.deletedAt)
        .orderBy(p.id)
      .limit(limit)
      .offset(offset)
    }.one(ProgrammerTable(p))
      .toMany(_.get[Option[SkillId]](ps.resultName.skillId))
      .map { (programmer, skills) => programmer.copy(skillIds = skills) }
      .list.apply()
  }

  def findById(id: ProgrammerId)(implicit session: DBSession): Option[Programmer] = {
    withSQL {
      select
        .from(ProgrammerTable as p)
        .leftJoin(ProgrammerSkillTable as ps).on(ps.programmerId, p.id)
        .where.isNull(p.deletedAt).and.eq(p.id, id.value)
        .orderBy(p.id)
    }.one(ProgrammerTable(p))
      .toMany(_.get[Option[SkillId]](ps.resultName.skillId))
      .map { (programmer, skills) => programmer.copy(skillIds = skills) }
      .single.apply()
  }

  def insert(name: String, companyId: Option[CompanyId], now: DateTime = DateTime.now())(implicit session: DBSession): Programmer = {
    val version = 1
    val id = withSQL {
      QueryDSL.insert.into(ProgrammerTable).namedValues(
        pc.name      -> name,
        pc.companyId -> companyId.map(_.value),
        pc.createdAt -> now,
        pc.updatedAt -> now,
        pc.version   -> version
      )
    }.updateAndReturnGeneratedKey.apply()
    Programmer(id = ProgrammerId(id), name = name, companyId = companyId, createdAt = now, updatedAt = now, version = version)
  }

  def update(p: Programmer, now: DateTime = DateTime.now())(implicit session: DBSession): Option[Programmer] = {
    val version = p.version + 1
    val updated = withSQL {
      QueryDSL.update(ProgrammerTable).set(
        pc.name      -> p.name,
        pc.companyId -> p.companyId.map(_.value),
        pc.updatedAt -> now,
        pc.version   -> version
      ).where.eq(pc.id, p.id.value).and.eq(pc.version, p.version)
    }.update.apply() > 0
    if (updated) Some(p.copy(updatedAt = now, version = version)) else None
  }

  def delete(id: ProgrammerId, now: DateTime = DateTime.now())(implicit session: DBSession): Boolean = {
    withSQL {
      QueryDSL.update(ProgrammerTable).set(
        pc.deletedAt -> now
      ).where.eq(pc.id, id.value)
    }.update.apply() > 0
  }

}
object ProgrammerDao {

  private[programmer] object ProgrammerTable extends SQLSyntaxSupport[Programmer] {

    override val tableName = "programmer"
    override val nameConverters = Map("At$" -> "_timestamp")

    // simple extractor
    def apply(p: SyntaxProvider[Programmer])(rs: WrappedResultSet): Programmer = autoConstruct(rs, p, "skillIds")

  }

}

