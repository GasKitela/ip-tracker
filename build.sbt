name := "ml-ip-tracker"
 
version := "1.0" 
      
lazy val mliptracker = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.10"

mainClass in assembly := Some("play.core.server.ProdServerStart")

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

libraryDependencies ++= List(
  "com.softwaremill.sttp" %% "core" % "1.7.2",
  "com.softwaremill.sttp" %% "akka-http-backend" % "1.6.7",
  "org.json4s" %% "json4s-native" % "3.6.0"
  /*"org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.scalamock" %% "scalamock" % "4.0.0" % "test",
  "org.mockito" % "mockito-core" % "2.18.3" % "test"*/
)

libraryDependencies += play.sbt.PlayImport.guice

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.9.0"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test, guice )

      