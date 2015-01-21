package models

import models.shared.PlaySampleContext
import scalikejdbc.DBSession

package object services {

  implicit def contextToDbSession(implicit ctx: PlaySampleContext): DBSession = ctx.dbSession

}
