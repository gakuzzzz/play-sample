package controllers

import controllers.stack.LoggingElement
import models.ComponentRegistry
import models.services.ServiceComponents
import play.api._, mvc._

trait Root extends Controller with LoggingElement { self: ServiceComponents =>

  def index = StackAction { implicit req =>
    Ok(views.html.index())
  }

}
object Root extends Root with ComponentRegistry