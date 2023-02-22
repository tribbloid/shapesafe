#!/usr/bin/env bash

FWDIR="$(
  cd "$(dirname "$0")"/.. || exit
  pwd
)"

${FWDIR}/gradlew wrapper --gradle-version=8.0.1

${FWDIR}/gradlew dependencyUpdates --warning-mode all "$@"
