# PSI Probe

[![Java CI](https://github.com/psi-probe/psi-probe/workflows/Java%20CI/badge.svg)](https://github.com/psi-probe/psi-probe/actions?query=workflow%3A%22Java+CI%22)
[![Coverity](https://scan.coverity.com/projects/28366/badge.svg)](https://scan.coverity.com/projects/28366)
[![Coveralls](https://coveralls.io/repos/github/psi-probe/psi-probe/badge.svg?branch=master)](https://coveralls.io/github/psi-probe/psi-probe?branch=master)
[![Known Vulnerabilities](https://snyk.io/test/github/psi-probe/psi-probe/badge.svg)](https://snyk.io/test/github/psi-probe/psi-probe)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.psi-probe/psi-probe-web)](https://maven-badges.herokuapp.com/maven-central/com.github.psi-probe/psi-probe-web)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/https/central.sonatype.com/com.github.psi-probe/psi-probe-web.svg)](https://central.sonatype.com/repository/maven-snapshots/org/psi-probe/psi-probe-web/)
[![Renovate enabled](https://img.shields.io/badge/renovate-enabled-brightgreen.svg)](https://renovatebot.com/)
[![Releases](https://img.shields.io/github/downloads/psi-probe/psi-probe/psi-probe-5.2.1/total)](https://github.com/psi-probe/psi-probe/releases/download/psi-probe-5.2.1/probe.war)
[![GPLv2 License](https://img.shields.io/badge/license-GPLv2-green.svg)](https://www.gnu.org/licenses/old-licenses/gpl-2.0.html)
[![Project Stats](https://www.openhub.net/p/psi-probe/widgets/project_thin_badge.gif)](https://www.openhub.net/p/psi-probe)
[![Github All Releases](https://img.shields.io/github/downloads/psi-probe/psi-probe/total.svg)]()
[![Reproducible Builds](https://img.shields.io/endpoint?url=https://raw.githubusercontent.com/jvm-repo-rebuild/reproducible-central/master/content/com/github/psi-probe/badge.json)](https://github.com/jvm-repo-rebuild/reproducible-central/blob/master/content/com/github/psi-probe/README.md)

![psi-probe](src/site/resources/images/psi-probe-banner.jpg)

## Sites ##

* [site](https://psi-probe.github.io/psi-probe/)
* [sonarqube](https://sonarcloud.io/project/overview?id=psi-probe_psi-probe)

## Contributing ##

See [CONTRIBUTING.md](CONTRIBUTING.md) for info on working on PSI Probe and sending patches.

## Latest Release via Github Releases ##

Please download latest probe.war from [here](https://github.com/psi-probe/psi-probe/releases/download/psi-probe-5.2.1/probe.war)

## Latest Release via Maven Central ##

Please download latest psi-probe-web.war release from [here](https://oss.sonatype.org/content/repositories/releases/com/github/psi-probe/psi-probe-web/)

You can rename 'psi-probe-web.war' to the traditional 'probe.war' or other name as you see fit.

## Latest Snapshot via Maven Central ##

Please download latest psi-probe-web.war snapshots from [here](https://central.sonatype.com/service/rest/repository/browse/maven-snapshots/com/github/psi-probe/psi-probe-web/)

You can rename 'psi-probe-web.war' to the traditional 'probe.war' or other name as you see fit.

## Building from Source ##

1.  **Clone PSI Probe's git repository.**

    *Note: If you plan to contribute to PSI Probe, you should create your own fork on GitHub first and clone that.  Otherwise, follow these steps to build the latest version of PSI Probe for yourself.*

    Execute the following command:

        git clone https://github.com/psi-probe/psi-probe

    This will create directory called `psi-probe`. Subsequent steps will refer to this as "your PSI Probe base directory."

2.  Minimum JDK version required to build is JDK 17 and run is JDK 17.

3.  **Download and install Maven 3.9.9 or better

    You may download it from the [Apache Maven website](https://maven.apache.org/download.cgi).

4.  **Run Maven.**

    Execute the following command from your PSI Probe base directory:

        mvn package

    This will create a deployable file at `web/target/probe.war`.

## Supported Tomcat Versions

Generally supported versions for third party tomcat providers align with their support but earlier versions may still work.  It is advisable in every case to use only supported tomcat releases per specific vendor.  Our support will only be against non CVE releases.

* Tomcat 9.0 Series

    - Requires java 11 or better
    - Use psi probe 4.x series for javax namespace
    - Spring is no longer receiving updates so vulnerabilities with spring will not be addressed.  If you have a subscription to spring, manually patch the jars.
    - Tomcat 9.0.72 to 9.0.105
    - TomEE 8.0.16 (Based on Tomcat 9.0.82).  TomEE 8.0 ended support on December 31st 2023
    - NonStop(tm) never supported 9.0 as they changed direction but seem to have come back now for 10.1
    - Vmware Tanzu tc 4.1.44 release (Based on Tomcat 9.0.102)
    - Vmware Tanzu tc 5.0.23 release (Based on Tomcat 9.0.104)

* Tomcat 10.1 Series

    - Requires java 17 or better
    - Use psi probe 5.x series for jakarta namespace
    - Tomcat 10.1.6 to 10.1.41
    - TomEE 10.0.0 to 10.0.1 (Based on Tomcat 10.1.39).
    - NonStop(tm) Servlets For JavaServer Pages(tm) v10.1 (Based on Tomcat 10.1.7)
    - Vmware Tanzu tc 10.1.40.A release (Based on Tomcat 10.1.40)

* Tomcat 11.0 Series

    - Requires java 17 or better
    - Use psi probe 5.x series for jakarta namespace
    - Tomcat 11.0.0.M3 to 11.0.7
    - TomEE - no support yet for tomcat 11.0
    - NonStop(tm) Servlets For JavaServer Pages(tm) - no support yet for tomcat 11.0
    - Vmware Tanzu tc 11.0.6.A release (Based on Tomcat 11.0.6)

## Precondition

PSI Probe uses deep reflection to access data. Take care your tomcat configuration considers this.
You can do this by extending the JDK_JAVA_OPTIONS variable:

        ...
        --add-opens=java.base/java.lang=ALL-UNNAMED \
        --add-opens=java.base/java.lang.reflect=ALL-UNNAMED \
        --add-opens=java.base/java.io=ALL-UNNAMED \
        --add-opens=java.base/java.util=ALL-UNNAMED \
        --add-opens=java.base/java.util.concurrent=ALL-UNNAMED \
        --add-opens=java.base/sun.util.locale=ALL-UNNAMED \
        --add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED \
        ...

## User Groups

* [Announcements](https://groups.google.com/forum/#!forum/psi-probe)
* [Discussions](https://groups.google.com/forum/#!forum/psi-probe-discuss)
* [Slack](https://psi-probe.slack.com/)

## FAQ

* [Adding Additional Loggers](https://github.com/psi-probe/psi-probe/wiki/Adding-Additional-Loggers)
* [Forcing tomcat version](https://github.com/psi-probe/psi-probe/wiki/Troubleshooting#error-on-first-request)
