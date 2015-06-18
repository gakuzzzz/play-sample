package controllers.stack

import jp.t2v.lab.play2.stackc.{RequestWithAttributes, RequestAttributeKey, StackableController}
import play.api.mvc.{Result, Controller}
import scalikejdbc._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure

trait TxElement extends StackableController { self: Controller =>

  private object DBSessionKey extends RequestAttributeKey[DBSession]

  private implicit object ResultTxBoundary extends TxBoundary[Future[Result]] {
    override def finishTx(result: Future[Result], tx: Tx): Future[Result] = {
      result.andThen {
        case Failure(_) => tx.rollback()
      }.map {
        result => result.copy(body = result.body.onDoneEnumerating(tx.commit()))
      }
    }
    override def closeConnection(result: Future[Result], doClose: () => Unit): Future[Result] = {
      result.andThen {
        case Failure(_) => doClose()
      }.map {
        result => result.copy(body = result.body.onDoneEnumerating(doClose()))
      }
    }
  }

  override def proceed[A](req: RequestWithAttributes[A])(f: (RequestWithAttributes[A]) => Future[Result]): Future[Result] = {
    DB.localTx { session =>
      super.proceed(req.set(DBSessionKey, session))(f)
    }
  }

  implicit def dbSession(implicit req: RequestWithAttributes[_]): DBSession = req.get(DBSessionKey).get

}
