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
    private java.util.function.Consumer<String> statusListener = null;
    
    private static final String DOWNLOAD_URL = "https://ffmpeg.org/download.html";
    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("win");
    private static final String FFMPEG_EXE = IS_WINDOWS ? "ffmpeg.exe" : "ffmpeg";
    private static final String FFPROBE_EXE = IS_WINDOWS ? "ffprobe.exe" : "ffprobe";
    
    public void setCustomPath(String path) {
        this.customPath = path;
    }
    
    public void setStatusListener(java.util.function.Consumer<String> listener) {
        this.statusListener = listener;
    }
    
    public static String getFFmpegExeName() {
        return FFMPEG_EXE;
    }
    
    public static String getDownloadUrl() {
        return DOWNLOAD_URL;
    }
    
    private void status(String message) {
        System.out.println(message);
        if (statusListener != null) {
            statusListener.accept(message);
        }
    }
    
    /**
     * Intenta localizar ffmpeg.
     * @return Ruta a ffmpeg o null si no se encuentra
     */
    public String locate() {
        // Paso 1: ruta personalizada
        if (customPath == null) {
            status("[Paso 1/3] Ruta personalizada no proporcionada.");
        } else {
            status("[Paso 1/3] Verificando ruta personalizada: " + customPath);
            File custom = new File(customPath);
            if (custom.exists() && custom.canExecute() && isWorkingExecutable(customPath)) {
                ffmpegPath = customPath;
                File probe = new File(custom.getParent(), FFPROBE_EXE);
                if (probe.exists()) {
                    ffprobePath = probe.getAbsolutePath();
                }
                status("         -> Encontrado en ruta personalizada.");
                return ffmpegPath;
            } else {
                status("         -> No encontrado o no ejecutable. Continuando...");
            }
        }
        
        // Paso 2: verificar si está en PATH
        status("[Paso 2/3] Buscando " + FFMPEG_EXE + " en el PATH del sistema...");
        if (isInPath("ffmpeg") && isWorkingExecutable("ffmpeg")) {
            ffmpegPath = "ffmpeg";
            ffprobePath = "ffprobe";
            status("         -> Encontrado en PATH.");
            return ffmpegPath;
        } else {
            status("         -> No encontrado en PATH. Continuando...");
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
        
        status("[Paso 3/3] Buscando en ubicaciones comunes...");
        for (String path : commonPaths) {
            status("         -> Probando: " + path);
            File f = new File(path);
            if (f.exists() && f.canExecute() && isWorkingExecutable(path)) {
                ffmpegPath = path;
                File probeFile = new File(f.getParent(), f.getName().replace("ffmpeg", "ffprobe"));
                if (probeFile.exists()) {
                    ffprobePath = probeFile.getPath();
                }
                status("         -> Encontrado en ubicación común.");
                return ffmpegPath;
            }
        }
        status("         -> No encontrado en ninguna ubicación común.");
        
        return null;
    }
    
    /**
     * Busca ffmpeg recursivamente en el sistema usando comandos.
     */
    public String searchInSystem() {
        status("[Búsqueda recursiva] Explorando el sistema de archivos completo en busca de " + FFMPEG_EXE + "...");
        status("                     (Este proceso puede tardar varios minutos)");
        
        try {
            List<String> command = new ArrayList<>();
            if (IS_WINDOWS) {
                command.add("where");
                command.add("/R");
                command.add("C:\\");
                command.add(FFMPEG_EXE);
                status("                     Ejecutando: where /R C:\\ " + FFMPEG_EXE);
            } else {
                command.add("find");
                command.add("/");
                command.add("-name");
                command.add(FFMPEG_EXE);
                command.add("-type");
                command.add("f");
                // Note: "2>/dev/null" must NOT be added here — ProcessBuilder does not use a shell
                // and would pass it as a literal argument to find. Stderr is discarded below.
                status("                     Ejecutando: find / -name " + FFMPEG_EXE + " -type f");
            }
            
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectError(ProcessBuilder.Redirect.DISCARD);
            Process process = pb.start();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                status("                     Analizando: " + line.trim());
                File f = new File(line.trim());
                if (f.exists() && f.canExecute() && isWorkingExecutable(f.getAbsolutePath())) {
                    ffmpegPath = f.getAbsolutePath();
                    File probe = new File(f.getParent(), FFPROBE_EXE);
                    if (probe.exists()) {
                        ffprobePath = probe.getAbsolutePath();
                    }
                    process.destroy();
                    status("                     -> Encontrado: " + ffmpegPath);
                    return ffmpegPath;
                }
            }
            
            process.waitFor();
        } catch (Exception e) {
            System.err.println("[Búsqueda recursiva] Error: " + e.getMessage());
        }
        
        status("[Búsqueda recursiva] " + FFMPEG_EXE + " no encontrado en el sistema.");
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
    
    /**
     * Verifica que el ejecutable funciona correctamente ejecutando 'ffmpeg -version'.
     * Descarta binarios rotos o con dependencias faltantes.
     */
    private boolean isWorkingExecutable(String path) {
        try {
            ProcessBuilder pb = new ProcessBuilder(path, "-version");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            // Consumir la salida para evitar bloqueo del buffer
            new BufferedReader(new InputStreamReader(process.getInputStream()))
                .lines().forEach(l -> {});
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getFFmpegPath() { return ffmpegPath; }
    public String getFFprobePath() { return ffprobePath; }
}
