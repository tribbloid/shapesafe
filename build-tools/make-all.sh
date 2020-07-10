#!/usr/bin/env bash

FWDIR="$(
  cd "$(dirname "$0")"/.. || exit
  pwd
)"
DATE=$(date --iso-8601=second)

mkdir -p ${FWDIR}/logs

${FWDIR}/gradlew -q dependencyTree "${@}" >${FWDIR}/logs/dependencyTree_"$DATE".out

${FWDIR}/gradlew clean testClasses "${@}"
