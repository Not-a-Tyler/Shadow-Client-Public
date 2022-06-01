#!/bin/sh

echo "Making Obfuscated Build"
ver=`cat ./src/main/resources/version.txt`
./gradlew build
java -jar "./proguard/proguard.jar" "$@"
mv ./build/libs/shadow-1.0.0.jar bin/shadow-$ver