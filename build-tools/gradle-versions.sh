#!/usr/bin/env bash

FWDIR="$(
  cd "$(dirname "$0")"/.. || exit
  pwd
)"

${FWDIR}/gradlew wrapper --gradle-version=6.8.2

${FWDIR}/gradlew dependencyUpdates
