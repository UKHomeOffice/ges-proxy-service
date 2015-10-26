package uk.gov.homeoffice.ges.proxy

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._

class AccessCodeForUSASimulation extends Simulation {
  val httpProtocol = http
    .baseURL("http://localhost:9300")
    .acceptHeader("text/xml")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn = scenario("Access Code for USA Simulation").repeat(100) {
    exec(http("Access Code for USA")
      .post("/soapservice/GesService")
      .body(RawFileBody("access-code-for-usa.xml"))
      .header("Content-Type", "text/xml;charset=UTF-8"))
  }

  setUp {
    scn.inject(rampUsers(10) over (10 seconds))
  } protocols httpProtocol
}
