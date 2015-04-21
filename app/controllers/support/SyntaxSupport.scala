package controllers.support

import play.api.data.Form
import play.api.mvc.Result

import scala.concurrent.Future
import scala.util.Either.RightProjection

object SyntaxSupport {

  implicit def formToEither[A](form: Form[A]): Either[Form[A], A] = form.fold(Left.apply, Right.apply)
  implicit def eitherToResult(e: Either[Result, Result]): Result = e.merge
  implicit def eitherToFutureResult(e: Either[Result, Future[Result]]): Future[Result] = e.left.map(Future.successful).merge
  implicit class OptionOps[A](val value: Option[A]) extends AnyVal {
    def V(left: => Result): RightProjection[Result, A] = value.toRight(left).right
  }
  implicit class FormOps[A](val value: Form[A]) extends AnyVal {
    def V(f: Form[A] => Result): RightProjection[Result, A] = value.left.map(f).right
    def V(left: => Result): RightProjection[Result, A] = value.left.map(_ => left).right
  }
  implicit class BooleanOps(val value: Boolean) extends AnyVal {
    def V(left: => Result): RightProjection[Result, Unit] = Either.cond(value, (), left).right
  }
  implicit class EitherOps[A, B](val value: Either[A, B]) extends AnyVal {
    def V(f: A => Result): RightProjection[Result, B] = value.left.map(f).right
    def V(left: => Result): RightProjection[Result, B] = value.left.map(_ => left).right
  }

}