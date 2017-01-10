FROM registry.opensource.zalan.do/stups/openjdk:8-42

MAINTAINER Zalando SE

ADD build/libs/crawler.jar /
ADD scm-source.json /scm-source.json

EXPOSE 8080

CMD java -Xmx512m $(appdynamics-agent) -jar /crawler.jar
