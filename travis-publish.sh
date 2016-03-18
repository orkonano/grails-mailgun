#!/bin/bash

vesion="$(grep 'mailgunPluginVersion =' build.gradle)"
vesion="${vesion#*=}"
vesion="${vesion//[[:blank:]\'\"]/}"

tagVersion="v$version"

echo "Publishing $vesion version"

EXIT_STATUS=0

if [[ $TRAVIS_REPO_SLUG == "orkonano/grails-mailgun" && $TRAVIS_PULL_REQUEST == 'false' && $TRAVIS_TAG == $tagVersion && $EXIT_STATUS -eq 0 ]]; then
    ./gradlew bintrayUpload
else
    if [[ $TRAVIS_REPO_SLUG == "orkonano/grails-mailgun" && $TRAVIS_PULL_REQUEST == 'false' && $TRAVIS_TAG != $tagVersion && $EXIT_STATUS -eq 0 ]]; then
        echo "Not publish because $tagVersion is different to Travis Tag  $TRAVIS_TAG"
        EXIT_STATUS=1
    else
        EXIT_STATUS=0
    fi
fi

exit $EXIT_STATUS
