## Configure NetBeans for Maven ##
  1. Download and install [Apache Maven](http://maven.apache.org/).
    * If you are using JDK 1.4, get version 2.0.x (preferably 2.0.9+).
    * If you are using JDK 1.5 or higher, get the latest version.
  1. In NetBeans, go to "Tools" > "Options." ([screenshot](http://psi-probe.googlecode.com/svn/wiki/IDEConfigNetBeans/tools-options.png))
  1. Click the "Miscellaneous" tab, then click the "Maven" subtab.
  1. Enter the path where you installed Maven. ([screenshot](http://psi-probe.googlecode.com/svn/wiki/IDEConfigNetBeans/misc-maven.png))

## Check Out PSI Probe Source ##
  1. In NetBeans, go to "Team" > "Subversion" > "Checkout..." ([screenshot](http://psi-probe.googlecode.com/svn/wiki/IDEConfigNetBeans/team-svn-checkout.png))
  1. Enter the URL for psi-probe (http://psi-probe.googlecode.com/svn/trunk) and leave the credential blank. ([screenshot](http://psi-probe.googlecode.com/svn/wiki/IDEConfigNetBeans/enter-repo-url.png))
    * Note to project committers: use https and enter your Google Code account information.
  1. Click "Next" and wait for NetBeans to connect to the repository.
  1. Enable "Skip 'trunk' and checkout only its contents."
  1. Enter or browse to the location where you would like to check out the PSI Probe source code. ([screenshot](http://psi-probe.googlecode.com/svn/wiki/IDEConfigNetBeans/enter-checkout-loc.png))
  1. Click "Finish."  It may take a while to check out.
  1. Assuming you have NetBeans configured to use Maven, a dialog will appear informing you that projects have been checked out.  Click "Open Project..." ([screenshot](http://psi-probe.googlecode.com/svn/wiki/IDEConfigNetBeans/checkout-complete.png))
  1. Select the "PSI Probe" project and enable "Open Required" near the bottom.  Then click "Open." ([screenshot](http://psi-probe.googlecode.com/svn/wiki/IDEConfigNetBeans/open-projects.png))

## Compiling and Packaging PSI Probe from Source ##
  1. You may see errors for some of the sub-projects.
    * Some of these will be fixed when you perform your first build and Maven downloads the artifacts.
    * You will need to install the ojdbc14 and tomcat-jdbc Maven artifacts manually.  See [readme.txt](http://psi-probe.googlecode.com/svn/trunk/readme.txt) for more details.
  1. Right-click the "PSI Probe" base project and select "Build" or "Clean and Build." ([screenshot](http://psi-probe.googlecode.com/svn/wiki/IDEConfigNetBeans/build-from-source.png))