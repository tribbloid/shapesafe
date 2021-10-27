#!/usr/bin/env bash

CRDIR="$(
  cd "$(dirname "$0")" || exit
  pwd
)"

"${CRDIR}"/.update-submodules.sh && \
echo "[COMPILING]" && \
"${CRDIR}"/make-all.sh "${@}" && \
"${CRDIR}"/demo-make-all.sh && \
echo "[RUNNING TESTS]" && \
"${CRDIR}"/test.sh "${@}"
