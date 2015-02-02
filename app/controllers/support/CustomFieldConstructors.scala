package controllers.support

import views.html.helper.FieldConstructor

trait CustomFieldConstructors {

  implicit val bootstrapFieldConstructor = FieldConstructor(views.html.bootstrapFieldConstructor.f)

}
object CustomFieldConstructors extends CustomFieldConstructors
