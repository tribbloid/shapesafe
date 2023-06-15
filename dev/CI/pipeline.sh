#!/usr/bin/env bash

FWDIR="$(
  cd "$(dirname "$0")"/.. || exit
  pwd
)"

echo "[COMPILING]" && \
"${FWDIR}"/make-all.sh "${@}" && \
echo "[COMPILING DEMO]" && \
"${FWDIR}"/../shapesafe-demo/dev/make-all.sh "${@}" && \
echo "[RUNNING TESTS]" && \
"${FWDIR}"/test.sh "${@}"
