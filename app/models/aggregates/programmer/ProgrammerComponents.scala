package models.aggregates.programmer

trait ProgrammerComponents {

  private[models] implicit lazy val programmerDao: ProgrammerDao = new ProgrammerDao
  private[models] implicit lazy val programmerSkillDao: ProgrammerSkillDao = new ProgrammerSkillDao

}
