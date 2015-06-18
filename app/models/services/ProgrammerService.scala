package models.services

import models.aggregates.skill.{Skill, SkillDao}
import models.aggregates.programmer.{Programmer, ProgrammerDao, ProgrammerSkillDao}
import models.shared.PlaySampleContext
import models.aggregates.{ProgrammerId, SkillId, CompanyId}
import models.aggregates.company.{CompanyDao, Company}
import scalikejdbc.DBSession

class ProgrammerService(implicit programmerDao: ProgrammerDao, programmerSkillDao: ProgrammerSkillDao, skillDao: SkillDao, companyDao: CompanyDao) {

  def findAll(implicit ctx: PlaySampleContext): Seq[(Programmer, Option[Company], Seq[Skill])] = {
    val ps: Seq[Programmer] = programmerDao.findAll
    val companyIds: Seq[CompanyId] = ps.flatMap(_.companyId)
    val skillIds: Seq[SkillId] = ps.flatMap(_.skillIds)
    val companyMap: Map[CompanyId, Company] = companyDao.findByIds(companyIds).map(c => c.id -> c).toMap
    val skillMap: Map[SkillId, Skill] = skillDao.findByIds(skillIds).map(s => s.id -> s).toMap
    ps.map { p =>
      (p, p.companyId.flatMap(companyMap.get), p.skillIds.flatMap(skillMap.get))
    }
  }

  def findSubset(limit: Int, offset: Int)(implicit ctx: DBSession): Seq[Programmer] = {
    programmerDao.findSubset(limit, offset)
  }

  def findByIdWithoutAssoc(id: ProgrammerId)(implicit ctx: PlaySampleContext): Option[Programmer] = {
    programmerDao.findById(id)
  }

  def findById(id: ProgrammerId)(implicit ctx: PlaySampleContext): Option[(Programmer, Option[Company], Seq[Skill])] = {
    programmerDao.findById(id).map { p =>
      val skills = skillDao.findByIds(p.skillIds).map(s => s.id -> s).toMap
      (p, p.companyId.flatMap(companyDao.findById), p.skillIds.flatMap(skills.get))
    }
  }

  def create(name: String, companyId: Option[CompanyId])(implicit ctx: PlaySampleContext): Programmer = {
    programmerDao.insert(name, companyId)
  }

  def delete(id: ProgrammerId)(implicit ctx: PlaySampleContext): Boolean = {
    programmerDao.delete(id)
  }

  def addSkill(programmer: Programmer, skillId: SkillId)(implicit ctx: PlaySampleContext): Boolean = {
    if (programmer.skillIds.contains(skillId)) false
    else programmerSkillDao.insert(programmer.id, skillId)
  }

  def deleteSkill(programmer: Programmer, skillId: SkillId)(implicit ctx: PlaySampleContext): Boolean = {
    if (!programmer.skillIds.contains(skillId)) false
    else programmerSkillDao.delete(programmer.id, skillId)
  }

  def joinCompany(programmer: Programmer, companyId: CompanyId)(implicit ctx: PlaySampleContext): Boolean = {
    val update = programmer.copy(companyId = Some(companyId))
    programmerDao.update(update).isDefined
  }

  def leaveCompany(programmer: Programmer)(implicit ctx: PlaySampleContext): Boolean = {
    val update = programmer.copy(companyId = None)
    programmerDao.update(update).isDefined
  }

}
