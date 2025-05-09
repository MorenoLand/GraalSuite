## Requirements

- Java JDK 8 or higher
- [RSyntaxTextArea library](https://github.com/bobbylight/RSyntaxTextArea.git) (included or grab one from [here](https://mvnrepository.com/artifact/com.fifesoft/rsyntaxtextarea/3.0.0))

### Using batch files (Windows)

```bash
# Build executable JAR
build.bat

# Run from JAR (after building)
java -jar bin/GraalSuite.jar

# -----------------------------------

# Run directly
run.bat
```

### Command line

```bash
# Compile all Java files
javac -Xlint:-deprecation -Xlint:-unchecked -cp .;rsyntaxtextarea-3.0.0-SNAPSHOT.jar com/dinkygames/graaleditor/*.java com/dinkygames/graaleditor/undo/*.java org/eclipse/jdt/internal/jarinjarloader/*.java

# Run from JAR (after building)
java -jar bin/GraalSuite.jar

# -----------------------------------

# Run directly 
java -cp .;rsyntaxtextarea-3.0.0-SNAPSHOT.jar com.dinkygames.graaleditor.GraalEditor

```

## Project Structure

- `com.dinkygames.graaleditor`: Main editor classes
- `org.eclipse.jdt.internal.jarinjarloader`: JAR-in-JAR loader utility
- `res/`: Resources including icons and default levels

## Notes

- First run prompts to set the Graal directory
- Creates a cache file in the Graal directory for faster loading
- Editing tileset definitions requires a restart
