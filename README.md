# PSI Probe

[![Build Status](https://travis-ci.org/psi-probe/psi-probe.svg?branch=master)](https://travis-ci.org/psi-probe/psi-probe)
[![Coverage Status](https://coveralls.io/repos/psi-probe/psi-probe/badge.svg?branch=master&service=github)](https://coveralls.io/github/psi-probe/psi-probe?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/569bd2562025a60031000001/badge.svg?style=flat)](https://www.versioneye.com/user/projects/569bd2562025a60031000001)
[![GPLv2 License](http://img.shields.io/badge/license-GPLv2-green.svg)](http://www.gnu.org/licenses/old-licenses/gpl-2.0.html)
[![Project Stats](https://www.openhub.net/p/psi-probe/widgets/project_thin_badge.gif)](https://www.openhub.net/p/psi-probe)

![psi-probe](src/site/resources/images/psi-probe-banner.jpg)

## Contributions ##

Please follow [GitHub Flow](https://guides.github.com/introduction/flow/), with the following additions:

*	**Ensure an [issue](//github.com/psi-probe/psi-probe/issues) exists** before you begin work. If not, file one.
	*	Your change should provide obvious material benefit to the project's users and/or developers. If in doubt, an issue serves as place to discuss your suggestion before you begin work.
	*	Issues power the release changelogs, so your change will be made known to users after it's done.
*	**One PR per issue.** Include the issue number in your commit(s) and PR so that merging it will close the issue.
*	**One issue per PR.** Keep changes minimal, and keep the scope narrow. A small change is easier to review.
	*	Avoid making formatting changes alongside functionality changes. This is a recipe for conflicts.
	*	Avoid bumping version numbers or correcting spelling errors along with your changes unless they're necessary.
	*	Feel free to make these sorts of corrections in a separate PR, though!
*   **Ensure commits are clean.** Please rebase before submitting PR. This will save time and ensure quicker merge. Use steps similar to following.
    *   git pull --rebase upstream master
	*   git push origin +master
	

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
