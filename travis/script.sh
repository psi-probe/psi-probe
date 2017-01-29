#!/bin/bash

if [ "${COVERITY_SCAN_BRANCH}" != 1 ]; then
    # Standard Build
    ./mvnw install -DskipTests=true -Dmaven.javadoc.skip=true -B -V
fi
