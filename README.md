# PSI Probe

[![Build Status](https://travis-ci.org/psi-probe/psi-probe.svg?branch=master)](https://travis-ci.org/psi-probe/psi-probe)
[![Coverage Status](https://coveralls.io/repos/psi-probe/psi-probe/badge.svg?branch=master&service=github)](https://coveralls.io/github/psi-probe/psi-probe?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/569bd2562025a60031000001/badge.svg?style=flat)](https://www.versioneye.com/user/projects/569bd2562025a60031000001)
[![GPLv2 License](http://img.shields.io/badge/license-GPLv2-green.svg)](http://www.gnu.org/licenses/old-licenses/gpl-2.0.html)
[![Project Stats](https://www.openhub.net/p/psi-probe/widgets/project_thin_badge.gif)](https://www.openhub.net/p/psi-probe)

![psi-probe](src/site/resources/images/psi-probe-banner.jpg)

[site](https://psi-probe.github.io/psi-probe/)

## Contributing ##

See [CONTRIBUTING.md](CONTRIBUTING.md) for info on working on PSI Probe and sending patches.

## Latest Snapshot ##

Please download latest snapshots from [here] (https://oss.sonatype.org/content/repositories/snapshots/com/github/psi-probe/)

## Building from Source ##

1.	**Clone PSI Probe's git repository.**

	*Note: If you plan to contribute to PSI Probe, you should create your own fork on GitHub first and clone that.  Otherwise, follow these steps to build the latest version of PSI Probe for yourself.*

	Execute the following command:

		git clone https://github.com/psi-probe/psi-probe

	This will create directory called `psi-probe`. Subsequent steps will refer to this as "your PSI Probe base directory."

2.  Minimum JDK version required to run build is JDK7.  Project still targets JDK6.  The raise to JDK7 is a direct result of early Tomcat 9 support and maven plugins moving to JDK7.
	
3.	**Download and install Maven 3.**

	You may download it from the [Apache Maven website](http://maven.apache.org/download.cgi).

4.	**Run Maven.**

	Execute the following command from your PSI Probe base directory:

		mvn package

	This will create a deployable file at `web/target/probe.war`.

## User Groups

* [Announcements](http://groups.google.com/group/psi-probe/)
* [Discussions](http://groups.google.com/group/psi-probe-discuss/)
* [Slack](https://psi-probe.slack.com/)