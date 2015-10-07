#!/bin/sh
echo "[build.sh] Building with Activator!"

# Ensure working directory is local to this script
cd "$(dirname "$0")"

./activator test

if [ $? -ne 0 ]; then
  echo "[build.sh] failure"
  exit 1
else
  echo "[build.sh] done"
fi
