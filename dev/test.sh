#!/usr/bin/env bash

FWDIR="$(
  cd "$(dirname "$0")"/.. || exit
  pwd
)"

# parallel disabled for interferring with benchmarks
${FWDIR}/gradlew test "-Dorg.gradle.parallel=false" "${@}"
