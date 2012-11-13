#!/bin/sh
JAVAPATH="/usr/local/linux-sun-jdk1.6.0"
"${JAVAPATH}/bin/java" -jar ./dist/sugarsync-java.jar $*
