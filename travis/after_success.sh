#!/bin/bash
#
# Licensed under the GPL License. You may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#   http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
#
# THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
# WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
# PURPOSE.
#


# Get Project Repo
upstream_repo=$(git config --get remote.origin.url 2>&1)
echo "Repo detected: ${upstream_repo}"
 
# Get the Java version.
# Java 1.5 will give 15.
# Java 1.6 will give 16.
# Java 1.7 will give 17.
# Java 1.8 will give 18.
VER=`java -version 2>&1 | sed 's/java version "\(.*\)\.\(.*\)\..*"/\1\2/; 1q'`
echo "Java detected: ${VER}"

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

if [ "$upstream_repo" == "https://github.com/psi-probe/psi-probe.git" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then
  if [ $VER == "18" ]; then
    # Send snapshots to sonatype
    mvn clean deploy -q --settings ./travis/settings.xml
    echo -e "Successfully deployed SNAPSHOT artifacts to Sonatype under Travis job ${TRAVIS_JOB_NUMBER}"

	# Send coverage to coveralls
    mvn clean test jacoco:report coveralls:report -q
    echo -e "Successfully ran coveralls under Travis job ${TRAVIS_JOB_NUMBER}"

	# various issues exist currently in building this so comment for now
	# mvn site site:deploy -q
	# echo -e "Successfully deploy site under Travis job ${TRAVIS_JOB_NUMBER}"
  fi
elif [ "$upstream_repo" == "https://github.com/psi-probe/psi-probe.git" ] && [ "$TRAVIS_PULL_REQUEST" == "true" ]; then
  if [ $VER == "18" ]; then
	# Send coverage to coveralls
	mvn clean jacoco:report coveralls:report -q
	echo -e "Successfully ran coveralls under Travis job ${TRAVIS_JOB_NUMBER}"
  fi
else
  echo "Travis build skipped"
fi
