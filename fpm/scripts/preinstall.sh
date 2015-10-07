#!/bin/bash

/usr/bin/getent group gespxsrv > /dev/null || /usr/sbin/groupadd gespxsrv
/usr/bin/getent passwd gespxsrv > /dev/null || /usr/sbin/useradd -r -g gespxsrv -s /bin/bash -c 'gespxsrv user' gespxsrv
