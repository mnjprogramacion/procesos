import java.awt.GraphicsEnvironment;
import javax.swing.JOptionPane;

/**
 *
 * @author Matt
 */
public class Test1 {

    public static void main(String[] args) {
        if (!GraphicsEnvironment.isHeadless()) {
            JOptionPane.showConfirmDialog(null, "Vamos a mostrar argumentos");
        }
        System.out.println("INICIO PROGRAMA");
        for (int i = 0; i < args.length; i++)
        {
            System.out.println(i+". "+args[i]);
        }
        System.out.println("FIN PROGRAMA");

    }
}