package uk.gov.homeoffice.ges.proxy

import scala.util.{Success, Try}
import akka.actor.ActorSystem
import grizzled.slf4j.Logging
import uk.gov.homeoffice.configuration.HasConfig
import uk.gov.homeoffice.console.Console
import uk.gov.homeoffice.rtp.proxy._
import uk.gov.homeoffice.rtp.proxy.ssl.SSL._
import uk.gov.homeoffice.rtp.proxy.ssl.SSLProxying

object Boot extends App with HasConfig with Console with Logging {
  present("GES Proxy Service")

  val proxiedServer = ProxiedServer(config.getString("proxied.server.host"), config.getInt("proxied.server.port"))

  val server = Server(config.getString("spray.can.server.host"), config.getInt("spray.can.server.port"))

  implicit val system = ActorSystem(config.getString("spray.can.server.name"))

  sys.addShutdownHook {
    system.terminate()
  }

  Try(config getConfig "ssl") match {
    case Success(_) =>
      info("Booting as SSL proxy")
      SSLProxying(sslContext(config)).proxy(proxiedServer, server)

    case _ =>
      info("Booting proxy")
      Proxying().proxy(proxiedServer, server)
  }
}