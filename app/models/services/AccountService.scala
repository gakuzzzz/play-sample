package models.services

import models.aggregates.AccountId
import models.aggregates.account.{PlainPassword, AccountDao, Account}
import models.shared.PlaySampleContext

class AccountService(implicit accountDao: AccountDao) {

  def findById(id: AccountId)(implicit ctx: PlaySampleContext): Option[Account] = {
    accountDao.findById(id)
  }

  def authenticate(mail: String, password: PlainPassword)(implicit ctx: PlaySampleContext): Option[Account] = {
    accountDao.findByMail(mail).filter(a => password.isValid(a.hashedPassword))
  }

}
