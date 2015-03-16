

---


# Collected Stats #

## Scheduling Data Collection ##

Command line properties can be used to configure stats collection schedules.  The template of these properties is:

`-Dcom.googlecode.psiprobe.beans.stats.collectors.CATEGORY.PARAMETER=EXPRESSION`

### Category ###

The category of statistics to configure.  Must be one of the following:

| **Category** | **Description** | **Data Points per Collection** |
|:-------------|:----------------|:-------------------------------|
| `app` | Number of requests/errors, current/average processing time time | 4 x (# of apps) + 2 |
| `cluster` | Bytes sent/received, messages sent/received | 4 |
| `connector` | Number of requests/errors, bytes sent/received, processing time | 5 x (# of connectors) |
| `datasource` | established/busy connections | 2 x (# of datasources) |
| `memory` | JVM memory used per pool | 1 x (# of memory pools) + 1 |
| `runtime` | OS physical/virtual/swap memory used, CPU usage | 4 |

### Parameter ###

The parameter for the specified category.  Must be one of the following:

| **Parameter** | **Description** |
|:--------------|:----------------|
| `period` | The length of time between each collection |
| `phase` | The offset from the top of the minute or hour when the collection will take place |
| `span` | The amount of time each data point is retained in history |

### Expression ###

A short time expression consisting of a number and a letter representing the unit of time (`s` for seconds, `m` for minutes, or `h` for hours).

The Expression you choose for a category's `period` should evenly divide the next highest unit of time.  Poor choices include `17s`, `24m` or `5h`.  Good choices include `15s`, `30m`, or `6h`.

### Examples ###

You may view the default values [here](http://code.google.com/p/psi-probe/source/browse/trunk/web/src/main/conf/WEB-INF/stats.properties) to see examples.

## Memory Consumption ##

Each data point takes up 24 bytes of memory, so these statistics can take up large amounts of memory if you have a short period, a long span, or a large number of items within the category.  To determine the amount of memory taken up by a category's data, use the following formula:

<a href='http://www.codecogs.com/eqnedit.php?latex=m=\frac{24ds}{p}'><img src='http://latex.codecogs.com/png.latex?m=\frac{24ds}{p}%.png' align='middle' title='m=\frac{24ds}{p}' /></a>, where

  * _m_ = Memory used in bytes
  * _d_ = Data Points per Collection (see [Category](#Category.md) above)
  * _s_ = `span` (in seconds)
  * _p_ = `period` (in seconds)

For instance, if you configure the `cluster` category to collect information every 10 seconds and retain it for 3 days, this will take up 2,488,320 bytes (2.37 MB) of memory.  The same settings for app statistics would take up about the same amount of memory _per application_.

## Excluding PSI Probe ##

PSI Probe can ignore its own requests when calculating cumulative application statistics.  Just add the following to the command line:

`-Dcom.googlecode.psiprobe.beans.stats.collectors.app.selfIgnored=true`

<a href='Hidden comment: 
= E-mail Notifications =

PSI Probe can send you notification emails based on the status of the server.

== Recipients and Formatting ==

Command line properties can be used to configure e-mail settings.

|| *Property* || *Function* || *Default value* ||
|| `mail.smtp.host` || SMTP server used to send mail || _None_ (mail is disabled) ||
|| `mail.from` || Mail sender || _Server-generated value_ ([http://javamail.kenai.com/nonav/javadocs/javax/mail/internet/InternetAddress.html#getLocalAddress(javax.mail.Session) info]) ||
|| `com.googlecode.psiprobe.tools.mail.to` || Mail recipient(s), comma-separated || _None_ (mail is disabled) ||
|| `com.googlecode.psiprobe.tools.mail.subjectPrefix` || Prefix added to the subject of every message || `[PSI Probe]` ||

=== Enabling ===
Both the mail.smtp.host and com.googlecode.psiprobe.tools.mail.to properties must be defined for e-mail notification to be enabled.

=== Identifying a message"s source ===
Defining the mail.from and com.googlecode.psiprobe.tools.mail.subjectPrefix properties will help recipients identify which server is sending the message.  !JavaMail allows Personal Names in addresses too, so "Test Server Monitor <psi-probe@test.mydomain.com>" is a valid sender address.

== Trigger: Memory Usage ==

Command line properties can be used to configure the memory threshold for notifications.  The template of these properties is:

-Dcom.googlecode.psiprobe.beans.stats.listeners.memory.pool.POOLNAME.threshold=VALUE

* POOLNAME will correspond to the name of the memory pool listed under the Memory Utilization page.
* VALUE will represent the threshold size of the pool, in bytes.  When this value is exceeded, PSI Probe will send an e-mail.

If the value flaps back and forth between being above and below the threshold, PSI Probe will put that pool listener into a temporary "flapping" state.  Emails will be temporarily suspended until the value stabilizes.  This helps to avoid flooding recipients with emails.
'></a>