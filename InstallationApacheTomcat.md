

---


# Configuration #

Before you deploy PSI Probe to Apache Tomcat, you must configure the server.  This may require you to restart the Tomcat service.

## Security ##

PSI Probe requires four security roles (in order of increasing [privileges](Features#Features_by_Role.md)):

  * **probeuser**
  * **poweruser**
  * **poweruserplus**
  * **manager** - This is the same role required by Tomcat Manager and has the highest level of privileges.

**Note:** On Tomcat 7 (or Tomcat 6.0.30 and above), you can use `manager-gui` instead of `manager`.

These must be added to your Realm.  By default, roles are configured by editing `$CATALINA_HOME/conf/tomcat-users.xml`:

```
<tomcat-users>

  <role rolename="probeuser" />
  <role rolename="poweruser" />
  <role rolename="poweruserplus" />
  <role rolename="manager" />

  <user username="admin" password="t0psecret" roles="manager" />

</tomcat-users>
```

This will create a user called `admin` with the role of `manager`.

If you are not using the default configuration, add these roles as appropriate for your Realm.  Some Realm configurations, such as a JNDIRealm connecting to LDAP, will not require a server restart.

## Enable Remote JMX (Optional) ##

PSI Probe requires remote JMX to collect and display certain pieces of information (memory usage, cluster traffic, connection pools, and active threads).  You can enable remote JMX with the following JVM command line option:

```
-Dcom.sun.management.jmxremote=true
```

If you need the JMX connection to be secured, refer to the Tomcat documentation on this topic.

# Deployment #

Once you have completed the configuration steps, it's time to deploy PSI Probe.

## Option 1: Deploy with Tomcat Manager ##

  1. Start Tomcat if it is not running.
  1. Open the manager URL (e.g. `http://localhost:8080/manager/html`) in your web browser.
  1. Upload `probe.war` using the "War file to deploy" option.

## Option 2: Manually Copy to Server ##

  1. Shut down Tomcat if it is running.
  1. Copy `probe.war` into `$CATALINA_HOME/webapps/`
  1. Start Tomcat.

## Option 3: Fully Manual Deployment ##

You can, of course, unpackage `probe.war` file and create the `probe.xml` context descriptor yourself.

If you do this, understand that PSI Probe _requires_ a **privileged** context.  `META-INF/context.xml` in `probe.war` contains an example:

```
<?xml version="1.0" encoding="UTF-8"?>
<Context path="/probe" privileged="true" />
```

# Testing #

Once you have deployed PSI Probe to Tomcat, verify that you can access it in your web browser by entering PSI Probe's URL (e.g. `http://localhost:8080/probe`).

When you are prompted for a username and password, enter the credentials for the manager account you created earlier.

If you have any problems, see our [Troubleshooting](Troubleshooting.md) page.