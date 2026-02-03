package ui;

import model.*;
import service.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Ventana principal de la aplicación con Swing.
 */
public class MainFrame extends JFrame {
    private String ffmpegPath;
    private FFmpegService ffmpegService;
    private FFprobeService ffprobeService;
    private FileScanner fileScanner;
    
    // Componentes UI
    private DefaultListModel<VideoFile> fileListModel;
    private JList<VideoFile> fileList;
    private JTextArea logArea;
    private SettingsPanel settingsPanel;
    private JButton convertBtn;
    private JButton thumbBtn;
    private JButton analyzeBtn;
    private JButton compareBtn;
    private JProgressBar progressBar;
    
    public MainFrame(String ffmpegPath) {
        this.ffmpegPath = ffmpegPath;
        this.ffmpegService = new FFmpegService(ffmpegPath);
        this.ffprobeService = new FFprobeService(ffmpegPath.replace("ffmpeg", "ffprobe"));
        this.fileScanner = new FileScanner(ffprobeService);
        
        ffmpegService.setOutputListener(this::log);
        
        initUI();
    }
    
    private void initUI() {
        setTitle("FFmpeg Video Converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Layout principal
        setLayout(new BorderLayout(5, 5));
        
        // Panel izquierdo - Lista de archivos
        JPanel leftPanel = createFileListPanel();
        add(leftPanel, BorderLayout.WEST);
        
        // Panel central - Configuración
        settingsPanel = new SettingsPanel();
        JScrollPane settingsScroll = new JScrollPane(settingsPanel);
        settingsScroll.setBorder(BorderFactory.createTitledBorder("Configuración"));
        add(settingsScroll, BorderLayout.CENTER);
        
        // Panel inferior - Log y botones
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Menú
        setJMenuBar(createMenuBar());
    }
    
    private JPanel createFileListPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Archivos"));
        panel.setPreferredSize(new Dimension(300, 0));
        
        fileListModel = new DefaultListModel<>();
        fileList = new JList<>(fileListModel);
        fileList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        // Drag & Drop
        new DropTarget(fileList, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    @SuppressWarnings("unchecked")
                    List<File> files = (List<File>) dtde.getTransferable()
                            .getTransferData(DataFlavor.javaFileListFlavor);
                    addFiles(files);
                } catch (Exception e) {
                    log("Error al soltar archivos: " + e.getMessage());
                }
            }
        });
        
        JScrollPane scroll = new JScrollPane(fileList);
        panel.add(scroll, BorderLayout.CENTER);
        
        // Botones
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton addBtn = new JButton("Añadir");
        addBtn.addActionListener(e -> browseFiles());
        
        JButton addFolderBtn = new JButton("Carpeta");
        addFolderBtn.addActionListener(e -> browseFolder());
        
        JButton removeBtn = new JButton("Quitar");
        removeBtn.addActionListener(e -> removeSelected());
        
        JButton clearBtn = new JButton("Limpiar");
        clearBtn.addActionListener(e -> fileListModel.clear());
        
        btnPanel.add(addBtn);
        btnPanel.add(addFolderBtn);
        btnPanel.add(removeBtn);
        btnPanel.add(clearBtn);
        
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        // Info al seleccionar
        fileList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                VideoFile selected = fileList.getSelectedValue();
                if (selected != null) {
                    showFileInfo(selected);
                }
            }
        });
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        
        // Log
        logArea = new JTextArea(8, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Log"));
        panel.add(logScroll, BorderLayout.CENTER);
        
        // Barra de progreso y botones
        JPanel actionPanel = new JPanel(new BorderLayout(5, 5));
        
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        actionPanel.add(progressBar, BorderLayout.NORTH);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        convertBtn = new JButton("Convertir");
        convertBtn.addActionListener(e -> convert());
        
        thumbBtn = new JButton("Crear Thumbnail");
        thumbBtn.addActionListener(e -> createThumbnail());
        
        analyzeBtn = new JButton("Analizar");
        analyzeBtn.addActionListener(e -> analyzeSelected());
        
        compareBtn = new JButton("Comparar Calidad");
        compareBtn.addActionListener(e -> compareQuality());
        
        JButton cancelBtn = new JButton("Cancelar");
        cancelBtn.addActionListener(e -> ffmpegService.cancel());
        
        btnPanel.add(convertBtn);
        btnPanel.add(thumbBtn);
        btnPanel.add(analyzeBtn);
        btnPanel.add(compareBtn);
        btnPanel.add(cancelBtn);
        
        actionPanel.add(btnPanel, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Archivo
        JMenu fileMenu = new JMenu("Archivo");
        
        JMenuItem addItem = new JMenuItem("Añadir archivos...");
        addItem.addActionListener(e -> browseFiles());
        
        JMenuItem addFolderItem = new JMenuItem("Añadir carpeta...");
        addFolderItem.addActionListener(e -> browseFolder());
        
        JMenuItem exitItem = new JMenuItem("Salir");
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(addItem);
        fileMenu.add(addFolderItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Herramientas
        JMenu toolsMenu = new JMenu("Herramientas");
        
        JMenuItem filterItem = new JMenuItem("Filtrar por bitrate...");
        filterItem.addActionListener(e -> showFilterDialog());
        
        JMenuItem searchItem = new JMenuItem("Buscar con regex...");
        searchItem.addActionListener(e -> showSearchDialog());
        
        toolsMenu.add(filterItem);
        toolsMenu.add(searchItem);
        
        // Ayuda
        JMenu helpMenu = new JMenu("Ayuda");
        
        JMenuItem aboutItem = new JMenuItem("Acerca de");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "FFmpeg Video Converter\n\nUsando: " + ffmpegPath,
                "Acerca de", JOptionPane.INFORMATION_MESSAGE));
        
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    private void browseFiles() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                String name = f.getName().toLowerCase();
                return name.endsWith(".mp4") || name.endsWith(".mkv") || 
                       name.endsWith(".avi") || name.endsWith(".mov") ||
                       name.endsWith(".wmv") || name.endsWith(".flv") ||
                       name.endsWith(".webm");
            }
            @Override
            public String getDescription() { return "Archivos de vídeo"; }
        });
        
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            addFiles(Arrays.asList(chooser.getSelectedFiles()));
        }
    }
    
    private void browseFolder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String regex = JOptionPane.showInputDialog(this, 
                    "Expresión regular (vacío = todos):", "");
            int result = JOptionPane.showConfirmDialog(this, 
                    "¿Buscar recursivamente?", "Recursivo", JOptionPane.YES_NO_OPTION);
            boolean recursive = result == JOptionPane.YES_OPTION;
            
            new Thread(() -> {
                try {
                    List<File> files = fileScanner.scanDirectory(
                            chooser.getSelectedFile().getAbsolutePath(),
                            regex != null && !regex.isEmpty() ? regex : null,
                            recursive);
                    SwingUtilities.invokeLater(() -> addFiles(files));
                } catch (IOException e) {
                    log("Error escaneando carpeta: " + e.getMessage());
                }
            }).start();
        }
    }
    
    private void addFiles(List<File> files) {
        new Thread(() -> {
            for (File file : files) {
                try {
                    if (file.isDirectory()) {
                        List<File> subFiles = fileScanner.scanDirectory(
                                file.getAbsolutePath(), null, true);
                        for (File subFile : subFiles) {
                            VideoFile video = ffprobeService.analyze(subFile);
                            SwingUtilities.invokeLater(() -> fileListModel.addElement(video));
                        }
                    } else {
                        VideoFile video = ffprobeService.analyze(file);
                        SwingUtilities.invokeLater(() -> fileListModel.addElement(video));
                    }
                } catch (IOException e) {
                    log("Error analizando " + file.getName() + ": " + e.getMessage());
                }
            }
            log("Añadidos " + files.size() + " elementos");
        }).start();
    }
    
    private void removeSelected() {
        int[] indices = fileList.getSelectedIndices();
        for (int i = indices.length - 1; i >= 0; i--) {
            fileListModel.remove(indices[i]);
        }
    }
    
    private void showFileInfo(VideoFile video) {
        StringBuilder sb = new StringBuilder();
        sb.append("Archivo: ").append(video.getName()).append("\n");
        sb.append("Resolución: ").append(video.getResolution()).append("\n");
        sb.append("Duración: ").append(video.getDurationFormatted()).append("\n");
        sb.append("Tamaño: ").append(video.getFileSizeFormatted()).append("\n");
        sb.append("Codec vídeo: ").append(video.getVideoCodec()).append("\n");
        sb.append("Codec audio: ").append(video.getAudioCodec()).append("\n");
        log(sb.toString());
    }
    
    private void convert() {
        if (fileListModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay archivos para convertir");
            return;
        }
        
        String outputDir = null;
        int result = JOptionPane.showConfirmDialog(this, 
                "¿Seleccionar carpeta de salida?", "Salida", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                outputDir = chooser.getSelectedFile().getAbsolutePath();
            }
        }
        
        final String finalOutputDir = outputDir;
        VideoSettings settings = settingsPanel.getSettings();
        
        setButtonsEnabled(false);
        progressBar.setMaximum(fileListModel.size());
        progressBar.setValue(0);
        
        new Thread(() -> {
            int success = 0;
            for (int i = 0; i < fileListModel.size(); i++) {
                VideoFile video = fileListModel.get(i);
                final int idx = i;
                SwingUtilities.invokeLater(() -> {
                    progressBar.setValue(idx);
                    progressBar.setString(video.getName());
                });
                
                try {
                    log("Convirtiendo: " + video.getName());
                    File output = ffmpegService.convert(video, settings, finalOutputDir);
                    log("Completado: " + output.getName());
                    success++;
                } catch (IOException e) {
                    log("Error: " + e.getMessage());
                }
            }
            
            final int total = success;
            SwingUtilities.invokeLater(() -> {
                progressBar.setValue(progressBar.getMaximum());
                progressBar.setString("Completado: " + total + "/" + fileListModel.size());
                setButtonsEnabled(true);
                JOptionPane.showMessageDialog(this, 
                        "Conversión completada: " + total + "/" + fileListModel.size());
            });
        }).start();
    }
    
    private void createThumbnail() {
        VideoFile selected = fileList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un archivo");
            return;
        }
        
        String time = JOptionPane.showInputDialog(this, 
                "Tiempo (HH:MM:SS o segundos):", "00:00:01");
        if (time == null) return;
        
        VideoSettings settings = settingsPanel.getSettings();
        settings.setThumbnailTime(time);
        
        new Thread(() -> {
            try {
                log("Creando thumbnail de: " + selected.getName());
                File thumb = ffmpegService.createThumbnail(selected, settings, null);
                log("Thumbnail creado: " + thumb.getAbsolutePath());
                SwingUtilities.invokeLater(() -> 
                    JOptionPane.showMessageDialog(this, "Thumbnail creado:\n" + thumb.getAbsolutePath()));
            } catch (IOException e) {
                log("Error: " + e.getMessage());
            }
        }).start();
    }
    
    private void analyzeSelected() {
        VideoFile selected = fileList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un archivo");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== Análisis de ").append(selected.getName()).append(" ===\n");
        sb.append("Ruta: ").append(selected.getPath()).append("\n");
        sb.append("Tamaño: ").append(selected.getFileSizeFormatted()).append("\n");
        sb.append("Duración: ").append(selected.getDurationFormatted()).append("\n");
        sb.append("Resolución: ").append(selected.getResolution()).append("\n");
        sb.append("Codec vídeo: ").append(selected.getVideoCodec()).append("\n");
        sb.append("Codec audio: ").append(selected.getAudioCodec()).append("\n");
        sb.append("Sample rate: ").append(selected.getAudioSampleRate()).append(" Hz\n");
        sb.append(String.format("Bitrate: %.2f Mbps%n", selected.calculateBitsPerSecond() / 1000000));
        
        log(sb.toString());
        JOptionPane.showMessageDialog(this, sb.toString(), "Análisis", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void compareQuality() {
        if (fileListModel.size() < 2) {
            JOptionPane.showMessageDialog(this, 
                    "Necesitas al menos 2 archivos (original y convertido)");
            return;
        }
        
        // Seleccionar original
        VideoFile original = (VideoFile) JOptionPane.showInputDialog(this,
                "Selecciona el archivo ORIGINAL:",
                "Comparar Calidad",
                JOptionPane.QUESTION_MESSAGE,
                null,
                Collections.list(fileListModel.elements()).toArray(),
                fileListModel.get(0));
        
        if (original == null) return;
        
        // Seleccionar convertido
        VideoFile converted = (VideoFile) JOptionPane.showInputDialog(this,
                "Selecciona el archivo CONVERTIDO:",
                "Comparar Calidad",
                JOptionPane.QUESTION_MESSAGE,
                null,
                Collections.list(fileListModel.elements()).toArray(),
                fileListModel.get(1));
        
        if (converted == null) return;
        
        setButtonsEnabled(false);
        new Thread(() -> {
            try {
                log("Comparando calidad (esto puede tardar)...");
                QualityMetrics metrics = ffmpegService.compareQuality(
                        original.getFile(), converted.getFile());
                
                SwingUtilities.invokeLater(() -> {
                    log(metrics.toString());
                    JOptionPane.showMessageDialog(this, metrics.toString(), 
                            "Métricas de Calidad", JOptionPane.INFORMATION_MESSAGE);
                    setButtonsEnabled(true);
                });
            } catch (IOException e) {
                log("Error: " + e.getMessage());
                SwingUtilities.invokeLater(() -> setButtonsEnabled(true));
            }
        }).start();
    }
    
    private void showFilterDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        JTextField minDurField = new JTextField("0");
        JTextField maxBpsField = new JTextField("5000000");
        JCheckBox convertCheck = new JCheckBox("Convertir archivos filtrados");
        
        panel.add(new JLabel("Duración mínima (seg):"));
        panel.add(minDurField);
        panel.add(new JLabel("Max bits/segundo:"));
        panel.add(maxBpsField);
        panel.add(convertCheck);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
                "Filtrar por Bitrate", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            double minDur = Double.parseDouble(minDurField.getText());
            double maxBps = Double.parseDouble(maxBpsField.getText());
            
            new Thread(() -> {
                try {
                    List<File> files = new ArrayList<>();
                    for (int i = 0; i < fileListModel.size(); i++) {
                        files.add(fileListModel.get(i).getFile());
                    }
                    
                    List<VideoFile> filtered = fileScanner.filterByBitrate(files, minDur, maxBps);
                    
                    SwingUtilities.invokeLater(() -> {
                        fileListModel.clear();
                        for (VideoFile v : filtered) {
                            fileListModel.addElement(v);
                        }
                        log("Filtrados: " + filtered.size() + " archivos exceden el umbral");
                    });
                    
                    if (convertCheck.isSelected() && !filtered.isEmpty()) {
                        SwingUtilities.invokeLater(this::convert);
                    }
                } catch (IOException e) {
                    log("Error: " + e.getMessage());
                }
            }).start();
        }
    }
    
    private void showSearchDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        JTextField pathField = new JTextField();
        JTextField regexField = new JTextField(".*\\.mp4");
        JCheckBox recursiveCheck = new JCheckBox("Recursivo", true);
        
        panel.add(new JLabel("Ruta base:"));
        panel.add(pathField);
        panel.add(new JLabel("Expresión regular:"));
        panel.add(regexField);
        panel.add(recursiveCheck);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
                "Buscar con Regex", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            new Thread(() -> {
                try {
                    List<File> files = fileScanner.scanDirectory(
                            pathField.getText(),
                            regexField.getText(),
                            recursiveCheck.isSelected());
                    
                    SwingUtilities.invokeLater(() -> addFiles(files));
                } catch (IOException e) {
                    log("Error: " + e.getMessage());
                }
            }).start();
        }
    }
    
    private void setButtonsEnabled(boolean enabled) {
        convertBtn.setEnabled(enabled);
        thumbBtn.setEnabled(enabled);
        analyzeBtn.setEnabled(enabled);
        compareBtn.setEnabled(enabled);
    }
    
    private void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
}
