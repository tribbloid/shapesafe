#!/usr/bin/env bash

CRDIR="$(
  cd "$(dirname "$0")" || exit
  pwd
)"

"${CRDIR}"/.update-submodules.sh && \
echo "[COMPILING]" && \
"${CRDIR}"/make-all.sh "${@}" && \
echo "[DEMO]" && \
cd ${FWDIR}/shapesafe-demo && \
sbt clean package && \
cd .. && \
echo "[RUNNING TESTS]" && \
"${CRDIR}"/test.sh "${@}"
