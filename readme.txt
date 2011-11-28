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

   a. Download version 10.2.0.1.0 of the ojdbc14.jar.

      You may download it from the following URL:
      http://www.oracle.com/technetwork/database/enterprise-edition/jdbc-10201-088211.html

   b. Locate the ojdbc14-pom.xml file (where this readme.txt file resides).

   c. Install ojdbc14.jar as a Maven artifact.

      Execute the following command:
      mvn install:install-file \
      -Dfile=/path/to/ojdbc14.jar \
      -DpomFile=/path/to/ojdbc14-pom.xml

4. Create the required tomcat-jdbc Maven artifact.

   At the time of writing, this artifact could not be found in any Maven
   repository.  However, it is available from the Apache Tomcat website.

   a. Download and extract the archive containing the tomcat-jdbc.jar file.

      You may download it from the following URL:
      http://people.apache.org/~fhanik/jdbc-pool/v1.0.8.5/

   b. Locate the tomcat-jdbc-pom.xml file (where this readme.txt file resides).

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
