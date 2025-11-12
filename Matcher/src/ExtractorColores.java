import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractorColores {
    public static void main(String[] args) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSS", "css");
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
    
    private static List<String> extraerColores(String contenido) {
        List<String> colores = new ArrayList<>();
        
        String hexPattern = "#([A-Fa-f0-9]{3,4}|[A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})";
        String rgbaPattern = "rgba?\\(\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*(?:,\\s*[0-9]*\\.?[0-9]+\\s*)?\\)";
        String patternString = hexPattern + "|" + rgbaPattern;
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(contenido);
        
        while (matcher.find()) {
            colores.add(matcher.group());
        }
        
        return colores;
    }
    
    private static void mostrarVentanaColores(List<String> colores, String nombreArchivo) {
        JFrame frame = new JFrame("Colores encontrados - " + nombreArchivo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 250); // Aumenté la altura para el texto
        frame.setLocationRelativeTo(null);
        
        // Panel principal con scroll horizontal
        JPanel panelColores = new JPanel();
        panelColores.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelColores.setBackground(Color.WHITE);
        
        for (String colorStr : colores) {
            Color color = parseColor(colorStr);
            
            // Crear un panel para cada color (cuadrado + texto)
            JPanel colorContainer = new JPanel();
            colorContainer.setLayout(new BorderLayout());
            colorContainer.setBackground(Color.WHITE);
            
            // Panel para el cuadrado de color
            JPanel colorPanel = new JPanel();
            colorPanel.setPreferredSize(new Dimension(80, 80));
            colorPanel.setBackground(color);
            colorPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            colorPanel.setToolTipText(colorStr);
            
            // Etiqueta con el código del color
            JLabel colorLabel = new JLabel(colorStr, SwingConstants.CENTER);
            colorLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            colorLabel.setPreferredSize(new Dimension(80, 20));
            
            // Añadir al contenedor
            colorContainer.add(colorPanel, BorderLayout.CENTER);
            colorContainer.add(colorLabel, BorderLayout.SOUTH);
            
            panelColores.add(colorContainer);
        }
        
        JScrollPane scrollPane = new JScrollPane(panelColores);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Colores encontrados: " + colores.size());
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        mainPanel.add(titulo, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    private static Color parseColor(String colorStr) {
        try {
            if (colorStr.startsWith("#")) {
                return Color.decode(colorStr);
            } else if (colorStr.startsWith("rgb")) {
                // Extracción de rgb/rgba
                String numbers = colorStr.replaceAll("[rgba()\\s]", "");
                String[] parts = numbers.split(",");
                
                int r = Integer.parseInt(parts[0].trim());
                int g = Integer.parseInt(parts[1].trim());
                int b = Integer.parseInt(parts[2].trim());
                
                return new Color(r, g, b);
            }
        } catch (Exception e) {
            System.out.println("Error parsing color: " + colorStr);
        }
        
        return Color.GRAY; // por defecto
    }
}