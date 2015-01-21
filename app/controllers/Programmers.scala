package controllers

import play.api._, mvc._
import play.api.data._, Forms._, validation.Constraints._, format.Formats._

import models.aggregates.{ProgrammerId, SkillId, CompanyId}
import _root_.controllers.support.CustomMappings._
import _root_.controllers.support.CustomJsonFormats._
import _root_.controllers.support.SyntaxSupport._
import _root_.controllers.stack.InitialContextElement
import play.api.libs.json.Json
import models.services.ServiceComponents
import views.programmers.ProgrammerView
import models.ComponentRegistry

trait Programmers extends Controller with InitialContextElement { self: ServiceComponents =>

  def all = StackAction { implicit req =>
    val items = programmerService.findAll
    val views = items.map { case (p, c, ss) => ProgrammerView(p.id, p.name, p.createdAt, p.updatedAt, c, ss) }
    Ok(Json.toJson(views))
  }

  def show(id: ProgrammerId) = StackAction { implicit req =>
    for {
      item <- programmerService.findById(id)  V  NotFound
    } yield {
      val (p, c, ss) = item
      val view = ProgrammerView(p.id, p.name, p.createdAt, p.updatedAt, c, ss)
      Ok(Json.toJson(view))
    }
  }

  case class ProgrammerForm(name: String, companyId: Option[CompanyId] = None)

  private val programmerForm = Form(
    mapping(
      "name"      -> text.verifying(nonEmpty),
      "companyId" -> optional(atom[CompanyId])
    )(ProgrammerForm.apply)(ProgrammerForm.unapply)
  )

  def create = StackAction { implicit req =>
    for {
      form <- programmerForm.bindFromRequest  V   BadRequest("invalid parameters")
    } yield {
      val programmer = programmerService.create(form.name, form.companyId)
//      Created.withHeaders(LOCATION -> routes.Programmers.show(programmer.id).url)
      NoContent // TODO: backbone bug
    }
  }

  def addSkill(programmerId: ProgrammerId, skillId: SkillId) = StackAction { implicit req =>
    for {
      p <- programmerService.findByIdWithoutAssoc(programmerId)   V   NotFound("Programmer not found!")
      _ <- programmerService.addSkill(p, skillId)                 V   Conflict
    } yield {
      Ok
    }
  }

  def deleteSkill(programmerId: ProgrammerId, skillId: SkillId) = StackAction { implicit req =>
    for {
      p <- programmerService.findByIdWithoutAssoc(programmerId)   V   NotFound("Programmer not found!")
      _ <- programmerService.deleteSkill(p, skillId)              V   Conflict
    } yield {
      Ok
    }
  }

  def joinCompany(programmerId: ProgrammerId, companyId: CompanyId) = StackAction { implicit req =>
    for {
      p <- programmerService.findByIdWithoutAssoc(programmerId)   V   NotFound("Programmer not found!")
      _ <- programmerService.joinCompany(p, companyId)            V   Conflict
    } yield {
      Ok
    }
  }

  def leaveCompany(programmerId: ProgrammerId) = StackAction { implicit req =>
    for {
      p <- programmerService.findByIdWithoutAssoc(programmerId)   V   NotFound("Programmer not found!")
      _ <- programmerService.leaveCompany(p)                      V   Conflict
    } yield {
      Ok
    }
  }

  def delete(id: ProgrammerId) = StackAction { implicit req =>
    for {
      p <- programmerService.findByIdWithoutAssoc(id)   V   NotFound("Programmer not found!")
      _ <- programmerService.delete(id)                 V   Conflict
    } yield {
      NoContent
    }
  }

}
object Programmers extends Programmers with ComponentRegistry
