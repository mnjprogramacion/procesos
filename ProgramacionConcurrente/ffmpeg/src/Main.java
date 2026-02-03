import java.awt.GraphicsEnvironment;
import console.ConsoleInterface;
import ui.MainFrame;
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
        
        // Localizar FFmpeg
        FFmpegLocator locator = new FFmpegLocator();
        if (ffmpegPath != null) {
            locator.setCustomPath(ffmpegPath);
        }
        
        String ffmpeg = locator.locate();
        if (ffmpeg == null) {
            System.out.println("Buscando FFmpeg en el sistema...");
            ffmpeg = locator.searchInSystem();
            
            if (ffmpeg == null) {
                System.out.println("FFmpeg no encontrado en el sistema.");
                System.out.println("Descarga FFmpeg desde: https://ffmpeg.org/download.html");
                if (!forceConsole && !GraphicsEnvironment.isHeadless()) {
                    locator.openDownloadPage();
                }
                System.exit(1);
            }
        }
        
        System.out.println("FFmpeg encontrado: " + ffmpeg);
        
        // Decidir modo de ejecución
        boolean useConsole = forceConsole || GraphicsEnvironment.isHeadless();
        
        final String ffmpegFinal = ffmpeg;
        
        if (useConsole) {
            ConsoleInterface console = new ConsoleInterface(ffmpegFinal);
            console.run();
        } else {
            javax.swing.SwingUtilities.invokeLater(() -> {
                MainFrame frame = new MainFrame(ffmpegFinal);
                frame.setVisible(true);
            });
        }
    }
}
