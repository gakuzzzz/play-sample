package controllers

import models.aggregates.company.Company
import play.api._, mvc._
import play.api.data._, Forms._, validation.Constraints._

import models.aggregates.CompanyId
import play.api.libs.json.Json
import _root_.controllers.support.CustomJsonFormats._
import _root_.controllers.support.SyntaxSupport._
import _root_.controllers.support.CustomFieldConstructors.bootstrapFieldConstructor
import _root_.controllers.stack.InitialContextElement
import models.ComponentRegistry
import models.services.ServiceComponents

trait Companies extends Controller with InitialContextElement { self: ServiceComponents =>

  def index = StackAction { implicit req =>
    val companies = companyService.findAll
    Ok(views.html.companies.index(companies))
  }

  def show(id: CompanyId) = StackAction { implicit req =>
    for {
      company <- companyService.findById(id)   V   NotFound
    } yield {
      Ok(Json.toJson(company))
    }
  }

  import Companies.CompanyForm

  def newForm = StackAction { implicit req =>
    Ok(views.html.companies.newForm(companyForm.fill(CompanyForm.Empty)))
  }

  private val companyForm = Form(
    mapping(
      "name"    -> nonEmptyText,
      "url"     -> optional(text),
      "version" -> number
    )(CompanyForm.apply)(CompanyForm.unapply)
  )

  def create = StackAction { implicit req =>
    for {
      form <- companyForm.bindFromRequest   V   {withError => BadRequest(views.html.companies.newForm(withError))}
    } yield {
      companyService.create(form.name, form.url)
      Redirect(routes.Companies.index()).flashing("success" -> "作成しました。")
    }
  }

  def editForm(id: CompanyId) = StackAction { implicit req =>
    for {
      old     <- companyService.findById(id)              V   NotFound
    } yield {
      Ok(views.html.companies.editForm(companyForm.fill(CompanyForm.of(old)), id))
    }
  }

  def update(id: CompanyId) = StackAction { implicit req =>
    for {
      old     <- companyService.findById(id)              V   NotFound
      form    <- companyForm.bindFromRequest              V   {withError => BadRequest(views.html.companies.editForm(withError, id))}
      updated <- companyService.update(form.merge(old))   V   Conflict("他の人の修正と衝突しました")
    } yield {
      Redirect(routes.Companies.index()).flashing("success" -> "更新しました。")
    }
  }

  def delete(id: CompanyId) = StackAction { implicit req =>
    for {
      company <- companyService.findById(id)   V   NotFound
      _       <- companyService.delete(id)     V   Conflict
    } yield {
      Redirect(routes.Companies.index()).flashing("success" -> "削除しました。")
    }
  }

}
object Companies extends Companies with ComponentRegistry {

  case class CompanyForm(name: String, url: Option[String] = None, version: Int) {
    def merge(base: Company): Company = base.copy(name = name, url = url, version = version)
  }
  object CompanyForm {
    def of(company: Company): CompanyForm = CompanyForm(name = company.name, url = company.url, version = company.version)
    def Empty: CompanyForm = CompanyForm("", None, 1)
  }

}