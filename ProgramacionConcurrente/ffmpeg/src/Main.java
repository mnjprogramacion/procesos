import java.awt.GraphicsEnvironment;
import console.ConsoleInterface;
import ui.MainFrame;
import ui.FFmpegSearchDialog;
import service.FFmpegLocator;

/**
 * Punto de entrada principal de la aplicación FFmpeg Video Converter.
 * Detecta automáticamente si debe ejecutarse en modo GUI o consola.
 */
public class Main {
    
    public static void main(String[] args) {
        boolean forceConsole = false;
        String ffmpegPath = null;
        
        // Parsear argumentos
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-x")) {
                forceConsole = true;
            } else if (args[i].equals("-ffmpeg") && i + 1 < args.length) {
                ffmpegPath = args[++i];
            }
        }
        
        // Decidir modo de ejecución
        boolean useConsole = forceConsole || GraphicsEnvironment.isHeadless();
        
        // Localizar FFmpeg
        FFmpegLocator locator = new FFmpegLocator();
        if (ffmpegPath != null) {
            locator.setCustomPath(ffmpegPath);
        }
        
        String ffmpeg = locator.locate();
        
        if (useConsole) {
            // Modo consola: búsqueda completa antes de arrancar
            if (ffmpeg == null) {
                System.out.println("Buscando FFmpeg en el sistema...");
                ffmpeg = locator.searchInSystem();
                
                if (ffmpeg == null) {
                    System.out.println("FFmpeg no encontrado en el sistema.");
                    System.out.println("Descarga FFmpeg desde: https://ffmpeg.org/download.html");
                    System.exit(1);
                }
            }
            
            System.out.println("FFmpeg encontrado: " + ffmpeg);
            String ffprobe = locator.getFFprobePath();
            ConsoleInterface console = new ConsoleInterface(ffmpeg, ffprobe);
            console.run();
        } else {
            // Modo GUI: abrir ventana primero, luego buscar ffmpeg si hace falta
            final String ffmpegFound = ffmpeg;
            final String ffprobeFound = (ffmpeg != null) ? locator.getFFprobePath() : null;
            
            javax.swing.SwingUtilities.invokeLater(() -> {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
                
                if (ffmpegFound != null) {
                    // FFmpeg ya encontrado, inicializar directamente
                    System.out.println("FFmpeg encontrado: " + ffmpegFound);
                    frame.initServices(ffmpegFound, ffprobeFound);
                } else {
                    // Mostrar diálogo de búsqueda
                    FFmpegSearchDialog dialog = new FFmpegSearchDialog(frame);
                    dialog.setVisible(true);
                    
                    if (dialog.getFFmpegPath() != null) {
                        frame.initServices(dialog.getFFmpegPath(), dialog.getFFprobePath());
                    } else {
                        System.exit(0);
                    }
                }
            });
        }
    }
}
