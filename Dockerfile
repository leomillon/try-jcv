FROM tomcat:8

ADD docker/allin1/conf/ /usr/local/tomcat/conf/

RUN ["rm", "-r", "/usr/local/tomcat/webapps"]

ADD docker/allin1/apps/WebDemoBackend.war /usr/local/tomcat/backend/ROOT.war
ADD docker/allin1/apps/WebDemoWar.war /usr/local/tomcat/server/ROOT.war

RUN apt-get update && apt-get install --assume-yes openjdk-8-jdk
