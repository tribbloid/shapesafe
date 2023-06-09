#!/usr/bin/env bash

CRDIR="$(
  cd "$(dirname "$0")" || exit
  pwd
)"

ARGS=("-Psplain.version=1.1.0-SNAPSHOT" "-Pscala.version=2.13.10" "${@}")

exec "${CRDIR}"/.CI.sh "${ARGS[@]}"
