# How to build PSI Probe

1.	**Clone PSI Probe's git repository.**

	*Note: If you plan to contribute to PSI Probe, you should create your own fork on GitHub first and clone that.  Otherwise, follow these steps to build the latest version of PSI Probe for yourself.*

	Execute the following command:

		git clone https://github.com/MALfunction84/psi-probe

	This will create directory called `psi-probe`. Subsequent steps will refer to this as "your PSI Probe base directory."

2.	**Install Maven 2.**

	You may download it from the [Maven release archive](http://maven.apache.org/docs/history.html).

3.	**Create the required ojdbc14 and ucp Maven artifacts.**

	Oracle's JDBC drivers and connection pool are protected by the OTN (Oracle Technology Network) Development and Distribution License.  For this reason, the .jar files cannot be legally hosted in any Maven repository or distributed with PSI Probe.  Follow these steps to create those artifacts manually:

	1.	Download the .jar files for [ojdbc14 10.2.0.1.0](http://www.oracle.com/technetwork/database/enterprise-edition/jdbc-10201-088211.html) and [ucp 11.2.0.1.0](http://www.oracle.com/technetwork/database/enterprise-edition/downloads/ucp-112010-099129.html).

	2.	Locate the ojdbc14-pom.xml and ucp-pom.xml files in your PSI Probe base directory.

	3.	Install ojdbc14.jar and ucp.jar as Maven artifacts.

		Execute the following commands:

			mvn install:install-file -Dfile=/path/to/ojdbc14.jar -DpomFile=/path/to/ojdbc14-pom.xml
			mvn install:install-file -Dfile=/path/to/ucp.jar -DpomFile=/path/to/ucp-pom.xml

4.	**Run Maven.**

	Execute the following command from your PSI Probe base directory:

		mvn package

	This will create a deployable file at `web/target/probe.war`.
