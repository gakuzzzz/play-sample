package controllers

import controllers.stack.{AuthConfigImpl, TxElement}
import jp.t2v.lab.play2.auth.LoginLogout
import models.ComponentRegistry
import models.aggregates.account.{PlainPassword, Account}
import models.services.ServiceComponents
import models.shared.PlaySampleContext
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.mvc.Controller
import _root_.controllers.support.CustomMappings._
import _root_.controllers.support.SyntaxSupport._

trait Sessions extends Controller with TxElement with LoginLogout with AuthConfigImpl { self: ServiceComponents =>

  import play.api.libs.concurrent.Execution.Implicits._

  private def loginForm(implicit ctx: PlaySampleContext): Form[Option[Account]] = Form {
    mapping("email" -> email, "password" -> atom[PlainPassword])(accountService.authenticate)(_.map(u => (u.mail, PlainPassword(""))))
      .verifying("Invalid email or password", result => result.isDefined)
  }

  def login = StackAction { implicit req =>
    implicit val ctx = PlaySampleContext(dbSession, None)
    Ok(views.html.login(loginForm))
  }

  def authenticate = AsyncStack { implicit req =>
    implicit val ctx = PlaySampleContext(dbSession, None)
    for {
      authed  <- loginForm.bindFromRequest()    V    {withErrors => BadRequest(views.html.login(withErrors))}
      account <- authed                         V    InternalServerError
    } yield gotoLoginSucceeded(account.id)
  }

}
object Sessions extends Sessions with ComponentRegistry
