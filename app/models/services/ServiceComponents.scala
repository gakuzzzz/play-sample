package models.services

import models.aggregates.account.AccountComponents
import models.aggregates.programmer.ProgrammerComponents
import models.aggregates.skill.SkillComponents
import models.aggregates.company.CompanyComponents

trait ServiceComponents {
    self: ProgrammerComponents with SkillComponents with CompanyComponents with AccountComponents =>

  implicit lazy val programmerService: ProgrammerService = new ProgrammerService
  implicit lazy val skillService: SkillService = new SkillService
  implicit lazy val companyService: CompanyService = new CompanyService
  implicit lazy val accountService: AccountService = new AccountService

}
