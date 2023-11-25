# PSI Probe

[![Java CI](https://github.com/psi-probe/psi-probe/workflows/Java%20CI/badge.svg)](https://github.com/psi-probe/psi-probe/actions?query=workflow%3A%22Java+CI%22)
[![Coverity Scan Build Status](https://scan.coverity.com/projects/28366/badge.svg)](https://scan.coverity.com/projects/28366)
[![Coverage Status](https://coveralls.io/repos/github/psi-probe/psi-probe/badge.svg?branch=master)](https://coveralls.io/github/psi-probe/psi-probe?branch=master)
[![Maven central](https://img.shields.io/maven-central/v/com.github.psi-probe/psi-probe-web)](https://maven-badges.herokuapp.com/maven-central/com.github.psi-probe/psi-probe-web)
[![releases](https://img.shields.io/github/downloads/psi-probe/psi-probe/psi-probe-4.0.0/total)](https://github.com/psi-probe/psi-probe/releases/download/psi-probe-4.0.0/probe.war)
[![GPLv2 License](https://img.shields.io/badge/license-GPLv2-green.svg)](https://www.gnu.org/licenses/old-licenses/gpl-2.0.html)
[![Project Stats](https://www.openhub.net/p/psi-probe/widgets/project_thin_badge.gif)](https://www.openhub.net/p/psi-probe)
[![Github All Releases](https://img.shields.io/github/downloads/psi-probe/psi-probe/total.svg)]()

![psi-probe](src/site/resources/images/psi-probe-banner.jpg)

## Sites ##

* [site](https://psi-probe.github.io/psi-probe/)
* [sonarqube](https://sonarqube.com/dashboard/index?id=com.github.psi-probe:psi-probe)

## Contributing ##

See [CONTRIBUTING.md](CONTRIBUTING.md) for info on working on PSI Probe and sending patches.

## Latest Release via Github Releases ##

Please download latest probe.war from [here](https://github.com/psi-probe/psi-probe/releases/download/psi-probe-3.7.3/probe.war)

## Latest Release via Maven Central ##

Please download latest psi-probe-web.war release from [here](https://oss.sonatype.org/content/repositories/releases/com/github/psi-probe/psi-probe-web/)

You can rename 'psi-probe-web.war' to the traditional 'probe.war' or other name as you see fit.

## Latest Snapshot via Maven Central ##

Please download latest psi-probe-web.war snapshots from [here](https://oss.sonatype.org/content/repositories/snapshots/com/github/psi-probe/psi-probe-web/)

You can rename 'psi-probe-web.war' to the traditional 'probe.war' or other name as you see fit.

## Building from Source ##

1.  **Clone PSI Probe's git repository.**

    *Note: If you plan to contribute to PSI Probe, you should create your own fork on GitHub first and clone that.  Otherwise, follow these steps to build the latest version of PSI Probe for yourself.*

    Execute the following command:

        git clone https://github.com/psi-probe/psi-probe

    This will create directory called `psi-probe`. Subsequent steps will refer to this as "your PSI Probe base directory."

2.  Minimum JDK version required to build is JDK 11 and run is JDK 8.

3.  **Download and install Maven 3.9.2 or better

    You may download it from the [Apache Maven website](https://maven.apache.org/download.cgi).

4.  **Run Maven.**

    Execute the following command from your PSI Probe base directory:

        mvn package

    This will create a deployable file at `web/target/probe.war`.

## Supported Tomcat Versions

Generally supported versions for third party tomcat providers align with their support but earlier versions may still work.  It is advisable in every case to use only supported tomcat releases per specific vendor.  Our support will only be against non CVE releases.

* Tomcat 8.5 Series (Notice: We will drop support for tomcat 8.5 after tomcat officially archives it shortly after 3/31/2024 and we will complete a final release before that is done)

    - Tomcat 8.5.86 to 8.5.96 (Stated end of life will be 3/31/2024, considering using tomcat 9 or better)
    - TomEE 7.1 Discontinued, suggest to use TomEE 8.0 or better.  TomEE no longer supports tomcat 8.5
    - NonStop(tm) Servlets For JavaServer Pages(tm) v8.5 (Based on Tomcat 8.5.78)
    - Pivotal tc 4.1.27 release with addon only (Based on Tomcat 8.5.94)

* Tomcat 9.0 Series

    - Tomcat 9.0.72 to 9.0.83
    - TomEE 8.0.16 (Based on Tomcat 9.0.82).  TomEE is ending support on December 31st 2023
    - NonStop(tm) never supported 9.0 as they changed direction but seem to have come back now for 10.x
    - Pivotal tc 4.1.27 release (Based on Tomcat 9.0.81)
    - Pivotal tc 5.0.6 release (Based on Tomcat 9.0.81)

* Tomcat 10.1 Series (pending)

    - Tomcat 10.1.6 to 10.1.16
    - TomEE 9.1.1 (Based on Tomcat 10.0.27)
    - NonStop(tm) Servlets For JavaServer Pages(tm) v10.1 (Based on Tomcat 10.1.7)
    - Pivotal tc 5.0.6 release (Based on Tomcat 10.1.14

* Tomcat 11.0 Series (pending)

    - Tomcat 11.0.0.M3 to 11.0.0.M14

## User Groups

* [Announcements](https://groups.google.com/forum/#!forum/psi-probe)
* [Discussions](https://groups.google.com/forum/#!forum/psi-probe-discuss)
* [Slack](https://psi-probe.slack.com/)

## FAQ

* [Adding Additional Loggers](https://github.com/psi-probe/psi-probe/wiki/Adding-Additional-Loggers)
* [Forcing tomcat version](https://github.com/psi-probe/psi-probe/wiki/Troubleshooting#error-on-first-request)
