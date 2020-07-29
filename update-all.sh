#!/usr/bin/env bash

git config --global credential.helper store
git pull --rebase --autostash

#git submodule init
####  sometimes revision track from parent is broken, need to use update --remote
git submodule foreach git fetch
git submodule update --init --recursive

exec build-tools/make-all.sh "$@"
