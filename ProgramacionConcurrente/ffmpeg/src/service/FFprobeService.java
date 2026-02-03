package service;

import model.VideoFile;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

/**
 * Servicio para analizar archivos de vídeo usando FFprobe.
 */
public class FFprobeService {
    private String ffprobePath;
    
    public FFprobeService(String ffprobePath) {
        this.ffprobePath = ffprobePath;
    }
    
    /**
     * Analiza un archivo de vídeo y obtiene sus propiedades.
     */
    public VideoFile analyze(File file) throws IOException {
        VideoFile video = new VideoFile(file);
        
        List<String> command = new ArrayList<>();
        command.add(ffprobePath);
        command.add("-v");
        command.add("quiet");
        command.add("-print_format");
        command.add("flat");
        command.add("-show_format");
        command.add("-show_streams");
        command.add(file.getAbsolutePath());
        
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        
        while ((line = reader.readLine()) != null) {
            parseProperty(video, line);
        }
        
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return video;
    }
    
    /**
     * Obtiene solo la duración de un archivo (más rápido).
     */
    public double getDuration(File file) throws IOException {
        List<String> command = new ArrayList<>();
        command.add(ffprobePath);
        command.add("-v");
        command.add("error");
        command.add("-show_entries");
        command.add("format=duration");
        command.add("-of");
        command.add("default=noprint_wrappers=1:nokey=1");
        command.add(file.getAbsolutePath());
        
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();
        
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        if (line != null && !line.isEmpty()) {
            try {
                return Double.parseDouble(line.trim());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
    
    private void parseProperty(VideoFile video, String line) {
        // format.duration="123.456"
        if (line.startsWith("format.duration=")) {
            video.setDuration(parseDouble(line));
        }
        // format.bit_rate="1234567"
        else if (line.startsWith("format.bit_rate=")) {
            video.setBitrate(parseDouble(line));
        }
        // format.format_name="mp4,m4a"
        else if (line.startsWith("format.format_name=")) {
            video.setFormat(parseString(line));
        }
        // streams.stream.0.width=1920
        else if (line.contains(".width=") && video.getWidth() == 0) {
            video.setWidth((int) parseDouble(line));
        }
        // streams.stream.0.height=1080
        else if (line.contains(".height=") && video.getHeight() == 0) {
            video.setHeight((int) parseDouble(line));
        }
        // streams.stream.0.codec_name="h264"
        else if (line.contains(".codec_name=")) {
            String codec = parseString(line);
            if (line.contains("stream.0")) {
                video.setVideoCodec(codec);
            } else if (line.contains("stream.1")) {
                video.setAudioCodec(codec);
            }
        }
        // streams.stream.1.sample_rate="48000"
        else if (line.contains(".sample_rate=")) {
            video.setAudioSampleRate((int) parseDouble(line));
        }
        // streams.stream.X.bit_rate="128000"
        else if (line.contains(".bit_rate=") && line.contains("stream.1")) {
            video.setAudioBitrate((int) (parseDouble(line) / 1000));
        }
    }
    
    private double parseDouble(String line) {
        Pattern p = Pattern.compile("=\\\"?([0-9.]+)\\\"?");
        Matcher m = p.matcher(line);
        if (m.find()) {
            try {
                return Double.parseDouble(m.group(1));
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
    
    private String parseString(String line) {
        Pattern p = Pattern.compile("=\\\"([^\\\"]+)\\\"");
        Matcher m = p.matcher(line);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }
}
