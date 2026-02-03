package service;

import model.VideoFile;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.*;

/**
 * Servicio para buscar archivos de vídeo recursivamente con filtros regex.
 */
public class FileScanner {
    private FFprobeService ffprobeService;
    
    private static final Set<String> VIDEO_EXTENSIONS = new HashSet<>(Arrays.asList(
        "mp4", "mkv", "avi", "mov", "wmv", "flv", "webm", "m4v", "mpeg", "mpg", "3gp", "ogv"
    ));
    
    public FileScanner(FFprobeService ffprobeService) {
        this.ffprobeService = ffprobeService;
    }
    
    /**
     * Busca archivos de vídeo recursivamente en un directorio.
     * @param path Directorio base
     * @param regex Expresión regular para filtrar nombres (puede ser null)
     * @param recursive Si buscar recursivamente
     */
    public List<File> scanDirectory(String path, String regex, boolean recursive) throws IOException {
        List<File> files = new ArrayList<>();
        Path startPath = Paths.get(path);
        
        Pattern pattern = null;
        if (regex != null && !regex.isEmpty()) {
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        }
        
        final Pattern finalPattern = pattern;
        
        if (recursive) {
            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (isVideoFile(file.toFile()) && matchesPattern(file, finalPattern)) {
                        files.add(file.toFile());
                    }
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(startPath)) {
                for (Path file : stream) {
                    if (Files.isRegularFile(file) && isVideoFile(file.toFile()) 
                            && matchesPattern(file, finalPattern)) {
                        files.add(file.toFile());
                    }
                }
            }
        }
        
        return files;
    }
    
    /**
     * Analiza archivos y filtra los que excedan un umbral de bits/segundo.
     * @param files Lista de archivos a analizar
     * @param minDuration Duración mínima en segundos (0 para no filtrar)
     * @param maxBitsPerSecond Máximo bits/segundo permitido
     */
    public List<VideoFile> filterByBitrate(List<File> files, double minDuration, 
                                           double maxBitsPerSecond) throws IOException {
        List<VideoFile> result = new ArrayList<>();
        
        for (File file : files) {
            VideoFile video = ffprobeService.analyze(file);
            
            // Filtrar por duración mínima
            if (minDuration > 0 && video.getDuration() < minDuration) {
                continue;
            }
            
            // Filtrar por bitrate máximo
            if (maxBitsPerSecond > 0 && video.calculateBitsPerSecond() > maxBitsPerSecond) {
                result.add(video);
            } else if (maxBitsPerSecond <= 0) {
                result.add(video);
            }
        }
        
        return result;
    }
    
    /**
     * Analiza archivos y filtra por multiplicador de tamaño/duración.
     * @param files Lista de archivos
     * @param bytesPerSecondMultiplier Multiplicador máximo de bytes/segundo
     */
    public List<VideoFile> filterBySizeMultiplier(List<File> files, double bytesPerSecondMultiplier) 
            throws IOException {
        List<VideoFile> result = new ArrayList<>();
        
        for (File file : files) {
            VideoFile video = ffprobeService.analyze(file);
            
            if (video.getDuration() > 0) {
                double bytesPerSecond = video.getFileSize() / video.getDuration();
                if (bytesPerSecond > bytesPerSecondMultiplier) {
                    result.add(video);
                }
            }
        }
        
        return result;
    }
    
    /**
     * Escanea múltiples directorios.
     */
    public List<File> scanDirectories(List<String> paths, String regex, boolean recursive) 
            throws IOException {
        List<File> allFiles = new ArrayList<>();
        
        for (String path : paths) {
            allFiles.addAll(scanDirectory(path, regex, recursive));
        }
        
        return allFiles;
    }
    
    private boolean isVideoFile(File file) {
        String name = file.getName().toLowerCase();
        int dot = name.lastIndexOf('.');
        if (dot > 0) {
            String ext = name.substring(dot + 1);
            return VIDEO_EXTENSIONS.contains(ext);
        }
        return false;
    }
    
    private boolean matchesPattern(Path file, Pattern pattern) {
        if (pattern == null) return true;
        return pattern.matcher(file.getFileName().toString()).find();
    }
}
