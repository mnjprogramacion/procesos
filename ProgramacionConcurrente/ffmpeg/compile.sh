#!/bin/bash
echo "Compilando FFmpeg Video Converter..."

# Crear directorio bin si no existe
mkdir -p bin

# Compilar todos los archivos Java
javac -d bin -sourcepath src src/Main.java src/model/*.java src/service/*.java src/ui/*.java src/console/*.java

if [ $? -ne 0 ]; then
    echo "Error de compilación!"
    exit 1
fi

echo "Compilación exitosa!"

# Crear MANIFEST
echo "Main-Class: Main" > MANIFEST.MF

# Crear JAR
jar cvfm ffmpeg-converter.jar MANIFEST.MF -C bin .

if [ $? -ne 0 ]; then
    echo "Error creando JAR!"
    exit 1
fi

echo ""
echo "JAR creado: ffmpeg-converter.jar"
echo ""
echo "Para ejecutar:"
echo "  java -jar ffmpeg-converter.jar          (modo GUI)"
echo "  java -jar ffmpeg-converter.jar -x       (modo consola)"
