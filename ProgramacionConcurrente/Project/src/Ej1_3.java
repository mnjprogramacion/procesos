import java.io.IOException;

public class Ej1_3 {
    public static void main(String[] args) {
        try {
            String texto = "Hola mundo";
            String nombreArchivo = "Hola.txt";
            ProcessBuilder pb = new ProcessBuilder(
                "cmd.exe",
                "/c",
                "echo " + texto + " > " + nombreArchivo
            );
            pb.start();
            System.out.println("Se usó la consola de Windows.");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}