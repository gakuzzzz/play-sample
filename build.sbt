lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .enablePlugins(SbtWeb)
  .settings(
    name := "play2-sample",
    version := "0.1",
    scalaVersion := "2.11.5",
    libraryDependencies ++= Seq(
      "jp.t2v"                    %% "stackable-controller"             % "0.4.1",
      "org.scalikejdbc"           %% "scalikejdbc"                      % scalikejdbcVersion,
      "org.scalikejdbc"           %% "scalikejdbc-syntax-support-macro" % scalikejdbcVersion,
      "org.scalikejdbc"           %% "scalikejdbc-config"               % scalikejdbcVersion,
      "org.scalikejdbc"           %% "scalikejdbc-play-plugin"          % scalikejdbcPlayVersion,
      "org.scalikejdbc"           %% "scalikejdbc-play-fixture-plugin"  % scalikejdbcPlayVersion,
      "com.h2database"             % "h2"                               % h2Version,
      "com.github.tototoshi"      %% "play-flyway"                      % "1.2.1",
      "org.scalikejdbc"           %% "scalikejdbc-test"                 % scalikejdbcVersion  % "test",
      "org.scalatest"             %% "scalatest"                        % "2.2.2"             % "test",
      "org.scalacheck"            %% "scalacheck"                       % "1.12.1"            % "test",
      "com.typesafe.play"         %% "play-test"                        % playVersion         % "test",
      "org.scalatestplus"         %% "play"                             % "1.2.0"             % "test"
    ),
    parallelExecution in test := false,
    play.PlayImport.PlayKeys.routesImport ++= commonImports ++ Seq(
      "controllers.support.CustomBinders._"
    ),
    TwirlKeys.templateImports ++= commonImports
  )

lazy val scalikejdbcVersion = "2.2.+"
lazy val scalikejdbcPlayVersion = "2.3.+"
lazy val h2Version = "1.4.+"
lazy val playVersion = play.core.PlayVersion.current
lazy val commonImports = Seq(
  "models.aggregates._",
  "models.aggregates.company.Company",
  "models.aggregates.programmer.Programmer",
  "models.aggregates.skill.Skill"
)
