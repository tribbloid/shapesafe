#!/usr/bin/env bash

CRDIR="$(
  cd "$(dirname "$0")" || exit
  pwd
)"

git submodule foreach git fetch
git submodule update --init --recursive

PP=("-PscalaVersion=2.12.13" "${@}")

echo "[COMPILING]" && \
"${CRDIR}"/make-all.sh "${PP}" && \
echo "[RUNNING TESTS]" && \
"${CRDIR}"/test.sh "${PP}"