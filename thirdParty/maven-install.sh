#!/bin/bash
mvn install:install-file -Dfile=jmxri-1.2.1.jar -DgroupId=javax.management -DartifactId=jmxri -Dversion=1.2.1 -Dpackaging=jar
mvn install:install-file -Dfile=jmxtools-1.2.1.jar -DgroupId=javax.management -DartifactId=jmxtools -Dversion=1.2.1 -Dpackaging=jar
mvn install:install-file -Dfile=ojdbc6-11.2.0.4.0.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.4.0 -Dpackaging=jar
mvn install:install-file -Dfile=ucp-11.2.0.4.0.jar -DgroupId=com.oracle -DartifactId=ucp -Dversion=11.2.0.4.0 -Dpackaging=jar
