#!/usr/bin/env bash

git config --global credential.helper store
git pull --rebase --autostash

git submodule foreach git fetch
git submodule update --init --recursive

exec build-tools/make-all.sh "$@"
