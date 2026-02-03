package service;

import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Localiza FFmpeg en el sistema o ayuda a descargarlo.
 */
public class FFmpegLocator {
    private String customPath = null;
    private String ffmpegPath = null;
    private String ffprobePath = null;
    
    private static final String DOWNLOAD_URL = "https://ffmpeg.org/download.html";
    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("win");
    private static final String FFMPEG_EXE = IS_WINDOWS ? "ffmpeg.exe" : "ffmpeg";
    private static final String FFPROBE_EXE = IS_WINDOWS ? "ffprobe.exe" : "ffprobe";
    
    public void setCustomPath(String path) {
        this.customPath = path;
    }
    
    /**
     * Intenta localizar ffmpeg.
     * @return Ruta a ffmpeg o null si no se encuentra
     */
    public String locate() {
        // Primero verificar ruta personalizada
        if (customPath != null) {
            File custom = new File(customPath);
            if (custom.exists() && custom.canExecute()) {
                ffmpegPath = customPath;
                // Buscar ffprobe junto a ffmpeg
                File probe = new File(custom.getParent(), FFPROBE_EXE);
                if (probe.exists()) {
                    ffprobePath = probe.getAbsolutePath();
                }
                return ffmpegPath;
            }
        }
        
        // Verificar si está en PATH
        if (isInPath("ffmpeg")) {
            ffmpegPath = "ffmpeg";
            ffprobePath = "ffprobe";
            return ffmpegPath;
        }
        
        // Buscar en ubicaciones comunes
        String[] commonPaths = IS_WINDOWS ? 
            new String[] {
                "C:\\ffmpeg\\bin\\ffmpeg.exe",
                "C:\\Program Files\\ffmpeg\\bin\\ffmpeg.exe",
                "C:\\Program Files (x86)\\ffmpeg\\bin\\ffmpeg.exe",
                System.getProperty("user.home") + "\\ffmpeg\\bin\\ffmpeg.exe"
            } :
            new String[] {
                "/usr/bin/ffmpeg",
                "/usr/local/bin/ffmpeg",
                "/opt/ffmpeg/bin/ffmpeg",
                System.getProperty("user.home") + "/ffmpeg/bin/ffmpeg"
            };
        
        for (String path : commonPaths) {
            File f = new File(path);
            if (f.exists() && f.canExecute()) {
                ffmpegPath = path;
                String probePath = path.replace("ffmpeg", "ffprobe");
                if (new File(probePath).exists()) {
                    ffprobePath = probePath;
                }
                return ffmpegPath;
            }
        }
        
        return null;
    }
    
    /**
     * Busca ffmpeg recursivamente en el sistema usando comandos.
     */
    public String searchInSystem() {
        System.out.println("Buscando ffmpeg en el sistema (puede tardar)...");
        
        try {
            List<String> command = new ArrayList<>();
            if (IS_WINDOWS) {
                command.add("where");
                command.add("/R");
                command.add("C:\\");
                command.add(FFMPEG_EXE);
            } else {
                command.add("find");
                command.add("/");
                command.add("-name");
                command.add(FFMPEG_EXE);
                command.add("-type");
                command.add("f");
                command.add("2>/dev/null");
            }
            
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                File f = new File(line.trim());
                if (f.exists() && f.canExecute()) {
                    ffmpegPath = f.getAbsolutePath();
                    File probe = new File(f.getParent(), FFPROBE_EXE);
                    if (probe.exists()) {
                        ffprobePath = probe.getAbsolutePath();
                    }
                    process.destroy();
                    return ffmpegPath;
                }
            }
            
            process.waitFor();
        } catch (Exception e) {
            System.err.println("Error buscando ffmpeg: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Abre la página de descarga de ffmpeg en el navegador.
     */
    public void openDownloadPage() {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(DOWNLOAD_URL));
            }
        } catch (Exception e) {
            System.out.println("Visita: " + DOWNLOAD_URL);
        }
    }
    
    private boolean isInPath(String executable) {
        try {
            List<String> command = new ArrayList<>();
            if (IS_WINDOWS) {
                command.add("where");
                command.add(executable);
            } else {
                command.add("which");
                command.add(executable);
            }
            
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getFFmpegPath() { return ffmpegPath; }
    public String getFFprobePath() { return ffprobePath; }
}
