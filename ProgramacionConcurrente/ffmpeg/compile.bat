@echo off
echo Compilando FFmpeg Video Converter...

REM Crear directorio bin si no existe
if not exist bin mkdir bin

REM Compilar todos los archivos Java
javac -d bin -sourcepath src src\Main.java src\model\*.java src\service\*.java src\ui\*.java src\console\*.java

if %ERRORLEVEL% NEQ 0 (
    echo Error de compilacion!
    pause
    exit /b 1
)

echo Compilacion exitosa!

REM Crear MANIFEST
echo Main-Class: Main> MANIFEST.MF

REM Crear JAR
jar cvfm ffmpeg-converter.jar MANIFEST.MF -C bin .

if %ERRORLEVEL% NEQ 0 (
    echo Error creando JAR!
    pause
    exit /b 1
)

echo.
echo JAR creado: ffmpeg-converter.jar
echo.
echo Para ejecutar:
echo   java -jar ffmpeg-converter.jar          (modo GUI)
echo   java -jar ffmpeg-converter.jar -x       (modo consola)
echo.
pause
