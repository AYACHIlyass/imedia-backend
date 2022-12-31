Create shop image
===
in this document we will present how to create a docker image of this project.
### Prerequisites
To generate the shop docker image you have to already have
* docker
* java
# create docker image of the shop application
**_NOTE:_** These commands have to be run in the root file of the project.
## 1. Create the project jar file
In this step we will create the jar file which it will be integrated in the image file we will generate.
```
./gradlew clean build
```
This command will generate a jar file in the ./build/libs named shop-0.0.1-SNAPSHOT.jar; this name can be customized from
the build.gradle.kts

## 2.Build docker image
To create the docker image run the following command
```
docker build --build-arg JAR_FILE=build/libs/\*.jar -t shop:0.0.1-SNAPSHOT -f src/main/resources/docker/Dockerfile .
```
This will create a docker image named shop and tagged with 0.0.1-SNAPSHOT

To verify that the image was created successfully run the following command
```
docker images
```
which will display a shop image and its tag 0.0.1-SNAPSHOT
