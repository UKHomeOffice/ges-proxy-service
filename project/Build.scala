import io.gatling.sbt.GatlingPlugin
import sbt.Keys._
import sbt._
import spray.revolver.RevolverPlugin._

object Build extends Build {
  val moduleName = "ges-proxy-service"

  val root = Project(id = moduleName, base = file(".")).enablePlugins(GatlingPlugin)
    .configs(IntegrationTest)
    .settings(Revolver.settings)
    .settings(Defaults.itSettings: _*)
    .settings(run := (run in Runtime).evaluated) // Required to stop Gatling plugin overriding the default "run".
    .settings(
      name := moduleName,
      organization := "uk.gov.homeoffice",
      version := "1.2.0-SNAPSHOT",
      scalaVersion := "2.11.8",
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
        "Kamon Repository" at "http://repo.kamon.io"
      )
    )
    .settings(libraryDependencies ++= {
      val `akka-version` = "2.4.0"
      val `gatling-version` = "2.1.7"
      val `rtp-io-lib-version` = "1.2.0-SNAPSHOT"
      val `rtp-test-lib-version` = "1.2.0-SNAPSHOT"
      val `rtp-akka-lib-version` = "1.5.0-SNAPSHOT"
      val `rtp-proxy-lib-version` = "1.2.0-SNAPSHOT"

      Seq(
        "com.typesafe.akka" %% "akka-slf4j" % `akka-version` withSources(),
        "uk.gov.homeoffice" %% "rtp-io-lib" % `rtp-io-lib-version` withSources(),
        "uk.gov.homeoffice" %% "rtp-test-lib" % `rtp-test-lib-version` withSources(),
        "uk.gov.homeoffice" %% "rtp-akka-lib" % `rtp-akka-lib-version` withSources(),
        "uk.gov.homeoffice" %% "rtp-proxy-lib" % "1.2.0-SNAPSHOT" withSources()
      ) ++ Seq(
        "com.typesafe.akka" %% "akka-testkit" % `akka-version` % "test, it" withSources(),
        "io.spray" %% "spray-testkit" % "1.3.3" % "test, it" withSources() excludeAll ExclusionRule(organization = "org.specs2"),
        "io.gatling.highcharts" % "gatling-charts-highcharts" % `gatling-version` % IntegrationTest withSources(),
        "io.gatling" % "gatling-test-framework" % `gatling-version` % IntegrationTest withSources(),
        "uk.gov.homeoffice" %% "rtp-io-lib" % `rtp-io-lib-version` % Test classifier "tests" withSources(),
        "uk.gov.homeoffice" %% "rtp-test-lib" % `rtp-test-lib-version` % Test classifier "tests" withSources(),
        "uk.gov.homeoffice" %% "rtp-akka-lib" % `rtp-akka-lib-version` % Test classifier "tests" withSources() excludeAll ExclusionRule(organization = "org.specs2"),
        "uk.gov.homeoffice" %% "rtp-proxy-lib" % `rtp-proxy-lib-version` % Test classifier "tests" withSources()
      )
    })
}