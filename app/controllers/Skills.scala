package controllers

import models.services.ServiceComponents
import play.api._, mvc._
import play.api.data._, Forms._

import _root_.controllers.support.SyntaxSupport._
import _root_.controllers.stack.InitialContextElement
import models.ComponentRegistry
import models.aggregates.SkillId
import play.api.libs.json.Json
import _root_.controllers.support.CustomJsonFormats._
import _root_.controllers.support.SyntaxSupport._

trait Skills extends Controller with InitialContextElement { self: ServiceComponents =>

  def all = StackAction { implicit req =>
    Ok(Json.toJson(skillService.findAll))
  }

  def show(id: SkillId) = StackAction { implicit req =>
    for {
      skill <- skillService.findById(id)  V  NotFound
    } yield {
      Ok(Json.toJson(skill))
    }
  }

  case class SkillForm(name: String)

  private val skillForm = Form(
    mapping("name" -> nonEmptyText)(SkillForm.apply)(SkillForm.unapply)
  )

  def create = StackAction { implicit req =>
    for {
      form  <- skillForm.bindFromRequest()  V  BadRequest("invalid parameters")
    } yield {
      val skill = skillService.create(form.name)
//      Created.withHeaders(LOCATION -> routes.Skills.show(skill.id).url)
      NoContent // TODO: backbone bug
    }
  }

  def delete(id: SkillId) = StackAction { implicit req =>
    for {
      old <- skillService.findById(id)  V  NotFound
      _   <- skillService.delete(id)    V  Conflict
    } yield {
      NoContent
    }
  }

}
object Skills extends Skills with ComponentRegistry
