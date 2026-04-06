package ui;

import service.FFmpegLocator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Diálogo para buscar FFmpeg cuando no se encuentra automáticamente.
 * Ofrece 3 opciones: seleccionar manualmente, buscar automáticamente, o descargar.
 */
public class FFmpegSearchDialog extends JDialog {
    private FFmpegLocator locator;
    private JTextArea logArea;
    private JButton manualBtn;
    private JButton autoBtn;
    private JButton downloadBtn;
    private String resultPath = null;
    private String resultProbePath = null;
    private volatile Thread searchThread = null;
    
    public FFmpegSearchDialog(Frame owner) {
        super(owner, "FFmpeg no encontrado", true);
        this.locator = new FFmpegLocator();
        initUI();
    }
    
    private void initUI() {
        setSize(550, 420);
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Al cerrar la ventana, interrumpir búsqueda si estuviera en marcha
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (searchThread != null) {
                    searchThread.interrupt();
                }
            }
        });
        
        // Panel superior - mensaje informativo
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        
        JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.warningIcon"));
        headerPanel.add(iconLabel, BorderLayout.WEST);
        
        JLabel messageLabel = new JLabel(
            "<html><b>No se ha encontrado FFmpeg en el sistema.</b><br><br>"
            + "FFmpeg es necesario para que la aplicación funcione.<br>"
            + "Selecciona una opción para continuar:</html>"
        );
        messageLabel.setFont(messageLabel.getFont().deriveFont(13f));
        headerPanel.add(messageLabel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Panel central - log de búsqueda
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Estado de la búsqueda"));
        logScroll.setPreferredSize(new Dimension(500, 180));
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        centerPanel.add(logScroll, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        
        // Panel inferior - 3 botones
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        
        manualBtn = new JButton("Selección manual");
        manualBtn.setToolTipText("Buscar el archivo FFmpeg manualmente");
        manualBtn.addActionListener(e -> selectManually());
        
        autoBtn = new JButton("Búsqueda automática");
        autoBtn.setToolTipText("Buscar el archivo FFmpeg en todo el sistema");
        autoBtn.addActionListener(e -> searchAutomatically());
        
        downloadBtn = new JButton("No tengo FFmpeg");
        downloadBtn.setToolTipText("Abrir la página de descarga de FFmpeg");
        downloadBtn.addActionListener(e -> openDownloadPage());
        
        buttonPanel.add(manualBtn);
        buttonPanel.add(autoBtn);
        buttonPanel.add(downloadBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void selectManually() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar " + FFmpegLocator.getFFmpegExeName());
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selected = chooser.getSelectedFile();
            if (selected.exists() && selected.canExecute()) {
                locator.setCustomPath(selected.getAbsolutePath());
                String result = locator.locate();
                if (result != null) {
                    resultPath = result;
                    resultProbePath = locator.getFFprobePath();
                    log("FFmpeg seleccionado: " + resultPath);
                    dispose();
                } else {
                    log("El archivo seleccionado no es un ejecutable de FFmpeg válido.");
                }
            } else {
                log("El archivo no existe o no es ejecutable: " + selected.getAbsolutePath());
            }
        }
    }
    
    private void searchAutomatically() {
        setButtonsEnabled(false);
        logArea.setText("");
        log("Iniciando búsqueda automática...\n");
        
        locator.setStatusListener(message -> 
            SwingUtilities.invokeLater(() -> {
                logArea.append(message + "\n");
                logArea.setCaretPosition(logArea.getDocument().getLength());
            })
        );
        
        searchThread = new Thread(() -> {
            // Primero intenta locate() (PATH + ubicaciones comunes)
            String result = locator.locate();
            
            // Si no lo encuentra, búsqueda recursiva
            if (result == null) {
                result = locator.searchInSystem();
            }
            
            final String finalResult = result;
            SwingUtilities.invokeLater(() -> {
                if (finalResult != null) {
                    resultPath = finalResult;
                    resultProbePath = locator.getFFprobePath();
                    log("\nFFmpeg encontrado: " + resultPath);
                    JOptionPane.showMessageDialog(this,
                        "FFmpeg encontrado:\n" + resultPath,
                        "Encontrado", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    log("\nNo se encontró FFmpeg en el sistema.");
                    setButtonsEnabled(true);
                }
            });
        });
        searchThread.setDaemon(true);
        searchThread.start();
    }
    
    private void openDownloadPage() {
        locator.openDownloadPage();
        log("Abriendo página de descarga: " + FFmpegLocator.getDownloadUrl());
        log("Descarga FFmpeg, instálalo, y vuelve a intentar con las otras opciones.");
    }
    
    private void setButtonsEnabled(boolean enabled) {
        manualBtn.setEnabled(enabled);
        autoBtn.setEnabled(enabled);
        downloadBtn.setEnabled(enabled);
    }
    
    private void log(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    
    /** @return ruta a ffmpeg, o null si el usuario cerró sin encontrarlo */
    public String getFFmpegPath() { return resultPath; }
    
    /** @return ruta a ffprobe */
    public String getFFprobePath() { return resultProbePath; }
}
