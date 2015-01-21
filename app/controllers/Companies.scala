package controllers

import play.api._, mvc._
import play.api.data._, Forms._, validation.Constraints._

import models.aggregates.CompanyId
import play.api.libs.json.Json
import _root_.controllers.support.CustomJsonFormats._
import _root_.controllers.support.SyntaxSupport._
import _root_.controllers.stack.InitialContextElement
import models.ComponentRegistry
import models.services.ServiceComponents

trait Companies extends Controller with InitialContextElement { self: ServiceComponents =>

  def all = StackAction { implicit req =>
    Ok(Json.toJson(companyService.findAll))
  }

  def show(id: CompanyId) = StackAction { implicit req =>
    for {
      company <- companyService.findById(id)   V   NotFound
    } yield {
      Ok(Json.toJson(company))
    }
  }

  case class CompanyForm(name: String, url: Option[String] = None)

  private val companyForm = Form(
    mapping(
      "name" -> text.verifying(nonEmpty),
      "url"  -> optional(text)
    )(CompanyForm.apply)(CompanyForm.unapply)
  )

  def create = StackAction { implicit req =>
    for {
      form <- companyForm.bindFromRequest   V   BadRequest("invalid parameters")
    } yield {
      val company = companyService.create(form.name, form.url)
//      Created.withHeaders(LOCATION -> routes.Companies.show(company.id).url)
      NoContent // TODO: backbone bug
    }
  }

  def delete(id: CompanyId) = StackAction { implicit req =>
    for {
      company <- companyService.findById(id)   V   NotFound
      _       <- companyService.delete(id)     V   Conflict
    } yield {
      NoContent
    }
  }

}
object Companies extends Companies with ComponentRegistry