package uk.gov.homeoffice.ges.proxy

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._

class GESProxyCheckSimulation extends Simulation {
  val httpProtocol = http
    .baseURL("http://localhost:9300")
    .acceptHeader("application/json;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn = scenario("GES Proxy Check Simulation").repeat(10) {
    exec(http("proxy-server GET")
      .get("/proxy-server")
      .check(status.is(200)))
      .pause(2 seconds)
  }

  setUp(
    scn.inject(rampUsers(10) over (10 seconds))
  ).protocols(httpProtocol)
}