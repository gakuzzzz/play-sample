package models.aggregates.account

trait AccountComponents {

  private[models] implicit lazy val accountDao: AccountDao = new AccountDao

}
