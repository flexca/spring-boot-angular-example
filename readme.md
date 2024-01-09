# SpringBoot Angular Example Application

## Description

This is example of application that use Java SpringBoot as backend and Angular as frontend.

Next functionality implemented:
- organization/user registration;
- organization management;
- user management (including permissions).

## Technologies and Frameworks

List of used technologies/frameworks:
- Java 17
- Apache maven 3.9.6
- SpringBoot 3.2.0
- SpringSecurity 6.2.0
- MongoDB 7.0.2
- Apache Ignite 2.15.0
- Angular
- Docker
- Kubernetes

## MongoDB setup

1. Install mongodb and mongosh
2. Configure replicaset (transactions only available for replicaset)
   `mongosh --eval "rs.initiate({_id: \"rs_sbae\",members: [{_id: 0, host: \"<your local network ip address>\"}]})"`
3. Start mongodb
   `mongod --dbpath ../data --port 27017 --replSet rs_sbae --bind_ip 0.0.0.0`

## Developer environment setup

### Required software
- Java 17
- Apache maven 3.9.6
- MongoDB 7.0.2
- Intellij IDEA 
- MailHog

### Steps to start application for development/debugging

1. Clone this repository:
  `git clone https://github.com/flexca/spring-boot-angular-example.git`
2. Navigate to spring-boot-angular-example folder (directory) and run:
  `mvn clean install`
3. Open Intellij IDEA and create new project from existing sources. When selecting sources select pom.xml in spring-boot-angular-example folder.
4. Create new run/debug configuration. Select SbaeApplication as main class
5. Add to VM options next (first option is Spring profile for local run, other options is required by Apache Ignite):
   
   `-Dspring.profiles.active=local
   --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED
   --add-opens=java.base/sun.nio.ch=ALL-UNNAMED
   --add-opens=java.base/sun.nio.ch=ALL-UNNAMED
   --add-opens=java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED
   --add-opens=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED
   --add-opens=java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED
   --add-opens=jdk.management/com.sun.management.internal=ALL-UNNAMED
   --add-opens=java.base/java.io=ALL-UNNAMED
   --add-opens=java.base/java.nio=ALL-UNNAMED
   --add-opens=java.base/java.util=ALL-UNNAMED
   --add-opens=java.base/java.lang=ALL-UNNAMED`
6. Click run or debug button and wait while backend will be started
7. Navigate to fronend folder
8. Run `ng serve`
9. Open in browser http://localhost:4200
10. Start MailHog to receive emails with organizations and users registration links
