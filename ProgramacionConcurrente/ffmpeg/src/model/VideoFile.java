package model;

import java.io.File;

/**
 * Representa un archivo de vídeo con sus propiedades obtenidas de FFprobe.
 */
public class VideoFile {
    private File file;
    private double duration;        // segundos
    private long fileSize;          // bytes
    private int width;
    private int height;
    private String videoCodec;
    private String audioCodec;
    private int videoBitrate;       // kbps
    private int audioBitrate;       // kbps
    private int audioSampleRate;    // Hz
    private String format;
    private double bitrate;         // bits por segundo total
    
    public VideoFile(File file) {
        this.file = file;
        this.fileSize = file.length();
    }
    
    public File getFile() { return file; }
    public String getPath() { return file.getAbsolutePath(); }
    public String getName() { return file.getName(); }
    
    public double getDuration() { return duration; }
    public void setDuration(double duration) { this.duration = duration; }
    
    public long getFileSize() { return fileSize; }
    
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    
    public String getVideoCodec() { return videoCodec; }
    public void setVideoCodec(String videoCodec) { this.videoCodec = videoCodec; }
    
    public String getAudioCodec() { return audioCodec; }
    public void setAudioCodec(String audioCodec) { this.audioCodec = audioCodec; }
    
    public int getVideoBitrate() { return videoBitrate; }
    public void setVideoBitrate(int videoBitrate) { this.videoBitrate = videoBitrate; }
    
    public int getAudioBitrate() { return audioBitrate; }
    public void setAudioBitrate(int audioBitrate) { this.audioBitrate = audioBitrate; }
    
    public int getAudioSampleRate() { return audioSampleRate; }
    public void setAudioSampleRate(int audioSampleRate) { this.audioSampleRate = audioSampleRate; }
    
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    
    public double getBitrate() { return bitrate; }
    public void setBitrate(double bitrate) { this.bitrate = bitrate; }
    
    /**
     * Calcula los bits por segundo del archivo.
     */
    public double calculateBitsPerSecond() {
        if (duration > 0) {
            return (fileSize * 8.0) / duration;
        }
        return bitrate;
    }
    
    /**
     * Verifica si el archivo excede un multiplicador de bits/segundo dado.
     * @param maxBitsPerSecondPerDuration máximo bits/segundo permitido por segundo de duración
     */
    public boolean exceedsBitrateThreshold(double maxBitsPerSecond) {
        return calculateBitsPerSecond() > maxBitsPerSecond;
    }
    
    /**
     * Comprueba si la duración es menor a los segundos especificados.
     */
    public boolean isShorterThan(double seconds) {
        return duration < seconds;
    }
    
    public String getResolution() {
        return width + "x" + height;
    }
    
    public String getFileSizeFormatted() {
        if (fileSize < 1024) return fileSize + " B";
        if (fileSize < 1024 * 1024) return String.format("%.2f KB", fileSize / 1024.0);
        if (fileSize < 1024 * 1024 * 1024) return String.format("%.2f MB", fileSize / (1024.0 * 1024));
        return String.format("%.2f GB", fileSize / (1024.0 * 1024 * 1024));
    }
    
    public String getDurationFormatted() {
        int hours = (int) (duration / 3600);
        int minutes = (int) ((duration % 3600) / 60);
        int seconds = (int) (duration % 60);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    
    @Override
    public String toString() {
        return String.format("%s [%s, %s, %s]", getName(), getResolution(), 
                           getDurationFormatted(), getFileSizeFormatted());
    }
}
