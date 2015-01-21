package models.aggregates.skill

import models.aggregates.SkillId
import org.joda.time.DateTime
import scalikejdbc._

class SkillDao {

  import SkillDao._

  private val s = SkillTable.syntax("s")
  private val sc = SkillTable.column

  def findById(id: SkillId)(implicit session: DBSession): Option[Skill] = withSQL {
    select.from(SkillTable as s).where.eq(s.id, id.value).and.isNull(s.deletedAt)
  }.map(SkillTable(s)).single.apply()

  def findByIds(ids: Traversable[SkillId])(implicit session: DBSession): Seq[Skill] = withSQL {
    select.from(SkillTable as s).where.in(s.id, ids.map(_.value).toSeq).and.isNull(s.deletedAt)
  }.map(SkillTable(s)).list.apply()

  def findAll(implicit session: DBSession): Seq[Skill] = withSQL {
    select.from(SkillTable as s)
      .where.isNull(s.deletedAt)
      .orderBy(s.id)
  }.map(SkillTable(s)).list.apply()

  def insert(name: String, now: DateTime = DateTime.now)(implicit session: DBSession): Skill = {
    val id = withSQL {
      QueryDSL.insert.into(SkillTable).namedValues(sc.name -> name, sc.createdAt -> now)
    }.updateAndReturnGeneratedKey.apply()
    Skill(id = SkillId(id), name = name, createdAt = now)
  }

  def delete(id: SkillId, now: DateTime = DateTime.now)(implicit session: DBSession): Boolean = {
    withSQL {
      update(SkillTable).set(sc.deletedAt -> now).where.eq(sc.id, id.value)
    }.update.apply() > 0
  }

}
object SkillDao {

  private[skill] object SkillTable extends SQLSyntaxSupport[Skill] {

    override val tableName = "skill"

    def apply(s: SyntaxProvider[Skill])(rs: WrappedResultSet): Skill = autoConstruct(rs, s)

  }

}
