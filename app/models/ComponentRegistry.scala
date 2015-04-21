package models

import models.aggregates.account.AccountComponents
import models.services.ServiceComponents
import models.aggregates.programmer.ProgrammerComponents
import models.aggregates.skill.SkillComponents
import models.aggregates.company.CompanyComponents

trait ComponentRegistry extends ServiceComponents
  with ProgrammerComponents
  with SkillComponents
  with CompanyComponents
  with AccountComponents

object ComponentRegistry extends ComponentRegistry