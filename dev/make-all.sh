#!/usr/bin/env bash

FWDIR="$(
  cd "$(dirname "$0")"/.. || exit
  pwd
)"
DATE=$(date --iso-8601=second)

mkdir -p ${FWDIR}/logs
mkdir -p ${FWDIR}/logs/dependencyTree

${FWDIR}/gradlew -q dependencyTree "${@}" > ${FWDIR}/logs/dependencyTree/"$DATE".log

${FWDIR}/gradlew clean testClasses publishToMavenLocal "${@}"

#CD ${FWDIR}/shapeshape-demo/
#
#sbt clean compile //  TODO: generates project dir at wrong location