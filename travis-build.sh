#!/bin/bash

if [[ -z $TRAVIS_TAG ]]; then
    ./gradlew test
fi

exit $?