
---


## Configure Eclipse for Maven and Web ##
  * Download [Eclipse](http://www.eclipse.org/) 3.5/3.6 and install the following plugins:
    * [m2eclipse](http://m2eclipse.sonatype.org/)
    * [wtp 2.0 plugin](http://www.eclipse.org/webtools/) (SpringSource STS or "Eclipse IDE for Java EE Developers" recommended)

## Check Out PSI Probe Source ##

**WARNING:** Do NOT checkout the project from svn directly! In this case, you get a single project layout. As there are multiple tomcat connectors with different versions of the catalina library, you will get errors displayed within Eclipse, as Eclipse only supports a common Classpath within a single project.

How to checkout the project as a M2 Eclipse Project:
  1. `File > Import > "Check out Maven Projects from SCM"`
  1. Select svn and give the SVN url (e.g. for trunk, use http://psi-probe.googlecode.com/svn/trunk/)
  1. With this, m2eclipse creates a multi-Project layout.
    * Note: The error shown in the web module is caused by the JSP validator and can be ignored.
  1. perform mvn eclipse:eclipse in the toplevel project dir.

Its recommended to create a Working Set (e.g. PSI-PROBE) where you move all sub-projects to.

## Compiling and Packaging PSI Probe from Source ##
  1. Right-click on the "web" project.
  1. Run as > Run on Server.
  1. Choose an installed Tomcat Server instance or create one.