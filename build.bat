@echo off

if not exist bin mkdir bin

javac -Xlint:-deprecation -Xlint:-unchecked -cp .;rsyntaxtextarea-3.0.0-SNAPSHOT.jar com/dinkygames/graaleditor/*.java com/dinkygames/graaleditor/undo/*.java org/eclipse/jdt/internal/jarinjarloader/*.java

if %ERRORLEVEL% neq 0 (
    echo Compilation failed
    exit /b 1
)

mkdir build
mkdir build\META-INF

xcopy /E /Y com\*.class build\com\
xcopy /E /Y org\*.class build\org\
xcopy /E /Y META-INF build\META-INF\
xcopy /E /Y res build\res\

cd build
jar xf ..\rsyntaxtextarea-3.0.0-SNAPSHOT.jar
cd ..

echo Manifest-Version: 1.0 > manifest.txt
echo Main-Class: com.dinkygames.graaleditor.GraalEditor >> manifest.txt

jar cvfm bin\GraalSuite.jar manifest.txt -C build .

rmdir /S /Q build
del manifest.txt

echo Build complete: bin\GraalSuite.jar