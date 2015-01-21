package models.aggregates.company

trait CompanyComponents {

  private[models] implicit lazy val companyDao: CompanyDao = new CompanyDao

}
