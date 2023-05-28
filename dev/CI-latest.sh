#!/usr/bin/env bash

CRDIR="$(
  cd "$(dirname "$0")" || exit
  pwd
)"

ARGS=("-Psplain.version=" "${@}")

exec "${CRDIR}"/.CI.sh "${ARGS[@]}"