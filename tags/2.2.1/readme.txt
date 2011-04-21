========================
 How to build PSI Probe
========================

1. Check out or download PSI Probe's source code.

   You may acquire it from the following URL:
   http://psi-probe.googlecode.com

2. Install Maven 2.

   You may download it from the following URL:
   http://maven.apache.org

3. Create the required ojdbc14 Maven artifact.

   Oracle's JDBC drivers are protected by the OTN (Oracle Technology Network)
   Development and Distribution License.  For this reason, the .jar cannot be
   legally hosted in any Maven repository or distributed with PSI Probe.

   a. Download ojdbc14.jar.

      Navigate to
      http://www.oracle.com/technetwork/database/enterprise-edition/jdbc-10201-088211.html
      and download the ojdbc14.jar file for version 10.2.0.1.0.

   b. Create the ojdbc14-pom.xml file:

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.oracle</groupId>
	<artifactId>ojdbc14</artifactId>
	<version>10.2.0.1.0</version>
	<packaging>jar</packaging>
	<name>Oracle Database 10g Release 2 JDBC Driver</name>
	<url>http://www.oracle.com/technetwork/database/enterprise-edition/jdbc-10201-088211.html</url>
	<licenses>
		<license>
			<name>Oracle Technology Network Development and Distribution License</name>
			<url>http://www.oracle.com/technetwork/licenses/distribution-license-152002.html</url>
			<distribution>manual</distribution>
		</license>
	</licenses>
</project>

   c. Install ojdbc14.jar as a Maven artifact.

      Execute the following command:
      mvn install:install-file \
      -Dfile=/path/to/ojdbc14.jar \
      -DpomFile=/path/to/ojdbc14-pom.xml

4. Create the required jdbc-pool Maven artifact.

   At the time of writing, this artifact could not be found in any Maven
   repository.  However, it is available from the Apache Tomcat website.

   a. Download the file.

      Navigate to http://people.apache.org/~fhanik/jdbc-pool/v1.0.8.5/
      and download whichever archive you prefer.  The tomcat-jdbc.jar file is inside.

   b. Create the tomcat-jdbc-pom.xml file:

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>org.apache.tomcat</groupId>
	<artifactId>jdbc-pool</artifactId>
	<version>1.0.8.5</version>
	<packaging>jar</packaging>
	<name>Apache Tomcat JDBC Pool</name>
	<url>http://people.apache.org/~fhanik/jdbc-pool/v1.0.8.5/</url>
	<licenses>
		<license>
			<name>Apache License, Version 2.0</name>
			<url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
			<distribution>manual</distribution>
		</license>
	</licenses>
	<dependencies>
		<dependency>
			<groupId>org.apache.tomcat</groupId>
			<artifactId>juli</artifactId>
			<version>6.0.13</version>
			<scope>provided</scope>
		</dependency>
	</dependencies>
</project>

      Note: groupId and artifactId chosen to match those used in
      http://ascendant76.blogspot.com/2010/02/upcoming-jdbc-pool.html

   c. Install tomcat-jdbc.jar as a Maven artifact.

      Execute the following command:
      mvn install:install-file \
      -Dfile=/path/to/tomcat-jdbc.jar \
      -DpomFile=/path/to/tomcat-jdbc-pom.xml

5. Run Maven.

   Execute the following command from the PSI Probe base directory (where this
   readme.txt file resides):
   mvn package

   This will create probe.war in web/target.
