name := "ScalaPlayground"

version := "1.0"

scalaVersion := "2.11.2"

// you may also want to add the typesafe repository
resolvers += "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23"
)
    