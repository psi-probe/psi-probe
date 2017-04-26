#!/bin/bash
#
# Licensed under the GPL License. You may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
#
# THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
# WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
# PURPOSE.
#

# Get Commit Message
commit_message=$(git log --format=%B -n 1)
echo "Current commit detected: ${commit_message}"

# We build for several JDKs on Travis.
# Some actions, like analyzing the code (Coveralls) and uploading
# artifacts on a Maven repository, should only be made for one version.
 
# If the version is 1.8, then perform the following actions.
# 1. Upload artifacts to Sonatype.
#    a. Use -q option to only display Maven errors and warnings.
#    b. Use --settings to force the usage of our "settings.xml" file.
# 2. Notify Coveralls.
# 3. Deploy site
#    a. Use -q option to only display Maven errors and warnings.

if [ $TRAVIS_REPO_SLUG == "psi-probe/psi-probe" ] && [ $TRAVIS_PULL_REQUEST == "false" ] && [ $TRAVIS_BRANCH == "master" ] && [[ "$commit_message" != *"[maven-release-plugin]"* ]]; then

  if [ $TRAVIS_JDK_VERSION == "oraclejdk8" ]; then
    # Deploy sonatype
    ./mvnw deploy -q --settings ./travis/settings.xml
    echo -e "Successfully deployed SNAPSHOT artifacts to Sonatype under Travis job ${TRAVIS_JOB_NUMBER}"

	# Send coverage to coveralls
    ./mvnw test jacoco:report coveralls:report -q --settings ./travis/settings.xml
    echo -e "Successfully ran coveralls under Travis job ${TRAVIS_JOB_NUMBER}"

    # Deploy to site
    # Cannot currently run site this way
	# ./mvnw site site:deploy -q --settings ./travis/settings.xml
	# echo -e "Successfully deploy site under Travis job ${TRAVIS_JOB_NUMBER}"
  fi

elif [ $TRAVIS_REPO_SLUG == "psi-probe/psi-probe" ] && [ $TRAVIS_PULL_REQUEST != "false" ]; then

  if [ $TRAVIS_JDK_VERSION == "oraclejdk8" ]; then
	# Send coverage to coveralls
	./mvnw clean test jacoco:report coveralls:report -q --settings ./travis/settings.xml
	echo -e "Successfully ran coveralls under Travis job ${TRAVIS_JOB_NUMBER}"
  fi

else
  echo "Travis build skipped"
fi
