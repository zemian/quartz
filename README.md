
## Builld status: 
![quartz-2.4.x branch build status](https://travis-ci.org/quartz-scheduler/quartz.svg?branch=quartz-2.4.x "quartz-2.4.x build status")


## Build instructions:

### Requirements:

* JDK 8

NOTE: Our maven wrapper is using Maven 3.6.0

### To compile:
```
export JAVA_HOME=/path/to/jdk8
$ ./mvnw compile
```

### To build Quartz distribution kit:

```
$ ./mvnw package
```

Note:  the final Quartz jar is found under quartz/target
