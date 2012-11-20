#!/bin/sh
JVMLOC="/usr/local/linux-sun-jdk1.7.0/bin"
cd dist && "${JVMLOC}/java" -Djava.library.path=../contrib/sqlite4java-282/ -jar sugarsync-java.jar $*
