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

      You may download it from the following URL:
      http://www.oracle.com/technology/software/tech/java/sqlj_jdbc/htdocs/jdbc_10201.html

   b. Install ojdbc14.jar as a Maven artifact.

      Execute the following command:
      mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc14 \
      -Dversion=10.2.0.1.0 -Dpackaging=jar -Dfile=/path/to/ojdbc14.jar \
      -DgeneratePom=true

4. Run Maven.

   Execute the following command from the PSI Probe base directory (where this
   readme.txt file resides):
   mvn package

   This will create probe.war in web/target.
