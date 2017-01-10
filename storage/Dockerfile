FROM registry.opensource.zalan.do/stups/openjdk:8-42

MAINTAINER Zalando SE

ADD build/libs/storage.jar /
ADD scm-source.json /

EXPOSE 8080
ENV HTTP_PORT=8080

CMD java -Xmx512m $(appdynamics-agent) -jar /storage.jar
