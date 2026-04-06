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
        // Paso 1: ruta personalizada
        if (customPath != null) {
            System.out.println("[Paso 1/3] Verificando ruta personalizada: " + customPath);
            File custom = new File(customPath);
            if (custom.exists() && custom.canExecute()) {
                ffmpegPath = customPath;
                File probe = new File(custom.getParent(), FFPROBE_EXE);
                if (probe.exists()) {
                    ffprobePath = probe.getAbsolutePath();
                }
                System.out.println("         -> Encontrado en ruta personalizada.");
                return ffmpegPath;
            } else {
                System.out.println("         -> No encontrado o no ejecutable. Continuando...");
            }
        }
        
        // Paso 2: verificar si está en PATH
        System.out.println("[Paso 2/3] Buscando " + FFMPEG_EXE + " en el PATH del sistema...");
        if (isInPath("ffmpeg")) {
            ffmpegPath = "ffmpeg";
            ffprobePath = "ffprobe";
            System.out.println("         -> Encontrado en PATH.");
            return ffmpegPath;
        } else {
            System.out.println("         -> No encontrado en PATH. Continuando...");
        }
        
        // Paso 3: ubicaciones comunes predefinidas
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
        
        System.out.println("[Paso 3/3] Buscando en ubicaciones comunes...");
        for (String path : commonPaths) {
            System.out.println("         -> Probando: " + path);
            File f = new File(path);
            if (f.exists() && f.canExecute()) {
                ffmpegPath = path;
                File probeFile = new File(f.getParent(), f.getName().replace("ffmpeg", "ffprobe"));
                if (probeFile.exists()) {
                    ffprobePath = probeFile.getPath();
                }
                System.out.println("         -> Encontrado en ubicación común.");
                return ffmpegPath;
            }
        }
        System.out.println("         -> No encontrado en ninguna ubicación común.");
        
        return null;
    }
    
    /**
     * Busca ffmpeg recursivamente en el sistema usando comandos.
     */
    public String searchInSystem() {
        System.out.println("[Búsqueda recursiva] Explorando el sistema de archivos completo en busca de " + FFMPEG_EXE + "...");
        System.out.println("                     (Este proceso puede tardar varios minutos)");
        
        try {
            List<String> command = new ArrayList<>();
            if (IS_WINDOWS) {
                command.add("where");
                command.add("/R");
                command.add("C:\\");
                command.add(FFMPEG_EXE);
                System.out.println("                     Ejecutando: where /R C:\\ " + FFMPEG_EXE);
            } else {
                command.add("find");
                command.add("/");
                command.add("-name");
                command.add(FFMPEG_EXE);
                command.add("-type");
                command.add("f");
                // Note: "2>/dev/null" must NOT be added here — ProcessBuilder does not use a shell
                // and would pass it as a literal argument to find. Stderr is discarded below.
                System.out.println("                     Ejecutando: find / -name " + FFMPEG_EXE + " -type f");
            }
            
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectError(ProcessBuilder.Redirect.DISCARD);
            Process process = pb.start();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("                     Analizando: " + line.trim());
                File f = new File(line.trim());
                if (f.exists() && f.canExecute()) {
                    ffmpegPath = f.getAbsolutePath();
                    File probe = new File(f.getParent(), FFPROBE_EXE);
                    if (probe.exists()) {
                        ffprobePath = probe.getAbsolutePath();
                    }
                    process.destroy();
                    System.out.println("                     -> Encontrado: " + ffmpegPath);
                    return ffmpegPath;
                }
            }
            
            process.waitFor();
        } catch (Exception e) {
            System.err.println("[Búsqueda recursiva] Error: " + e.getMessage());
        }
        
        System.out.println("[Búsqueda recursiva] " + FFMPEG_EXE + " no encontrado en el sistema.");
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
