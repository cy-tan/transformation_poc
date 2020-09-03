##############################
#Dokcer Build using alpine jdk
##############################
FROM openjdk:8-alpine
MAINTAINER CY Tan < cheetan at redhat dot com >
LABEL BASE_IMAGE="ropenjdk:8-alpine:latest"
LABEL JAVA_VERSION="8"

RUN mkdir -p /app; \
chmod "g+rwX" /app

WORKDIR /app/
COPY target/*.jar /app/application.jar

EXPOSE 8080
CMD ["java", "-jar", "application.jar"]

##############################
#Dokcer Build using alpine registry.access.redhat.com/ubi7/ubi-minimal:latest + jdk
##############################
#FROM registry.access.redhat.com/ubi7/ubi-minimal:latest
#MAINTAINER CY Tan < cheetan at redhat dot com >
#LABEL BASE_IMAGE="ropenjdk:8-alpine:latest"
#LABEL JAVA_VERSION="8"

#RUN microdnf install java-1.8.0-openjdk --nodocs ;\
#microdnf clean all  
#RUN mkdir -p /app; \
#chmod "g+rwX" /app

#WORKDIR /app/
#COPY target/*.jar /app/application.jar

#EXPOSE 8080
#CMD ["java", "-jar", "application.jar"]
