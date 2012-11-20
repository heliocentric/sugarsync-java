#!/bin/sh
cd dist && "java" -Djava.library.path=../contrib/sqlite4java-282/ -jar sugarsync-java.jar $*
