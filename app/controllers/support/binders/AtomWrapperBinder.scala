package controllers.support.binders

import play.api.mvc.{PathBindable, QueryStringBindable}
import scala.reflect.ClassTag
import models.shared.atom.{AtomCompanion, AtomWrapper}

trait AtomWrapperBinder {

  implicit def atomWrapperQueryStringBindable[A <: AtomWrapper]
      (implicit companion: AtomCompanion[A], base: QueryStringBindable[A#ValueType], classTag: ClassTag[A]): QueryStringBindable[A] = new QueryStringBindable[A] {
    override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, A]] = {
      base.bind(key, params).map(_.right.map(companion.apply))
    }
    override def unbind(key: String, value: A): String = base.unbind(key, value.value)
  }

  implicit def atomWrapperPathBindable[A <: AtomWrapper]
      (implicit companion: AtomCompanion[A], base: PathBindable[A#ValueType], classTag: ClassTag[A]): PathBindable[A] = new PathBindable[A] {
    override def bind(key: String, value: String): Either[String, A] = {
      base.bind(key, value).right.map(companion.apply)
    }
    override def unbind(key: String, value: A): String = base.unbind(key, value.value)
  }

}
object AtomWrapperBinder extends AtomWrapperBinder