package models.aggregates.skill

trait SkillComponents {

  private[models] implicit lazy val skillDao: SkillDao = new SkillDao

}
