package models.aggregates.company

import org.joda.time.DateTime
import models.aggregates.CompanyId

case class Company(
    id: CompanyId,
    name: String,
    url: Option[String],
    createdAt: DateTime,
    deletedAt: Option[DateTime] = None) {

}
