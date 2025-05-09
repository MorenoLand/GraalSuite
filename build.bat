@echo off
echo Building GraalSuite JAR...

REM Create the bin directory if it doesn't exist
if not exist bin mkdir bin

REM Create a temporary directory for building the JAR
mkdir build
mkdir build\com
mkdir build\org
mkdir build\META-INF
mkdir build\res

REM Copy all compiled classes and resources
xcopy /E /Y com build\com\
xcopy /E /Y org build\org\
xcopy /E /Y META-INF build\META-INF\
xcopy /E /Y res build\res\

REM Extract the RSyntaxTextArea JAR into the build directory
cd build
jar xf ..\rsyntaxtextarea-3.0.0-SNAPSHOT.jar
cd ..

REM Create a manifest file with correct classpath
echo Manifest-Version: 1.0 > manifest.txt
echo Main-Class: com.dinkygames.graaleditor.GraalEditor >> manifest.txt

REM Create the JAR file in the bin directory
jar cvfm bin\GraalSuite.jar manifest.txt -C build .

REM Copy required resource files to bin directory for running the application
if not exist bin\res mkdir bin\res
if not exist bin\res\images mkdir bin\res\images
xcopy /Y res\images\*.* bin\res\images\

REM Clean up
rmdir /S /Q build
del manifest.txt

echo JAR creation complete: bin\GraalSuite.jar
echo.
echo To run the application, use: java -jar bin\GraalSuite.jar