lazy val akkaHttpVersion = "10.2.0"
lazy val akkaVersion    = "2.6.9"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.skirdey",
      scalaVersion    := "2.13.3"
    )),
    name := "djl-akka-http",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"    % "logback-classic"           % "1.2.3",

      "ai.djl" % "api" % "0.7.0",
      "ai.djl.tensorflow" % "tensorflow-native-auto" % "2.3.0",
      "ai.djl.tensorflow" % "tensorflow-model-zoo" % "0.7.0",
      "ai.djl.tensorflow" % "tensorflow-engine" % "0.7.0",

"com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"                % "3.0.8"         % Test
    )
  )
