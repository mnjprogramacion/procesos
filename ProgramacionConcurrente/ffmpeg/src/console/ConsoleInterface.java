package console;

import model.*;
import service.*;

import java.io.*;
import java.util.*;

/**
 * Interfaz de consola para cuando no hay entorno gráfico disponible.
 */
public class ConsoleInterface {
    private String ffmpegPath;
    private FFmpegService ffmpegService;
    private FFprobeService ffprobeService;
    private FileScanner fileScanner;
    private Scanner scanner;
    
    public ConsoleInterface(String ffmpegPath) {
        this.ffmpegPath = ffmpegPath;
        this.ffmpegService = new FFmpegService(ffmpegPath);
        this.ffprobeService = new FFprobeService(ffmpegPath.replace("ffmpeg", "ffprobe"));
        this.fileScanner = new FileScanner(ffprobeService);
        this.scanner = new Scanner(System.in);
    }
    
    public void run() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║ FFmpeg Video Converter - Modo Consola  ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("FFmpeg: " + ffmpegPath);
        System.out.println();
        
        boolean running = true;
        while (running) {
            showMenu();
            String option = scanner.nextLine().trim();
            
            switch (option) {
                case "1": convertVideo(); break;
                case "2": convertBatch(); break;
                case "3": createThumbnail(); break;
                case "4": analyzeFile(); break;
                case "5": compareQuality(); break;
                case "6": filterByBitrate(); break;
                case "0": running = false; break;
                default: System.out.println("Opción no válida");
            }
        }
        
        System.out.println("¡Hasta luego!");
    }
    
    private void showMenu() {
        System.out.println("\n--- MENÚ ---");
        System.out.println("1. Convertir un vídeo");
        System.out.println("2. Conversión por lotes (carpeta)");
        System.out.println("3. Crear thumbnail");
        System.out.println("4. Analizar archivo");
        System.out.println("5. Comparar calidad (PSNR/SSIM)");
        System.out.println("6. Filtrar archivos por bitrate");
        System.out.println("0. Salir");
        System.out.print("Selecciona: ");
    }
    
    private void convertVideo() {
        System.out.print("Ruta del archivo: ");
        String inputPath = scanner.nextLine().trim();
        
        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            System.out.println("Archivo no encontrado");
            return;
        }
        
        try {
            VideoFile video = ffprobeService.analyze(inputFile);
            System.out.println("Archivo: " + video);
            
            VideoSettings settings = configureSettings();
            
            System.out.print("Directorio de salida (Enter para mismo): ");
            String outputDir = scanner.nextLine().trim();
            if (outputDir.isEmpty()) outputDir = null;
            
            System.out.println("\nConvirtiendo...");
            File output = ffmpegService.convert(video, settings, outputDir);
            System.out.println("¡Completado! Archivo: " + output.getAbsolutePath());
            
            System.out.print("¿Comparar calidad? (s/n): ");
            if (scanner.nextLine().trim().toLowerCase().startsWith("s")) {
                QualityMetrics metrics = ffmpegService.compareQuality(inputFile, output);
                System.out.println(metrics);
            }
            
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void convertBatch() {
        System.out.print("Ruta de la carpeta: ");
        String path = scanner.nextLine().trim();
        
        System.out.print("Expresión regular (Enter para todas): ");
        String regex = scanner.nextLine().trim();
        if (regex.isEmpty()) regex = null;
        
        System.out.print("¿Recursivo? (s/n): ");
        boolean recursive = scanner.nextLine().trim().toLowerCase().startsWith("s");
        
        try {
            List<File> files = fileScanner.scanDirectory(path, regex, recursive);
            System.out.println("Encontrados: " + files.size() + " archivos");
            
            if (files.isEmpty()) return;
            
            VideoSettings settings = configureSettings();
            
            System.out.print("Directorio de salida (Enter para misma ubicación): ");
            String outputDir = scanner.nextLine().trim();
            if (outputDir.isEmpty()) outputDir = null;
            
            int success = 0;
            for (File file : files) {
                try {
                    System.out.println("\nConvirtiendo: " + file.getName());
                    VideoFile video = ffprobeService.analyze(file);
                    ffmpegService.convert(video, settings, outputDir);
                    success++;
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            
            System.out.println("\n¡Completado! " + success + "/" + files.size() + " archivos");
            
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void createThumbnail() {
        System.out.print("Ruta del vídeo: ");
        String inputPath = scanner.nextLine().trim();
        
        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            System.out.println("Archivo no encontrado");
            return;
        }
        
        VideoSettings settings = new VideoSettings();
        
        System.out.print("Tiempo (HH:MM:SS o segundos, Enter=1s): ");
        String time = scanner.nextLine().trim();
        settings.setThumbnailTime(time.isEmpty() ? "1" : time);
        
        System.out.print("Ancho (Enter=original): ");
        String w = scanner.nextLine().trim();
        if (!w.isEmpty()) settings.setThumbnailWidth(Integer.parseInt(w));
        
        System.out.print("Alto (Enter=original): ");
        String h = scanner.nextLine().trim();
        if (!h.isEmpty()) settings.setThumbnailHeight(Integer.parseInt(h));
        
        System.out.print("Formato (jpg/png, Enter=jpg): ");
        String fmt = scanner.nextLine().trim();
        settings.setThumbnailFormat(fmt.isEmpty() ? "jpg" : fmt);
        
        System.out.print("Calidad 1-31 (Enter=2): ");
        String q = scanner.nextLine().trim();
        settings.setThumbnailQuality(q.isEmpty() ? 2 : Integer.parseInt(q));
        
        try {
            VideoFile video = new VideoFile(inputFile);
            File thumb = ffmpegService.createThumbnail(video, settings, null);
            System.out.println("Thumbnail creado: " + thumb.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void analyzeFile() {
        System.out.print("Ruta del archivo: ");
        String path = scanner.nextLine().trim();
        
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("Archivo no encontrado");
            return;
        }
        
        try {
            VideoFile video = ffprobeService.analyze(file);
            System.out.println("\n=== Información del archivo ===");
            System.out.println("Nombre: " + video.getName());
            System.out.println("Tamaño: " + video.getFileSizeFormatted());
            System.out.println("Duración: " + video.getDurationFormatted());
            System.out.println("Resolución: " + video.getResolution());
            System.out.println("Codec vídeo: " + video.getVideoCodec());
            System.out.println("Codec audio: " + video.getAudioCodec());
            System.out.println("Sample rate: " + video.getAudioSampleRate() + " Hz");
            System.out.printf("Bitrate: %.2f kbps%n", video.calculateBitsPerSecond() / 1000);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void compareQuality() {
        System.out.print("Ruta del archivo original: ");
        String orig = scanner.nextLine().trim();
        
        System.out.print("Ruta del archivo convertido: ");
        String conv = scanner.nextLine().trim();
        
        File origFile = new File(orig);
        File convFile = new File(conv);
        
        if (!origFile.exists() || !convFile.exists()) {
            System.out.println("Archivo(s) no encontrado(s)");
            return;
        }
        
        try {
            System.out.println("Calculando métricas (puede tardar)...");
            QualityMetrics metrics = ffmpegService.compareQuality(origFile, convFile);
            System.out.println(metrics);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void filterByBitrate() {
        System.out.print("Ruta de la carpeta: ");
        String path = scanner.nextLine().trim();
        
        System.out.print("Duración mínima en segundos (Enter=0): ");
        String minDur = scanner.nextLine().trim();
        double minDuration = minDur.isEmpty() ? 0 : Double.parseDouble(minDur);
        
        System.out.print("Máximo bits/segundo (Enter=5000000 ~5Mbps): ");
        String maxBps = scanner.nextLine().trim();
        double maxBitsPerSecond = maxBps.isEmpty() ? 5000000 : Double.parseDouble(maxBps);
        
        System.out.print("Expresión regular (Enter=todas): ");
        String regex = scanner.nextLine().trim();
        if (regex.isEmpty()) regex = null;
        
        try {
            List<File> files = fileScanner.scanDirectory(path, regex, true);
            List<VideoFile> filtered = fileScanner.filterByBitrate(files, minDuration, maxBitsPerSecond);
            
            System.out.println("\n=== Archivos que exceden " + (maxBitsPerSecond/1000000) + " Mbps ===");
            for (VideoFile v : filtered) {
                System.out.printf("%s - %.2f Mbps%n", v.getName(), v.calculateBitsPerSecond() / 1000000);
            }
            System.out.println("Total: " + filtered.size() + " archivos");
            
            if (!filtered.isEmpty()) {
                System.out.print("\n¿Convertir estos archivos? (s/n): ");
                if (scanner.nextLine().trim().toLowerCase().startsWith("s")) {
                    VideoSettings settings = configureSettings();
                    for (VideoFile v : filtered) {
                        try {
                            System.out.println("Convirtiendo: " + v.getName());
                            ffmpegService.convert(v, settings, null);
                        } catch (IOException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                }
            }
            
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private VideoSettings configureSettings() {
        VideoSettings settings = new VideoSettings();
        
        System.out.println("\n--- Configuración ---");
        
        System.out.print("Ancho (Enter=original): ");
        String w = scanner.nextLine().trim();
        if (!w.isEmpty()) settings.setWidth(Integer.parseInt(w));
        
        System.out.print("Alto (Enter=original): ");
        String h = scanner.nextLine().trim();
        if (!h.isEmpty()) settings.setHeight(Integer.parseInt(h));
        
        System.out.print("Codec vídeo (libx264/libx265/copy, Enter=auto): ");
        String vc = scanner.nextLine().trim();
        if (!vc.isEmpty()) settings.setVideoCodec(vc);
        
        System.out.print("Bitrate vídeo en kbps (Enter=auto): ");
        String vb = scanner.nextLine().trim();
        if (!vb.isEmpty()) settings.setVideoBitrate(Integer.parseInt(vb));
        
        System.out.print("CRF 0-51 (Enter=auto): ");
        String crf = scanner.nextLine().trim();
        if (!crf.isEmpty()) settings.setCrf(Integer.parseInt(crf));
        
        System.out.print("Preset (ultrafast/fast/medium/slow, Enter=medium): ");
        String preset = scanner.nextLine().trim();
        if (!preset.isEmpty()) settings.setPreset(preset);
        
        System.out.print("Codec audio (aac/mp3/copy, Enter=auto): ");
        String ac = scanner.nextLine().trim();
        if (!ac.isEmpty()) settings.setAudioCodec(ac);
        
        System.out.print("Bitrate audio en kbps (Enter=auto): ");
        String ab = scanner.nextLine().trim();
        if (!ab.isEmpty()) settings.setAudioBitrate(Integer.parseInt(ab));
        
        System.out.print("Sample rate en Hz (Enter=original): ");
        String sr = scanner.nextLine().trim();
        if (!sr.isEmpty()) settings.setAudioSampleRate(Integer.parseInt(sr));
        
        System.out.print("Formato de salida (mp4/mkv/avi, Enter=original): ");
        String fmt = scanner.nextLine().trim();
        if (!fmt.isEmpty()) settings.setOutputFormat(fmt);
        
        return settings;
    }
}
