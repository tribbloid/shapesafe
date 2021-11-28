#!/usr/bin/env bash

CRDIR="$(
  cd "$(dirname "$0")" || exit
  pwd
)"

ARGS=("-PscalaVersion=2.13.7" "-PsplainVersion=" "${@}")

exec "${CRDIR}"/.CI.sh "${ARGS[@]}"