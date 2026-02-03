package service;

import model.VideoSettings;
import model.VideoFile;
import model.QualityMetrics;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.*;

/**
 * Servicio principal para ejecutar comandos FFmpeg.
 * Usa ProcessBuilder con List<String> para manejar espacios correctamente.
 */
public class FFmpegService {
    private String ffmpegPath;
    private String ffprobePath;
    private Consumer<String> outputListener;
    private volatile boolean cancelled = false;
    private Process currentProcess;
    
    public FFmpegService(String ffmpegPath) {
        this.ffmpegPath = ffmpegPath;
        // Asumir ffprobe junto a ffmpeg
        this.ffprobePath = ffmpegPath.replace("ffmpeg", "ffprobe");
    }
    
    public void setFFprobePath(String path) {
        this.ffprobePath = path;
    }
    
    public void setOutputListener(Consumer<String> listener) {
        this.outputListener = listener;
    }
    
    public void cancel() {
        cancelled = true;
        if (currentProcess != null) {
            currentProcess.destroyForcibly();
        }
    }
    
    /**
     * Convierte un archivo de vídeo según la configuración.
     * @return Archivo de salida generado
     */
    public File convert(VideoFile input, VideoSettings settings, String outputPath) throws IOException {
        cancelled = false;
        
        List<String> command = new ArrayList<>();
        command.add(ffmpegPath);
        command.add("-y");  // Sobrescribir sin preguntar
        command.add("-i");
        command.add(input.getPath());
        
        // Filtros de vídeo
        List<String> videoFilters = new ArrayList<>();
        if (settings.hasResolution()) {
            videoFilters.add(settings.getResolutionFilter());
        }
        
        if (!videoFilters.isEmpty()) {
            command.add("-vf");
            command.add(String.join(",", videoFilters));
        }
        
        // Codec de vídeo
        if (settings.getVideoCodec() != null && !settings.getVideoCodec().isEmpty()) {
            command.add("-c:v");
            command.add(settings.getVideoCodec());
            
            // Preset para x264/x265
            if (settings.getVideoCodec().contains("264") || settings.getVideoCodec().contains("265")) {
                command.add("-preset");
                command.add(settings.getPreset());
                
                // CRF
                if (settings.getCrf() > 0) {
                    command.add("-crf");
                    command.add(String.valueOf(settings.getCrf()));
                }
            }
        }
        
        // Bitrate de vídeo
        if (settings.getVideoBitrate() > 0) {
            command.add("-b:v");
            command.add(settings.getVideoBitrate() + "k");
        }
        
        // Codec de audio
        if (settings.getAudioCodec() != null && !settings.getAudioCodec().isEmpty()) {
            command.add("-c:a");
            command.add(settings.getAudioCodec());
        }
        
        // Bitrate de audio
        if (settings.getAudioBitrate() > 0) {
            command.add("-b:a");
            command.add(settings.getAudioBitrate() + "k");
        }
        
        // Sample rate
        if (settings.getAudioSampleRate() > 0) {
            command.add("-ar");
            command.add(String.valueOf(settings.getAudioSampleRate()));
        }
        
        // Archivo de salida
        String output = buildOutputPath(input.getPath(), outputPath, settings.getOutputFormat());
        command.add(output);
        
        log("Ejecutando: " + String.join(" ", command));
        
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        currentProcess = pb.start();
        
        // Leer salida para evitar bloqueo del buffer
        BufferedReader reader = new BufferedReader(new InputStreamReader(currentProcess.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null && !cancelled) {
            log(line);
        }
        
        try {
            int exitCode = currentProcess.waitFor();
            if (exitCode != 0 && !cancelled) {
                throw new IOException("FFmpeg terminó con código de error: " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Conversión interrumpida");
        }
        
        return new File(output);
    }
    
    /**
     * Genera un thumbnail de un frame específico.
     */
    public File createThumbnail(VideoFile input, VideoSettings settings, String outputPath) throws IOException {
        List<String> command = new ArrayList<>();
        command.add(ffmpegPath);
        command.add("-y");
        command.add("-ss");
        command.add(settings.getThumbnailTime() != null ? settings.getThumbnailTime() : "00:00:01");
        command.add("-i");
        command.add(input.getPath());
        command.add("-vframes");
        command.add("1");
        
        // Resolución del thumbnail
        if (settings.getThumbnailWidth() > 0 && settings.getThumbnailHeight() > 0) {
            command.add("-vf");
            command.add("scale=" + settings.getThumbnailWidth() + ":" + settings.getThumbnailHeight());
        }
        
        // Calidad
        command.add("-q:v");
        command.add(String.valueOf(settings.getThumbnailQuality()));
        
        // Salida
        String ext = settings.getThumbnailFormat();
        if (ext == null || ext.isEmpty()) ext = "jpg";
        String output = outputPath != null ? outputPath : 
                       input.getPath().replaceAll("\\.[^.]+$", "_thumb." + ext);
        command.add(output);
        
        log("Generando thumbnail: " + String.join(" ", command));
        
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while (reader.readLine() != null) { /* consumir salida */ }
        
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return new File(output);
    }
    
    /**
     * Compara dos vídeos y calcula métricas de calidad (PSNR y SSIM).
     */
    public QualityMetrics compareQuality(File original, File converted) throws IOException {
        QualityMetrics metrics = new QualityMetrics();
        metrics.setOriginalSize(original.length());
        metrics.setConvertedSize(converted.length());
        
        // Calcular PSNR
        double psnr = calculatePSNR(original, converted);
        metrics.setPsnr(psnr);
        
        // Calcular SSIM
        double ssim = calculateSSIM(original, converted);
        metrics.setSsim(ssim);
        
        return metrics;
    }
    
    private double calculatePSNR(File original, File converted) throws IOException {
        List<String> command = new ArrayList<>();
        command.add(ffmpegPath);
        command.add("-i");
        command.add(converted.getAbsolutePath());
        command.add("-i");
        command.add(original.getAbsolutePath());
        command.add("-lavfi");
        command.add("psnr=stats_file=-");
        command.add("-f");
        command.add("null");
        command.add("-");
        
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        double psnr = -1;
        
        Pattern pattern = Pattern.compile("average:([0-9.]+)");
        while ((line = reader.readLine()) != null) {
            Matcher m = pattern.matcher(line);
            if (m.find()) {
                try {
                    psnr = Double.parseDouble(m.group(1));
                } catch (NumberFormatException e) { }
            }
            // También buscar en formato PSNR y avg
            if (line.contains("PSNR") && line.contains("average")) {
                Pattern p2 = Pattern.compile("average:([0-9.]+)");
                Matcher m2 = p2.matcher(line);
                if (m2.find()) {
                    try {
                        psnr = Double.parseDouble(m2.group(1));
                    } catch (NumberFormatException e) { }
                }
            }
        }
        
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return psnr;
    }
    
    private double calculateSSIM(File original, File converted) throws IOException {
        List<String> command = new ArrayList<>();
        command.add(ffmpegPath);
        command.add("-i");
        command.add(converted.getAbsolutePath());
        command.add("-i");
        command.add(original.getAbsolutePath());
        command.add("-lavfi");
        command.add("ssim=stats_file=-");
        command.add("-f");
        command.add("null");
        command.add("-");
        
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        double ssim = -1;
        
        Pattern pattern = Pattern.compile("All:([0-9.]+)");
        while ((line = reader.readLine()) != null) {
            Matcher m = pattern.matcher(line);
            if (m.find()) {
                try {
                    ssim = Double.parseDouble(m.group(1));
                } catch (NumberFormatException e) { }
            }
        }
        
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return ssim;
    }
    
    private String buildOutputPath(String inputPath, String outputDir, String newFormat) {
        File input = new File(inputPath);
        String baseName = input.getName().replaceAll("\\.[^.]+$", "");
        
        String ext = (newFormat != null && !newFormat.isEmpty()) ? newFormat : 
                    inputPath.substring(inputPath.lastIndexOf('.') + 1);
        
        String dir = (outputDir != null && !outputDir.isEmpty()) ? outputDir : input.getParent();
        
        return new File(dir, baseName + "_converted." + ext).getAbsolutePath();
    }
    
    private void log(String message) {
        if (outputListener != null) {
            outputListener.accept(message);
        } else {
            System.out.println(message);
        }
    }
}
