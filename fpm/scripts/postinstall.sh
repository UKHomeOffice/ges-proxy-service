#!/bin/bash

/bin/chown -R gespxsrv:gespxsrv /usr/share/ges-proxy-service
/bin/chown -R gespxsrv:gespxsrv /var/log/ges-proxy-service
/bin/chown -R gespxsrv:gespxsrv /var/run/ges-proxy-service
/bin/chmod 755 /etc/init.d/ges-proxy-service
