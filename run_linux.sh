#!/bin/bash

javac -Xlint:-deprecation -Xlint:-unchecked -cp .:rsyntaxtextarea-3.0.0-SNAPSHOT.jar com/dinkygames/graaleditor/*.java com/dinkygames/graaleditor/undo/*.java org/eclipse/jdt/internal/jarinjarloader/*.java

if [ $? -eq 0 ]; then
    echo
    echo "Compilation successful! Running the application..."
    java -Dswing.defaultlaf=javax.swing.plaf.metal.MetalLookAndFeel -cp .:rsyntaxtextarea-3.0.0-SNAPSHOT.jar com.dinkygames.graaleditor.GraalEditor
else
    echo
    echo "Compilation failed with errors."
fi