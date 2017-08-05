## Team

**Group 1**:

| MatriculationNr. | eMail                    | Firstname | Surname   |
| ---------------- | ------------------------ | --------- |---------- |
|1633394           | @student.tuwien.ac.at    |lingfan    |gao        |
|1528594           | @student.tuwien.ac.at    |patrick    |jofen      |
|0707282           | @student.tuwien.ac.at    |martina    |sengschmid |

## Environment Installation
Following steps must be performed in order:
1. Install Apache Maven
2. Install Java 7 or higher (and add `JAVA_HOME` to `PATH`-Variable)
3. Install docker and docker-compose

## Docker deployment (WAR - recommended)
1. Start the DB and server containers with `$ docker-compose up`
2. Run the command `mvn clean package -Pwar` to package and output the .war file to webapps folder
3. The tomcat server should automatically deploy the war-file and run migrations upon startup.
4. Navigate to http://localhost:8080/swme/ to check if everything is up and running
5. If this is not working always try `$ docker-compose down` and `$docker-compose up --build`

## Standalone deployment (JAR)
It is also possible to deploy the application as a JAR file with an embedded tomcat server.

1. Run the command `mvn clean package -Pjar` to package and output the .jar file to webapps folder
2. Change into the webapps folder and run the jar file with:
 * `java -jar swme.jar` to use the embedded h2 database.
 * `java -jar -Dspring.profiles.active=prod swme.jar` to run the application using the production profile which is 
 configured to use a PostgreSQL database (see main `pom.xml`).
 * `java -jar -Dspring.profiles.active=local swme.jar` to run the application using the production profile for for a
 PostgresSQL database on localhost. Make sure that only the postgres is started and not Tomcat as the ports will conflict
 with each other
3. Navigate to http://localhost:8080/swme/ to check if everything is up and running

## Spring Boot Profiles
The Spring Boot application profiles can be found under `src/main/resources/application-X.properties`.
However in the default configuration, it should not be necessary to change any of these files.

The `test` profile (`application-test.properties`) is configured to use a embedded h2-database to ensure the tests
run independently of an external database. This profile is set as default.

The `prod` (`application-prod.properties`) profile is using a PostgreSQL database with the following settings:

```
url:        jdbc:postgresql://postgres:5432/swme
username:   postgres
postgres:   postgres
```
These properties are injected by maven at build time, so in order to change any of these settings, change the properties
 inside the `<properties>` tag of the main `pom.xml` then rebuild and redeploy the package.
 
### Docker Application profile
The docker application profile is specified through an environment variable. The default profile is `prod`.
To switch the application profile, change the `JAVA_OPTS="-Dspring.profiles.active=prod"` line in the 
`docker-compose.yml` file.

## Migrations
All migration scripts are located in the folder /resouces/migrations. A migration script must take the form of 
`V1__initial_baseline.sql` which states the version number followed by TWO underscores and then the name of the script.

Migrations are run by default at startup in this configuration, however it is possible to clean the database manually
with `$ mvn flyway:clean`. The database should NOT be manually migrated as the checksum will differ from Spring's automatic
migration. 