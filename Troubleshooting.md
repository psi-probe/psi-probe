

---


# ProbeServlet is privileged and cannot be loaded... #

An issue with the yum package for Tomcat 6 on RHEL 6 may prevent privileged apps from working correctly (see [issue 243](https://code.google.com/p/psi-probe/issues/detail?id=243)).  You may need to create `/etc/tomcat6/Catalina/localhost/probe.xml` manually, similar to a [Fully Manual Deployment](InstallationApacheTomcat#Option_3:_Fully_Manual_Deployment.md).

# Error on first request #

Probe uses ServerInfo to determine what server it's running on.  If you've changed this property on your server or if you are using an unsupported server, Probe may give an error when you try to access it.  However, you can bypass this issue by forcing Probe to use a specific server adapter:

  1. Open probe.war and edit `/WEB-INF/spring-probe-resources.xml`.
  1. Find the "containerWrapper" bean.
  1. Toggle the "forceFirstAdaptor" value from **false** to **true**.
  1. Ensure that the adapter for your version of Tomcat is listed first in the list of "adaptorClasses."  For example, if you are deploying Probe to Tomcat 6, ensure that `com.googlecode.psiprobe.Tomcat60ContainerAdaptor` is listed first.

# Graphs do not display #

## Run AWT in Headless Mode on UNIX ##

Some users have experienced problems (see [issue 64](https://code.google.com/p/psi-probe/issues/detail?id=64)) with graph images not displaying properly, especially when running Tomcat on Sun's JDK for UNIX.  If you see a stack trace for a `java.lang.NoClassDefFoundError` thrown by a class in the `org.jfree.chart` package, add the following JVM command line option:

```
-Djava.awt.headless=true
```

## No fonts found in OpenJDK ##

Some users have experienced problems (see [issue 161](https://code.google.com/p/psi-probe/issues/detail?id=161)) when running OpenJDK, causing `java.lang.Error: Probable fatal error:No fonts found.`  If switching to the Sun/Oracle JDK is not an option, consider installing a font package.

  * http://wiki.hudson-ci.org/display/HUDSON/Hudson+got+java.awt.headless+problem
  * https://bugzilla.redhat.com/show_bug.cgi?id=478480
  * http://www.rhq-project.org/display/JOPR2/FAQ#FAQ-WhyarethegraphsandchartsontheMonitortabintheGUInotdisplayed%3F

# My problem wasn't listed here #

I hate to ask, but are you using the latest version of Probe?  Your issue might be [fixed already](Changelogs.md).

Someone might have already found a solution you can apply yourself.  Check the [list of issues with an ExternalFix](http://code.google.com/p/psi-probe/issues/list?can=1&q=status%3AExternalFix).

No fix available?  You might not be alone.  Search the [list of open issues](http://code.google.com/p/psi-probe/issues/list) and star your issue to stay informed.

Still no luck?  Please [file a new issue](http://code.google.com/p/psi-probe/issues/entry) and provide as much information as you can.