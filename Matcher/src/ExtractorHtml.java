import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ExtractorHtml {
public static void main(String[] args) {
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("HTML", "html");
    chooser.setFileFilter(filter);
    int returnVal = chooser.showOpenDialog(null);

    if(returnVal == JFileChooser.APPROVE_OPTION) {
        String filePath = chooser.getSelectedFile().getAbsolutePath();
        
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            List<String> colores = extraerColores(content);
            
            mostrarVentanaColores(colores, chooser.getSelectedFile().getName());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al leer el archivo: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
}
