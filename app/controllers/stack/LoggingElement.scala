package controllers.stack

import jp.t2v.lab.play2.stackc.RequestWithAttributes
import models.services.ServiceComponents
import play.api.Logger
import play.api.mvc.{Result, Controller}

import scala.concurrent.Future
import scala.util.Failure

trait LoggingElement extends InitialContextElement { self: Controller with ServiceComponents =>

  override def proceed[A](req: RequestWithAttributes[A])(f: (RequestWithAttributes[A]) => Future[Result]): Future[Result] = {
    super.proceed(req) { implicit r =>
      implicit val ec = StackActionExecutionContext
      f(r).andThen {
        case Failure(e) => logging(e)
      }
    }
  }

  private def logging(e: Throwable)(implicit request: RequestWithAttributes[_]): Unit = {
    Logger.error(s"An error has occurred. account: ${playSampleContext.account.map(a => a.id -> a.name)}", e)
  }

  override def cleanupOnFailed[A](request: RequestWithAttributes[A], e: Throwable) = {
    super.cleanupOnFailed(request, e)
    logging(e)(request)
  }
}
