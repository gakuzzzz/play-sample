resolvers ++= Seq(
  "sonatype releases" at "http://oss.sonatype.org/content/repositories/releases"
)
addSbtPlugin("com.typesafe.play" % "sbt-plugin"                   % "2.3.7")
addSbtPlugin("com.typesafe.sbt"  % "sbt-coffeescript"             % "1.0.0")
addSbtPlugin("com.timushev.sbt"  % "sbt-updates"                  % "0.1.7")
