#!/bin/bash

if [ $1 = "remove" ]; then
  echo "deleting user data on uninstall"
  /usr/bin/getent passwd gespxsrv > /dev/null && /usr/sbin/userdel geintsrv || /bin/true
  /usr/bin/getent group gespxsrv > /dev/null && /usr/sbin/groupdel geintsrv || /bin/true
fi

exit 0
