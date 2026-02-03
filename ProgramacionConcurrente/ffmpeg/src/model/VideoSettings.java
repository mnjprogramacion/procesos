package model;

/**
 * Configuración de conversión de vídeo.
 * Almacena todos los parámetros para la conversión con FFmpeg.
 */
public class VideoSettings {
    private int width = -1;              // -1 = mantener original
    private int height = -1;
    private int videoBitrate = -1;       // kbps, -1 = auto
    private int audioBitrate = -1;       // kbps
    private int audioSampleRate = -1;    // Hz
    private String videoCodec = null;    // null = copiar
    private String audioCodec = null;
    private String outputFormat = null;  // extensión de salida
    private int crf = -1;                // Calidad constante (0-51 para h264)
    private String preset = "medium";    // ultrafast, fast, medium, slow, veryslow
    
    // Para thumbnail
    private String thumbnailTime = null; // formato HH:MM:SS o segundos
    private int thumbnailWidth = -1;
    private int thumbnailHeight = -1;
    private String thumbnailFormat = "jpg";
    private int thumbnailQuality = 2;    // 1-31 para mjpeg (menor = mejor)
    
    public VideoSettings() {}
    
    // Getters y Setters
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    
    public int getVideoBitrate() { return videoBitrate; }
    public void setVideoBitrate(int videoBitrate) { this.videoBitrate = videoBitrate; }
    
    public int getAudioBitrate() { return audioBitrate; }
    public void setAudioBitrate(int audioBitrate) { this.audioBitrate = audioBitrate; }
    
    public int getAudioSampleRate() { return audioSampleRate; }
    public void setAudioSampleRate(int audioSampleRate) { this.audioSampleRate = audioSampleRate; }
    
    public String getVideoCodec() { return videoCodec; }
    public void setVideoCodec(String videoCodec) { this.videoCodec = videoCodec; }
    
    public String getAudioCodec() { return audioCodec; }
    public void setAudioCodec(String audioCodec) { this.audioCodec = audioCodec; }
    
    public String getOutputFormat() { return outputFormat; }
    public void setOutputFormat(String outputFormat) { this.outputFormat = outputFormat; }
    
    public int getCrf() { return crf; }
    public void setCrf(int crf) { this.crf = crf; }
    
    public String getPreset() { return preset; }
    public void setPreset(String preset) { this.preset = preset; }
    
    public String getThumbnailTime() { return thumbnailTime; }
    public void setThumbnailTime(String thumbnailTime) { this.thumbnailTime = thumbnailTime; }
    
    public int getThumbnailWidth() { return thumbnailWidth; }
    public void setThumbnailWidth(int thumbnailWidth) { this.thumbnailWidth = thumbnailWidth; }
    
    public int getThumbnailHeight() { return thumbnailHeight; }
    public void setThumbnailHeight(int thumbnailHeight) { this.thumbnailHeight = thumbnailHeight; }
    
    public String getThumbnailFormat() { return thumbnailFormat; }
    public void setThumbnailFormat(String thumbnailFormat) { this.thumbnailFormat = thumbnailFormat; }
    
    public int getThumbnailQuality() { return thumbnailQuality; }
    public void setThumbnailQuality(int thumbnailQuality) { this.thumbnailQuality = thumbnailQuality; }
    
    public boolean hasResolution() {
        return width > 0 && height > 0;
    }
    
    public String getResolutionFilter() {
        if (!hasResolution()) return null;
        return "scale=" + width + ":" + height;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("VideoSettings{");
        if (hasResolution()) sb.append("res=").append(width).append("x").append(height).append(", ");
        if (videoBitrate > 0) sb.append("vBitrate=").append(videoBitrate).append("k, ");
        if (audioBitrate > 0) sb.append("aBitrate=").append(audioBitrate).append("k, ");
        if (audioSampleRate > 0) sb.append("sampleRate=").append(audioSampleRate).append("Hz, ");
        if (videoCodec != null) sb.append("vCodec=").append(videoCodec).append(", ");
        if (audioCodec != null) sb.append("aCodec=").append(audioCodec).append(", ");
        if (outputFormat != null) sb.append("format=").append(outputFormat).append(", ");
        if (crf > 0) sb.append("crf=").append(crf).append(", ");
        sb.append("preset=").append(preset).append("}");
        return sb.toString();
    }
}
