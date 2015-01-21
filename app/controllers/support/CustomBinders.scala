package controllers.support

import controllers.support.binders.{AtomWrapperBinder, EnumLikeBinder}

trait CustomBinders
    extends EnumLikeBinder
    with AtomWrapperBinder

object CustomBinders extends CustomBinders