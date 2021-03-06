GES Proxy Service
=================

Scala Spray service to proxy request/response between client and server

Application built with the following (main) technologies:

- Scala

- SBT

- Akka

- Spray

Introduction
------------
TODO

Application
-----------
The application is configured as per any Akka application, where the default configuration file is "application.conf".
This default file can be overridden with other "conf" files and then given to the application upon boot with the following example Java option:
```bash
-Dconfig.file=test-classes/application.test.conf
```

Individual configuration properties can be overridden again by Java options e.g. to override which Mongodb to connect (if Mongo required configuring):
```bash
-Dmongo.db=some-other-mongo
```

where this overrides the default in application.conf.

Booting the application (uk.gov.homeoffice.rtp.proxy.Boot) exposes a RESTful API to act as a proxy to the real API e.g. the following URL could be given in a browser, a browser's REST client plugin or curl:
```bash
http://localhost:9300/blah
```

this would get back some appropriate response for some existing API.

Build and Deploy
----------------
The project is built with SBT. On a Mac (sorry everyone else) do:
```bash
brew install sbt
```

It is also a good idea to install Typesafe Activator (which sits on top of SBT) for when you need to create new projects - it also has some SBT extras, so running an application with Activator instead of SBT can be useful. On Mac do:
```bash
brew install typesafe-activator
```

To compile:
```bash
sbt compile
```

or
```bash
activator compile
```

To run the specs:
```bash
sbt test
```

Note: Do not turn off the CONSOLE logging in test/resources/logback.xml as there are Specs that rely on this

To actually run the application, first "assemble" it:
```bash
sbt assembly
```

This packages up an executable JAR - Note that "assembly" will first compile and test.

Then just run as any executable JAR, with any extra Java options for overriding configurations.

For example, to use a config file (other than the default application.conf) which is located on the file system (in this case in the boot directory)
```bash
java -Dconfig.file=test-classes/my-application.conf -jar <jar name>.jar
```

Note that the log configuration file could also be included e.g.
```bash
-Dlogback.configurationFile=path/to/my-logback.xml
```

So a more indepth startup with sbt itself could be:
```bash
sbt run -Dconfig.file=target/scala-2.11/test-classes/application.test.conf -Dlogback.configurationFile=target/scala-2.11/test-classes/logback.test.xml
```

And other examples:

booting from project root:
```bash
java -Dspray.can.server.port=8080 -jar target/scala-2.11/<jar name>.jar
```

and running from directory of the executable JAR using a config that is within said JAR:
```bash
java -Dconfig.resource=application.uat.conf -jar <jar name>.jar
```

Finally you can perform a quick test of the application by calling one of the monitor API e.g. making a cURL call to the application:
```bash
curl http://localhost:9300/proxy-server 
```

and to check the connection between the proxy and SOAP service:
```bash
curl -X GET localhost:9300/soapservice/GesService?wsdl
```

SBT - Revolver
--------------
sbt-revolver is a plugin for SBT enabling a super-fast development turnaround for your Scala applications:

See https://github.com/spray/sbt-revolver

For development, you can use ~re-start to go into "triggered restart" mode.
Your application starts up and SBT watches for changes in your source (or resource) files.
If a change is detected SBT recompiles the required classes and sbt-revolver automatically restarts your application. 
When you press &lt;ENTER&gt; SBT leaves "triggered restart" and returns to the normal prompt keeping your application running.

Gatling - Performance (Integration) Testing
-------------------------------------------
Performance tests are under src/it, and test reports are written to the "target" directory.

To execute Gatling performance integration tests from withing SBT:
```bash
gatling-it:test
```

Note that as Gatling tests are integration tests, then a ges-proxy-service must be running.
When running the proxy locally, the global-entry-stub should be started, where both applications run in either SSL or non-SSL mode.