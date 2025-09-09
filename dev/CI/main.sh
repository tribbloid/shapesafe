#!/usr/bin/env bash

CRDIR="$(
  cd "$(dirname "$0")" || exit
  pwd
)"

"${CRDIR}"/../update-submodules.sh && \
"${CRDIR}"/pipeline.sh "${@}"