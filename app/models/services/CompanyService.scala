package models.services

import models.aggregates.company.{Company, CompanyDao}
import models.aggregates.CompanyId
import models.shared.PlaySampleContext

class CompanyService(implicit companyDao: CompanyDao) {

  def findById(id: CompanyId)(implicit ctx: PlaySampleContext): Option[Company] = {
    companyDao.findById(id)
  }

  def findAll(implicit ctx: PlaySampleContext): Seq[Company] = {
    companyDao.findAll
  }

  def create(name: String, url: Option[String])(implicit ctx: PlaySampleContext): Company = {
    companyDao.insert(name, url)
  }

  def update(company: Company)(implicit ctx: PlaySampleContext): Option[Company] = {
    companyDao.update(company)
  }

  def delete(id: CompanyId)(implicit ctx: PlaySampleContext): Boolean = {
    companyDao.delete(id)
  }

}
