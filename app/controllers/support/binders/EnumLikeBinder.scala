package controllers.support.binders

import play.api.mvc.{PathBindable, QueryStringBindable}
import models.shared.enum.{EnumCompanion, EnumLike}
import scala.reflect.ClassTag

trait EnumLikeBinder {

  implicit def enumLikeQueryStringBindable[A <: EnumLike]
      (implicit companion: EnumCompanion[A], base: QueryStringBindable[A#ValueType], classTag: ClassTag[A]): QueryStringBindable[A] = new QueryStringBindable[A] {
    override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, A]] = {
      base.bind(key, params).map(_.right.flatMap(v => companion.valueOf(v).toRight(s"Could not parse $v to ${classTag.runtimeClass.getSimpleName}")))
    }
    override def unbind(key: String, value: A): String = base.unbind(key, value.value)
  }

  implicit def enumLikePathBindable[A <: EnumLike]
      (implicit companion: EnumCompanion[A], base: PathBindable[A#ValueType], classTag: ClassTag[A]): PathBindable[A] = new PathBindable[A] {

    override def bind(key: String, value: String): Either[String, A] = {
      base.bind(key, value).right.flatMap(v => companion.valueOf(v).toRight(s"Could not parse $v to ${classTag.runtimeClass.getSimpleName}"))
    }

    override def unbind(key: String, value: A): String = base.unbind(key, value.value)

  }

}

object EnumLikeBinder extends EnumLikeBinder
