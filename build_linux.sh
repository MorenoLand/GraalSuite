#!/bin/bash

mkdir -p bin

javac -Xlint:-deprecation -Xlint:-unchecked -cp .:rsyntaxtextarea-3.0.0-SNAPSHOT.jar com/dinkygames/graaleditor/*.java com/dinkygames/graaleditor/undo/*.java org/eclipse/jdt/internal/jarinjarloader/*.java

if [ $? -ne 0 ]; then
    echo "Compilation failed"
    exit 1
fi

mkdir -p build/META-INF

cp -r com/*.class build/com/ 2>/dev/null || mkdir -p build/com && cp -r com/*.class build/com/
cp -r org/*.class build/org/ 2>/dev/null || mkdir -p build/org && cp -r org/*.class build/org/
cp -r META-INF/* build/META-INF/ 2>/dev/null || true
cp -r res build/

cd build
jar xf ../rsyntaxtextarea-3.0.0-SNAPSHOT.jar
cd ..

echo "Manifest-Version: 1.0" > manifest.txt
echo "Main-Class: com.dinkygames.graaleditor.GraalEditor" >> manifest.txt

jar cvfm bin/GraalSuite.jar manifest.txt -C build .

rm -rf build
rm manifest.txt

echo "Build complete: bin/GraalSuite.jar"