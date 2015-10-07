#!/bin/sh

cd fpm
cp ../target/scala-2.11/ges-proxy-service-assembly*.jar build/usr/share/ges-proxy-service/ges-proxy-service.jar
fpm -x .git* --config-files etc/ges-proxy-service/application.conf --before-install scripts/preinstall.sh --after-install scripts/postinstall.sh --before-remove scripts/preuninstall.sh --after-remove scripts/postuninstall.sh -C build -t rpm -s dir -d java -n ges-proxy-service -v "$version-$BUILD_NUMBER" -a all .
