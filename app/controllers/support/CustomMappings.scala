package controllers.support

import controllers.support.mappings.{AtomWrapperMapping, EnumLikeMapping}

trait CustomMappings
    extends EnumLikeMapping
    with AtomWrapperMapping

object CustomMappings extends CustomMappings
