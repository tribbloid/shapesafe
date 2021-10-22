#!/usr/bin/env bash

CRDIR="$(
  cd "$(dirname "$0")" || exit
  pwd
)"

ARGS=("-PsplainVersion=1.0.0-SNAPSHOT" ${@})

exec "${CRDIR}"/.CI.sh ${ARGS}