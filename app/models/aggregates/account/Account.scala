package models.aggregates.account

import models.aggregates.AccountId
import org.joda.time.DateTime


case class Account(
    id: AccountId,
    name: String,
    mail: String,
    hashedPassword: HashedPassword,
    createdAt: DateTime,
    updatedAt: DateTime,
    deletedAt: Option[DateTime] = None,
    version: Int) {

}
