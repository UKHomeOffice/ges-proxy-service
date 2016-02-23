import io.gatling.sbt.GatlingPlugin
import sbt.Keys._
import sbt._
import spray.revolver.RevolverPlugin._

object Build extends Build {
  val moduleName = "ges-proxy-service"

  lazy val proxy = Project(id = moduleName, base = file(".")).enablePlugins(GatlingPlugin)
    .configs(IntegrationTest)
    .settings(Revolver.settings)
    .settings(Defaults.itSettings: _*)
    .settings(
      name := moduleName,
      organization := "uk.gov.homeoffice",
      version := "1.2.0-SNAPSHOT",
      scalaVersion := "2.11.7",
      scalacOptions ++= Seq(
        "-feature",
        "-language:implicitConversions",
        "-language:higherKinds",
        "-language:existentials",
        "-language:reflectiveCalls",
        "-language:postfixOps",
        "-Yrangepos",
        "-Yrepl-sync"),
      ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) },
      resolvers ++= Seq(
        "Artifactory Snapshot Realm" at "http://artifactory.registered-traveller.homeoffice.gov.uk/artifactory/libs-snapshot-local/",
        "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
        "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
        "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
        "Kamon Repository" at "http://repo.kamon.io"),
      libraryDependencies ++= Seq(
        "com.typesafe.akka" %% "akka-slf4j" % "2.4.0" withSources()
      ),
      libraryDependencies ++= Seq(
        "com.typesafe.akka" %% "akka-testkit" % "2.3.12" % "test, it" withSources(),
        "io.spray" %% "spray-testkit" % "1.3.3" % "test, it" withSources() excludeAll ExclusionRule(organization = "org.specs2"),
        "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.1.7" % IntegrationTest withSources(),
        "io.gatling" % "gatling-test-framework" % "2.1.7" % IntegrationTest withSources()
      )
    )
    .settings(run := (run in Runtime).evaluated) // Required to stop Gatling plugin overriding the default "run".

  val ioPath = "../rtp-io-lib"
  val akkaPath = "../rtp-akka-lib"
  val testPath = "../rtp-test-lib"
  val rtpProxyPath = "../rtp-proxy-lib"

  val root = if (file(ioPath).exists && sys.props.get("jenkins").isEmpty) {
    println("=============")
    println("Build Locally")
    println("=============")

    val io = ProjectRef(file(ioPath), "rtp-io-lib")
    val akka = ProjectRef(file(akkaPath), "rtp-akka-lib")
    val test = ProjectRef(file(testPath), "rtp-test-lib")
    val rtpProxy = ProjectRef(file(rtpProxyPath), "rtp-proxy-lib")

    proxy.dependsOn(io % "test->test;compile->compile")
         .dependsOn(akka % "test->test;compile->compile")
         .dependsOn(test % "test->test;compile->compile")
         .dependsOn(rtpProxy % "test->test;compile->compile")
  } else {
    println("================")
    println("Build on Jenkins")
    println("================")

    proxy.settings(
      libraryDependencies ++= Seq(
        "uk.gov.homeoffice" %% "rtp-io-lib" % "1.2.0-SNAPSHOT" withSources(),
        "uk.gov.homeoffice" %% "rtp-io-lib" % "1.2.0-SNAPSHOT" % Test classifier "tests" withSources(),
        "uk.gov.homeoffice" %% "rtp-akka-lib" % "1.5.0-SNAPSHOT" withSources(),
        "uk.gov.homeoffice" %% "rtp-akka-lib" % "1.5.0-SNAPSHOT" % Test classifier "tests" withSources() excludeAll ExclusionRule(organization = "org.specs2"),
        "uk.gov.homeoffice" %% "rtp-test-lib" % "1.2.0-SNAPSHOT" withSources(),
        "uk.gov.homeoffice" %% "rtp-test-lib" % "1.2.0-SNAPSHOT" % Test classifier "tests" withSources(),
        "uk.gov.homeoffice" %% "rtp-proxy-lib" % "1.2.0-SNAPSHOT" withSources(),
        "uk.gov.homeoffice" %% "rtp-proxy-lib" % "1.2.0-SNAPSHOT" % Test classifier "tests" withSources()
      )
    )
  }
}