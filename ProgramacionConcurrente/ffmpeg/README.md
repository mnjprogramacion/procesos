# FFmpeg Video Converter

Aplicación Java + Swing para convertir vídeos usando FFmpeg.

## Características

- ✅ Interfaz gráfica (Swing) y modo consola (parámetro `-x`)
- ✅ Cambiar resolución, bitrate, codec de vídeo/audio, sample rate
- ✅ Cambiar extensión/formato de salida
- ✅ Drag & Drop de archivos y carpetas
- ✅ Procesamiento recursivo de carpetas
- ✅ Búsqueda de archivos con expresiones regulares
- ✅ Comparación de calidad con PSNR y SSIM
- ✅ Generación de thumbnails (portadas)
- ✅ Filtrado por bitrate (bits/segundo)
- ✅ Búsqueda automática de FFmpeg en el sistema
- ✅ Orientado a objetos con ProcessBuilder

## Estructura

```
src/
├── Main.java                    # Punto de entrada
├── model/
│   ├── VideoSettings.java       # Configuración de conversión
│   ├── VideoFile.java           # Archivo de vídeo
│   └── QualityMetrics.java      # Métricas PSNR/SSIM
├── service/
│   ├── FFmpegService.java       # Ejecuta ffmpeg
│   ├── FFprobeService.java      # Analiza archivos
│   ├── FFmpegLocator.java       # Busca/descarga ffmpeg
│   └── FileScanner.java         # Búsqueda recursiva
├── ui/
│   ├── MainFrame.java           # Ventana principal
│   └── SettingsPanel.java       # Panel de configuración
└── console/
    └── ConsoleInterface.java    # Modo consola
```

## Compilación

### Windows (PowerShell)
```powershell
.\compile.bat
```

### Linux/Mac
```bash
chmod +x compile.sh
./compile.sh
```

### Manual
```bash
# Compilar
javac -d bin src/Main.java src/model/*.java src/service/*.java src/ui/*.java src/console/*.java

# Crear JAR
jar cvfm ffmpeg-converter.jar MANIFEST.MF -C bin .
```

## Ejecución

### Modo GUI (automático si hay display)
```bash
java -jar ffmpeg-converter.jar
```

### Modo consola (forzar)
```bash
java -jar ffmpeg-converter.jar -x
```

### Especificar ruta de FFmpeg
```bash
java -jar ffmpeg-converter.jar -ffmpeg "C:\ffmpeg\bin\ffmpeg.exe"
```

## Requisitos

- Java 11 o superior
- FFmpeg instalado (se busca automáticamente o redirige a descarga)

## Uso

### GUI
1. Arrastra archivos/carpetas a la lista o usa "Añadir"
2. Configura resolución, codecs, bitrate, etc.
3. Click en "Convertir"
4. Opcional: Comparar calidad con PSNR/SSIM

### Consola
Sigue el menú interactivo para:
1. Convertir archivos individuales
2. Conversión por lotes
3. Crear thumbnails
4. Analizar archivos
5. Comparar calidad
6. Filtrar por bitrate
