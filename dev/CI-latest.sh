#!/usr/bin/env bash

CRDIR="$(
  cd "$(dirname "$0")" || exit
  pwd
)"

ARGS=("-PsplainVersion=" "${@}")

exec "${CRDIR}"/CI/main.sh "${ARGS[@]}"