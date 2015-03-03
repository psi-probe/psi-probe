========================
 How to build PSI Probe
========================

1. Check out or download PSI Probe's source code.

   You may acquire it from the following URL:
   http://psi-probe.googlecode.com

2. Install Maven 2.

   You may download it from the following URL:
   http://maven.apache.org

3. Create the required ojdbc14 and ucp Maven artifacts.

   Oracle's JDBC drivers and connection pool are protected by the OTN (Oracle
   Technology Network) Development and Distribution License.  For this reason,
   the .jar files cannot be legally hosted in any Maven repository or
   distributed with PSI Probe.

   a. Download version 10.2.0.1.0 of the ojdbc14.jar and version 11.2.0.1.0 of
      the ucp.jar.

      You may download them from the following URLs:
      http://www.oracle.com/technetwork/database/enterprise-edition/jdbc-10201-088211.html
      http://www.oracle.com/technetwork/database/enterprise-edition/downloads/ucp-112010-099129.html

   b. Locate the ojdbc14-pom.xml and ucp-pom.xml files (where this readme.txt
      file resides).

   c. Install ojdbc14.jar and ucp.jar as Maven artifacts.

      Execute the following commands:
      mvn install:install-file \
      -Dfile=/path/to/ojdbc14.jar \
      -DpomFile=/path/to/ojdbc14-pom.xml

      mvn install:install-file \
      -Dfile=/path/to/ucp.jar \
      -DpomFile=/path/to/ucp-pom.xml

4. Run Maven.

   Execute the following command from the PSI Probe base directory (where this
   readme.txt file resides):
   mvn package

   This will create probe.war in web/target.
