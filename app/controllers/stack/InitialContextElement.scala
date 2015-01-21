package controllers.stack

import jp.t2v.lab.play2.stackc.{RequestAttributeKey, RequestWithAttributes, StackableController}
import play.api.mvc.{Result, Controller}
import scala.concurrent.Future
import models.shared.PlaySampleContext
import scalikejdbc._

trait InitialContextElement extends StackableController { self: Controller =>

  private object ContextKey extends RequestAttributeKey[PlaySampleContext]

  override def proceed[A](req: RequestWithAttributes[A])(f: (RequestWithAttributes[A]) => Future[Result]): Future[Result] = {
    import TxBoundary.Future._
    DB.localTx { session =>
      super.proceed(req.set(ContextKey, PlaySampleContext(session)))(f)
    }
  }

  implicit def playSampleContext(implicit req: RequestWithAttributes[_]): PlaySampleContext = req.get(ContextKey).get

}
