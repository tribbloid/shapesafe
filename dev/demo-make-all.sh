#!/usr/bin/env bash

FWDIR="$(
  cd "$(dirname "$0")"/.. || exit
  pwd
)"

echo "[DEMO]" && \
cd ${FWDIR}/shapesafe-demo && \
sbt clean package && \
cd ..