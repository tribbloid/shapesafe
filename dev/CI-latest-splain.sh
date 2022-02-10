#!/usr/bin/env bash

CRDIR="$(
  cd "$(dirname "$0")" || exit
  pwd
)"

ARGS=("-PsplainVersion=1.0.0" "${@}")

exec "${CRDIR}"/.CI.sh "${ARGS[@]}"
