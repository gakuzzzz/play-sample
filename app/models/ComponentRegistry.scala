package models

import models.services.ServiceComponents
import models.aggregates.programmer.ProgrammerComponents
import models.aggregates.skill.SkillComponents
import models.aggregates.company.CompanyComponents

trait ComponentRegistry extends ServiceComponents
  with ProgrammerComponents
  with SkillComponents
  with CompanyComponents

object ComponentRegistry extends ComponentRegistry