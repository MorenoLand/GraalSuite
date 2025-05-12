[![Jar Build](https://github.com/username/GraalSuite/actions/workflows/main.yml/badge.svg)](https://github.com/username/GraalSuite/actions/workflows/main.yml)

[![Native Builds](https://github.com/username/GraalSuite/actions/workflows/native-build.yml/badge.svg)](https://github.com/username/GraalSuite/actions/workflows/native-build.yml)

## Requirements

- Java JDK 8 or higher
- [RSyntaxTextArea library](https://github.com/bobbylight/RSyntaxTextArea.git) (included or grab one from [here](https://mvnrepository.com/artifact/com.fifesoft/rsyntaxtextarea/3.0.0))

### Command line (Windows)

```bash
# Build executable JAR
build_win.bat

# Run from JAR (after building)
java -jar bin/GraalSuite.jar

# -----------------------------------

# Run directly
run_win.bat

or

java -cp .;rsyntaxtextarea-3.0.0-SNAPSHOT.jar com.dinkygames.graaleditor.GraalEditor

```


## Command Line (Linux, only tested in Ubuntu 24.04)


```bash
# Pre-requisite (optional sound)
sudo apt-get install libcanberra-gtk-module

# Build executable JAR
build_linux.sh

# Run from JAR (after building)
java -jar bin/GraalSuite.jar

# -----------------------------------

# Run directly
run_linux.sh

or

java -cp .:rsyntaxtextarea-3.0.0-SNAPSHOT.jar com.dinkygames.graaleditor.GraalEditor

```

## Project Structure

- `com.dinkygames.graaleditor`: Main editor classes
- `org.eclipse.jdt.internal.jarinjarloader`: JAR-in-JAR loader utility
- `res/`: Resources including icons and default levels

## Notes

- First run prompts to set the Graal directory
- Creates a cache file in the Graal directory for faster loading
- Editing tileset definitions requires a restart
