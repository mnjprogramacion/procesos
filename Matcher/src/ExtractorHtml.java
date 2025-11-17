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
                List<ElementoHtml> elementos = extraerElementos(content);
                mostrarVentanaElementos(elementos, chooser.getSelectedFile().getName());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al leer el archivo: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
    
    public static class ElementoHtml {
        public String clase;
        public String texto;
        public String numero;
        public ElementoHtml(String clase, String texto, String numero) {
            this.clase = clase;
            this.texto = texto;
            this.numero = numero;
        }
    }
    
    // Extrae los elementos del contenedor y su información
    public static List<ElementoHtml> extraerElementos(String contenido) {
        java.util.ArrayList<ElementoHtml> elementos = new java.util.ArrayList<>();
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("<div class=\\\"elemento ([^\\\"]+)\\\">([^<]+)</div>");
        java.util.regex.Matcher matcher = pattern.matcher(contenido);
        while (matcher.find()) {
            String clase = matcher.group(1);
            String texto = matcher.group(2).trim();
            java.util.regex.Matcher numMatcher = java.util.regex.Pattern.compile("(\\d+)").matcher(texto);
            String numero = numMatcher.find() ? numMatcher.group(1) : "";
            elementos.add(new ElementoHtml(clase, texto, numero));
        }
        return elementos;
    }
    
    // Muestra la ventana final
    public static void mostrarVentanaElementos(List<ElementoHtml> elementos, String nombreArchivo) {
        StringBuilder sb = new StringBuilder();
        for (ElementoHtml el : elementos) {
            sb.append("(" + el.clase + ") --> {" + el.texto + "} - [" + el.numero + "]\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Lista de elementos", JOptionPane.INFORMATION_MESSAGE);
    }
}