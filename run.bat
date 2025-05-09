@echo off

REM Compile the project
javac -Xlint:-deprecation -Xlint:-unchecked -cp .;rsyntaxtextarea-3.0.0-SNAPSHOT.jar com/dinkygames/graaleditor/*.java com/dinkygames/graaleditor/undo/*.java org/eclipse/jdt/internal/jarinjarloader/*.java

REM Run the application
if %ERRORLEVEL% == 0 (
  echo.
  echo Compilation successful! Running the application...
  java -cp .;rsyntaxtextarea-3.0.0-SNAPSHOT.jar com.dinkygames.graaleditor.GraalEditor
) else (
  echo.
  echo Compilation failed with errors.
)
:end