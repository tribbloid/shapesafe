#!/usr/bin/env bash

FWDIR="$(
  cd "$(dirname "$0")"/.. || exit
  pwd
)"
DATE=$(date --iso-8601=second)

echo "[SUBMODULE(S)]" && \
cd splain && \
sbt publishM2 && \
cd ..

mkdir -p ${FWDIR}/logs

${FWDIR}/gradlew -q dependencyTree "${@}" >${FWDIR}/logs/dependencyTree_"$DATE".out

${FWDIR}/gradlew clean testClasses publishToMavenLocal "${@}"

#CD ${FWDIR}/shapeshape-demo/
#
#sbt clean compile //  TODO: generates project dir at wrong location