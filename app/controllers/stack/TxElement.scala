package controllers.stack

import jp.t2v.lab.play2.stackc.{RequestWithAttributes, RequestAttributeKey, StackableController}
import play.api.mvc.{Result, Controller}
import scalikejdbc._

import scala.concurrent.Future

trait TxElement extends StackableController { self: Controller =>

  private object DBSessionKey extends RequestAttributeKey[DBSession]

  override def proceed[A](req: RequestWithAttributes[A])(f: (RequestWithAttributes[A]) => Future[Result]): Future[Result] = {
    import TxBoundary.Future._
    DB.localTx { session =>
      super.proceed(req.set(DBSessionKey, session))(f)
    }
  }

  implicit def dbSession(implicit req: RequestWithAttributes[_]): DBSession = req.get(DBSessionKey).get

}
