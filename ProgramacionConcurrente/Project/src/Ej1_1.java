import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ej1_1 {
    public static void main(String[] args) {
        try {
            String[] command = {"Notepad.exe", "notas.txt"};
            Process process = Runtime.getRuntime().exec(command);
            int codigoRetorno = process.waitFor();
            System.out.println("Notepad cerrado con código " + codigoRetorno);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Ej1_1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}