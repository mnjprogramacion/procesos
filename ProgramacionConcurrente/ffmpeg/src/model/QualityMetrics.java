package model;

/**
 * Almacena métricas de calidad comparando vídeo original vs convertido.
 * PSNR: Peak Signal-to-Noise Ratio (mayor = mejor, >40 dB excelente)
 * SSIM: Structural Similarity Index (0-1, mayor = mejor, >0.95 excelente)
 */
public class QualityMetrics {
    private double psnr = -1;
    private double ssim = -1;
    private long originalSize;
    private long convertedSize;
    private double originalDuration;
    private double convertedDuration;
    
    public QualityMetrics() {}
    
    public double getPsnr() { return psnr; }
    public void setPsnr(double psnr) { this.psnr = psnr; }
    
    public double getSsim() { return ssim; }
    public void setSsim(double ssim) { this.ssim = ssim; }
    
    public long getOriginalSize() { return originalSize; }
    public void setOriginalSize(long originalSize) { this.originalSize = originalSize; }
    
    public long getConvertedSize() { return convertedSize; }
    public void setConvertedSize(long convertedSize) { this.convertedSize = convertedSize; }
    
    public double getOriginalDuration() { return originalDuration; }
    public void setOriginalDuration(double originalDuration) { this.originalDuration = originalDuration; }
    
    public double getConvertedDuration() { return convertedDuration; }
    public void setConvertedDuration(double convertedDuration) { this.convertedDuration = convertedDuration; }
    
    /**
     * Calcula el porcentaje de reducción de tamaño.
     */
    public double getSizeReductionPercent() {
        if (originalSize == 0) return 0;
        return (1.0 - ((double) convertedSize / originalSize)) * 100;
    }
    
    /**
     * Evalúa la calidad basándose en PSNR.
     */
    public String getPsnrQuality() {
        if (psnr < 0) return "No disponible";
        if (psnr >= 50) return "Excelente (prácticamente idéntico)";
        if (psnr >= 40) return "Muy bueno";
        if (psnr >= 30) return "Bueno";
        if (psnr >= 20) return "Aceptable";
        return "Pobre";
    }
    
    /**
     * Evalúa la calidad basándose en SSIM.
     */
    public String getSsimQuality() {
        if (ssim < 0) return "No disponible";
        if (ssim >= 0.99) return "Excelente (prácticamente idéntico)";
        if (ssim >= 0.95) return "Muy bueno";
        if (ssim >= 0.90) return "Bueno";
        if (ssim >= 0.80) return "Aceptable";
        return "Pobre";
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Métricas de Calidad ===\n");
        
        if (psnr >= 0) {
            sb.append(String.format("PSNR: %.2f dB - %s\n", psnr, getPsnrQuality()));
        }
        if (ssim >= 0) {
            sb.append(String.format("SSIM: %.4f - %s\n", ssim, getSsimQuality()));
        }
        
        sb.append(String.format("Tamaño original: %s\n", formatSize(originalSize)));
        sb.append(String.format("Tamaño convertido: %s\n", formatSize(convertedSize)));
        sb.append(String.format("Reducción: %.1f%%\n", getSizeReductionPercent()));
        
        return sb.toString();
    }
    
    private String formatSize(long size) {
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.2f KB", size / 1024.0);
        if (size < 1024 * 1024 * 1024) return String.format("%.2f MB", size / (1024.0 * 1024));
        return String.format("%.2f GB", size / (1024.0 * 1024 * 1024));
    }
}
