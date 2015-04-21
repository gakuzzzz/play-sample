package controllers.stack

import jp.t2v.lab.play2.auth.AuthenticationElement
import jp.t2v.lab.play2.stackc.{RequestAttributeKey, RequestWithAttributes}
import models.services.ServiceComponents
import play.api.mvc.{Result, Controller}
import scala.concurrent.Future
import models.shared.PlaySampleContext

trait InitialContextElement extends TxElement with AuthenticationElement with AuthConfigImpl { self: Controller with ServiceComponents =>

  private object ContextKey extends RequestAttributeKey[PlaySampleContext]

  override def proceed[A](req: RequestWithAttributes[A])(f: (RequestWithAttributes[A]) => Future[Result]): Future[Result] = {
    super.proceed(req) { implicit r =>
      f(r.set(ContextKey, PlaySampleContext(dbSession, Some(loggedIn))))
    }
  }

  implicit def playSampleContext(implicit req: RequestWithAttributes[_]): PlaySampleContext = req.get(ContextKey).get

}
