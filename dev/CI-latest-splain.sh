#!/usr/bin/env bash

CRDIR="$(
  cd "$(dirname "$0")" || exit
  pwd
)"

ARGS=("-Psplain.version=1.1.0-SNAPSHOT" "${@}")

exec "${CRDIR}"/.CI.sh "${ARGS[@]}"
