package models.aggregates.account

import models.aggregates.AccountId
import scalikejdbc._

class AccountDao {

  import AccountDao._

  private val a = AccountTable.syntax("a")

  def findById(id: AccountId)(implicit session: DBSession): Option[Account] = {
    withSQL {
      select.from(AccountTable as a).where.eq(a.id, id.value).and.isNull(a.deletedAt)
    }.map(AccountTable(a)).single.apply()

  }

  def findByMail(mail: String)(implicit session: DBSession): Option[Account] = {
    withSQL {
      select.from(AccountTable as a).where.eq(a.mail, mail).and.isNull(a.deletedAt)
    }.map(AccountTable(a)).single.apply()
  }

}
object AccountDao {

  private[account] object AccountTable extends SQLSyntaxSupport[Account] {

    override val tableName = "account"

    def apply(s: SyntaxProvider[Account])(rs: WrappedResultSet): Account = autoConstruct(rs, s)

  }


}