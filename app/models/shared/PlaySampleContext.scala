package models.shared

import models.aggregates.account.Account
import scalikejdbc.{AutoSession, DBSession}

case class PlaySampleContext(dbSession: DBSession, account: Option[Account])

object PlaySampleContext {

  def NotInitialized: PlaySampleContext = PlaySampleContext(AutoSession, None)

}