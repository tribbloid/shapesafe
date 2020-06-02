#!/usr/bin/env bash

FWDIR="$(
  cd "$(dirname "$0")"/..
  pwd
)"

${FWDIR}/gradlew test "${@}"
