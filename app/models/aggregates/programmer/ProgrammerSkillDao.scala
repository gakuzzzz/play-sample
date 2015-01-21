package models.aggregates.programmer

import scalikejdbc._
import models.aggregates._

class ProgrammerSkillDao {

  import ProgrammerSkillDao._

  private val psc = ProgrammerSkillTable.column

  def insert(pId: ProgrammerId, sId: SkillId)(implicit session: DBSession): Boolean = {
    withSQL {
      QueryDSL.insert.into(ProgrammerSkillTable).namedValues(
        psc.programmerId -> pId.value,
        psc.skillId      -> sId.value
      )
    }.update.apply() > 0
  }

  def delete(pId: ProgrammerId, sId: SkillId)(implicit session: DBSession): Boolean = {
    withSQL {
      QueryDSL.delete.from(ProgrammerSkillTable)
        .where.eq(psc.programmerId, pId.value)
        .and.eq(psc.skillId, sId.value)
    }.update.apply() > 0
  }

}
object ProgrammerSkillDao {

  private[programmer] object ProgrammerSkillTable extends SQLSyntaxSupport[ProgrammerSkill] {
    override val tableName = "programmer_skill"
  }

}