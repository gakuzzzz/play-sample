package models.services

import models.aggregates.programmer.ProgrammerComponents
import models.aggregates.skill.SkillComponents
import models.aggregates.company.CompanyComponents

trait ServiceComponents {
    self: ProgrammerComponents with SkillComponents with CompanyComponents =>

  implicit lazy val programmerService: ProgrammerService = new ProgrammerService
  implicit lazy val skillService: SkillService = new SkillService
  implicit lazy val companyService: CompanyService = new CompanyService


}
