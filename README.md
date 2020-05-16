# j2ee

Basic sample with RestEasy + JPA + Hibernate + Liquibase + Wildfly + Swagger.

## Getting Started


### Prerequisites

* JDK 8
* Java IDE
* Maven
* Wildfly-10.0.0.Final | Wildfly-12.0.0.Final | Wildfly-13.0.0.Final | Wildfly-14.0.0.Final
* PostgreSQL

### Installing

For working you will need to:

* Import this project to your IDE using the option "existing maven project" and selecting the parent project "j2ee"

## Deployment

You will need a base wildfly (one of the versions posted above).
Note: files on dev_tools folder are based on wildfly-12, relevant config is:
run with java 8, add the datasource, remove h2 dtasource, config default-bindings to use our datasource. 

**Steps:**
Then go to wildfly bin folder rename standalone.conf.bat to standalone.conf.bat.original
Then took standalone.conf.bat from dev_tools/wildfly folder and paste it in wildfly bin folder (check to set your java 8 dir)
Then go to standalone/configuration folder and rename standalone.xml to standalone-original.xml
Then took standalone.xml from dev_tools/wildfly folder and paste it in wildfly standalone/configuration folder
Since it use a postgresql database you must config postgresql driver for wildfly by creating in wildfly modules folder
a folder structure like -> org/postgresql/main/, put inside your jdbc jar file and **module.xml**. 
You can find the whole structure in dev_tools folder.

Also you will need to:

* Generate maven artifact through -> **mvn clean install** or **mvn clean package** command
* Put generated ear and the previous artifacts on your $Wildfly_Home/standalone/deployments folder
* A base Wildfly with base properties and values in standalone.conf.bat (Windows) or standalone.conf(Linux).

For start you need to run Wildfly with **standalone.bat**
Enjoy

## Check if container is up

When the service is deployed, wildfly generates a file with ext **.deployed** like "j2ee-ear.ear.deployed" . 
Also you can check the status with the health check service.

## J2EE Services

This project uses swagger-ui for a complete endpoint reference:

```
* (Swagger-UI)     {{ host  }}:{{ port  }}/j2ee/swagger-ui
```
Once open please paste on the search box ->  {{ host  }}:{{ port  }}/j2ee/swagger.json
So like you see the app context is **j2ee**
For a complete reference of how to call this services, please check Insomnia JSON file (in dev_tools folder).

## Persistence

Currently I am using JPA +  Liquibase + PostgreSQL set up, so for config you can check j2ee-db project for add or modify the current profiles. For update db please make sure that **db.changelog.xml** contains all the changes for the project. For run changes over db run a goal like -> **mvn clean install -P your_profile_here -Dscm.skipdb=false**

## Java Doc

For check java doc about service once you have already download the sources you can run **mvn clean install** or **mvn clean package** command, this will generate ear file for  services, also that will run unit and integration tests and generate javadoc for this project. After the process will done, check on every child project this route:

**{{ the child project }}/target/apidocs** inside must be a file named 'index.html' open with your prefered browser and enjoy.

## Java Test and Code Coverage

In this project I am using:

* [JUnit 5](https://junit.org/junit5/)
* [Mockito](https://site.mockito.org/)

After you build the project you can inspect:

**On every child project**

- /target/failsafe-reports -> Integration test results
- /target/surefire-reports -> Unit test results
- /site/jacoco-ut          -> Java Code Coverage (JaCoCo) provided by unit test (inside must be a file named 'index.html' open with your prefered browser)
- /site/jacoco-it          -> Java Code Coverage (JaCoCo) provided by integration test (inside must be a file named 'index.html' open with your prefered browser)

**On parent project**
- /target/jacoco-ut.exec   -> Merged JaCoCo unit test file for export metrics to analyze with tools like SonarQube
- /target/jacoco-ut.exec   -> Merged JaCoCo integration test file for export metrics to analyze with tools like SonarQube

You can check project metrics using the provided sonar-project.properties in parent project. Enjoy

## Formatter

At this moment I am using formatter-maven-plugin for avoid to have diff between code format on developer ide's.
That's because we can have small changes, but if our formmater is different it may cause a lot of git diffs and is not convenient for merge our changes. 
During the build this plugin run the goal validate, so if one class
doesn't comply with established format, build will fails, to fix run -> **mvn formatter:format**.
Ensure before your merges to run that goal please. If you want to know more about the plugin or want to change to a custom checkstyle please check [formatter-maven-plugin](https://code.revelc.net/formatter-maven-plugin/) for more details. 
**I suggest to run this goal before merge your changes for CI/Push.**

## Built With

* [Java](https://www.java.com/en/download/) - Programming language
* [PostgreSQL](https://www.postgresql.org/) - DB
* [Maven](https://maven.apache.org/) - Dependency Management
* [Wildfly](http://wildfly.org/) - Server/Container
* [Jenkins](https://jenkins.io/) - CI

## Contributors

* **Alexander Galvis Grisales** - *Initial status*
