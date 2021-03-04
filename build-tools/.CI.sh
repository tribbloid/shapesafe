#!/usr/bin/env bash

CRDIR="$(
  cd "$(dirname "$0")" || exit
  pwd
)"

git submodule foreach git fetch
git submodule update --init --recursive

echo "[COMPILING]" && \
"${CRDIR}"/make-all.sh "${@}" && \
echo "[RUNNING TESTS]" && \
"${CRDIR}"/test.sh "${@}"