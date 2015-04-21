package controllers.stack

import controllers.routes
import jp.t2v.lab.play2.auth.AuthConfig
import models.aggregates.AccountId
import models.aggregates.account.Account
import models.services.ServiceComponents
import models.shared.PlaySampleContext
import play.api.mvc.{Result, RequestHeader, Controller}

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.{ClassTag, classTag}
import scala.concurrent.ExecutionContext.Implicits.global

trait AuthConfigImpl extends AuthConfig { self: Controller with ServiceComponents =>

  type Id = AccountId

  type User = Account

  type Authority = Unit

  val idTag: ClassTag[Id] = classTag[Id]

  val sessionTimeoutInSeconds: Int = 3600

  def resolveUser(id: Id)(implicit ctx: ExecutionContext): Future[Option[User]] = Future {
    accountService.findById(id)(PlaySampleContext.NotInitialized)
  }

  def loginSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] = {
    Future.successful(Redirect(routes.Root.index()))
  }

  def logoutSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] = {
    Future.successful(Redirect(routes.Sessions.login()))
  }

  def authenticationFailed(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] = {
    Future.successful(Redirect(routes.Sessions.login()))
  }

  def authorizationFailed(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] = Future.successful(Forbidden("no permission"))

  def authorize(user: User, authority: Authority)(implicit ctx: ExecutionContext): Future[Boolean] = Future.successful(true)

}
