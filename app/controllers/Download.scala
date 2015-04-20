package controllers

import play.api.libs.iteratee.Enumerator
import play.api.mvc.{Controller, Action}
import play.api.libs.concurrent.Execution.Implicits._

case class Csv()


trait Download extends Controller {

  def it = Iterator.iterate(0)(_ + 1).takeWhile(_ < 10000).map(_.toString + ", ")
  def enum = Enumerator.enumerate(it)

  def index = Action {
    Ok.chunked(enum)
  }


}

object Download extends Download
