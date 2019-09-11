#!/usr/bin/env sh

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

./gradlew ::copyKotlinLibs
./gradlew war

cp "${DIR}/kotlin.web.demo.backend/build/libs/WebDemoBackend.war" "${DIR}/docker/allin1/apps/WebDemoBackend.war"
cp "${DIR}/kotlin.web.demo.server/build/libs/WebDemoWar.war" "${DIR}/docker/allin1/apps/WebDemoWar.war"
