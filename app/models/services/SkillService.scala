package models.services

import models.aggregates.SkillId
import models.aggregates.skill.{SkillDao, Skill}
import models.shared.PlaySampleContext

class SkillService(implicit skillDao: SkillDao) {

  def findAll(implicit ctx: PlaySampleContext): Seq[Skill] = {
    skillDao.findAll
  }

  def findById(id: SkillId)(implicit ctx: PlaySampleContext): Option[Skill] = {
    skillDao.findById(id)
  }

  def create(name: String)(implicit ctx: PlaySampleContext): Skill = {
    skillDao.insert(name)
  }

  def delete(id: SkillId)(implicit ctx: PlaySampleContext): Boolean = {
    skillDao.delete(id)
  }

}
