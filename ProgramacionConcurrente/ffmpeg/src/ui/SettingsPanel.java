package ui;

import model.VideoSettings;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Panel de configuración para las opciones de conversión.
 */
public class SettingsPanel extends JPanel {
    // Vídeo
    private JTextField widthField;
    private JTextField heightField;
    private JComboBox<String> videoCodecCombo;
    private JTextField videoBitrateField;
    private JSlider crfSlider;
    private JLabel crfLabel;
    private JComboBox<String> presetCombo;
    
    // Audio
    private JComboBox<String> audioCodecCombo;
    private JTextField audioBitrateField;
    private JComboBox<String> sampleRateCombo;
    
    // Salida
    private JComboBox<String> formatCombo;
    
    // Thumbnail
    private JTextField thumbTimeField;
    private JTextField thumbWidthField;
    private JTextField thumbHeightField;
    private JComboBox<String> thumbFormatCombo;
    private JSpinner thumbQualitySpinner;
    
    // Compatibilidad formato-codecs
    private static final String[] ALL_VIDEO_CODECS = {
        "(auto)", "libx264", "libx265", "libvpx-vp9", "mpeg4", "copy"
    };
    private static final String[] ALL_AUDIO_CODECS = {
        "(auto)", "aac", "libmp3lame", "libvorbis", "libopus", "flac", "copy"
    };
    
    private static final Map<String, String[]> FORMAT_VIDEO_CODECS = new HashMap<>();
    private static final Map<String, String[]> FORMAT_AUDIO_CODECS = new HashMap<>();
    
    static {
        FORMAT_VIDEO_CODECS.put("mp4",  new String[]{"(auto)", "libx264", "libx265", "mpeg4", "copy"});
        FORMAT_VIDEO_CODECS.put("mkv",  ALL_VIDEO_CODECS);
        FORMAT_VIDEO_CODECS.put("avi",  new String[]{"(auto)", "libx264", "mpeg4", "copy"});
        FORMAT_VIDEO_CODECS.put("webm", new String[]{"(auto)", "libvpx-vp9", "copy"});
        FORMAT_VIDEO_CODECS.put("mov",  new String[]{"(auto)", "libx264", "libx265", "mpeg4", "copy"});
        FORMAT_VIDEO_CODECS.put("flv",  new String[]{"(auto)", "libx264", "copy"});
        
        FORMAT_AUDIO_CODECS.put("mp4",  new String[]{"(auto)", "aac", "libmp3lame", "libopus", "copy"});
        FORMAT_AUDIO_CODECS.put("mkv",  ALL_AUDIO_CODECS);
        FORMAT_AUDIO_CODECS.put("avi",  new String[]{"(auto)", "aac", "libmp3lame", "copy"});
        FORMAT_AUDIO_CODECS.put("webm", new String[]{"(auto)", "libvorbis", "libopus", "copy"});
        FORMAT_AUDIO_CODECS.put("mov",  new String[]{"(auto)", "aac", "libmp3lame", "libopus", "flac", "copy"});
        FORMAT_AUDIO_CODECS.put("flv",  new String[]{"(auto)", "aac", "libmp3lame", "copy"});
    }
    
    public SettingsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(createVideoPanel());
        add(Box.createVerticalStrut(10));
        add(createAudioPanel());
        add(Box.createVerticalStrut(10));
        add(createOutputPanel());
        add(Box.createVerticalStrut(10));
        add(createThumbnailPanel());
        add(Box.createVerticalGlue());
        
        // Filtrar codecs según formato seleccionado
        formatCombo.addActionListener(e -> updateCodecsForFormat());
    }
    
    private void updateCodecsForFormat() {
        String format = (String) formatCombo.getSelectedItem();
        
        String[] videoCodecs = FORMAT_VIDEO_CODECS.getOrDefault(format, ALL_VIDEO_CODECS);
        String[] audioCodecs = FORMAT_AUDIO_CODECS.getOrDefault(format, ALL_AUDIO_CODECS);
        
        String currentVideo = (String) videoCodecCombo.getSelectedItem();
        String currentAudio = (String) audioCodecCombo.getSelectedItem();
        
        videoCodecCombo.setModel(new DefaultComboBoxModel<>(videoCodecs));
        audioCodecCombo.setModel(new DefaultComboBoxModel<>(audioCodecs));
        
        // Mantener selección si sigue siendo compatible, sino volver a (auto)
        if (Arrays.asList(videoCodecs).contains(currentVideo)) {
            videoCodecCombo.setSelectedItem(currentVideo);
        } else {
            videoCodecCombo.setSelectedIndex(0);
        }
        if (Arrays.asList(audioCodecs).contains(currentAudio)) {
            audioCodecCombo.setSelectedItem(currentAudio);
        } else {
            audioCodecCombo.setSelectedIndex(0);
        }
    }
    
    private JPanel createVideoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Vídeo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Resolución
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Resolución:"), gbc);
        
        JPanel resPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        widthField = new JTextField(5);
        widthField.setToolTipText("Ancho (vacío = original)");
        heightField = new JTextField(5);
        heightField.setToolTipText("Alto (vacío = original)");
        resPanel.add(widthField);
        resPanel.add(new JLabel("x"));
        resPanel.add(heightField);
        
        // Presets de resolución
        JComboBox<String> resPresets = new JComboBox<>(new String[]{
            "Original", "1920x1080", "1280x720", "854x480", "640x360"
        });
        resPresets.addActionListener(e -> {
            String sel = (String) resPresets.getSelectedItem();
            if (!"Original".equals(sel)) {
                String[] parts = sel.split("x");
                widthField.setText(parts[0]);
                heightField.setText(parts[1]);
            } else {
                widthField.setText("");
                heightField.setText("");
            }
        });
        resPanel.add(resPresets);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(resPanel, gbc);
        
        // Codec
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Codec:"), gbc);
        
        videoCodecCombo = new JComboBox<>(new String[]{
            "(auto)", "libx264", "libx265", "libvpx-vp9", "mpeg4", "copy"
        });
        videoCodecCombo.setPreferredSize(new Dimension(160, videoCodecCombo.getPreferredSize().height));
        gbc.gridx = 1;
        panel.add(videoCodecCombo, gbc);
        
        // Bitrate
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Bitrate (kbps):"), gbc);
        
        videoBitrateField = new JTextField(8);
        videoBitrateField.setToolTipText("Vacío = automático");
        gbc.gridx = 1;
        panel.add(videoBitrateField, gbc);
        
        // CRF
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("CRF (calidad):"), gbc);
        
        JPanel crfPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        crfSlider = new JSlider(0, 51, 23);
        crfSlider.setMajorTickSpacing(10);
        crfSlider.setPaintTicks(true);
        crfLabel = new JLabel("23");
        crfSlider.addChangeListener(e -> crfLabel.setText(String.valueOf(crfSlider.getValue())));
        crfPanel.add(crfSlider);
        crfPanel.add(crfLabel);
        crfPanel.add(new JLabel("(0=mejor, 51=peor)"));
        gbc.gridx = 1;
        panel.add(crfPanel, gbc);
        
        // Preset
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Preset:"), gbc);
        
        presetCombo = new JComboBox<>(new String[]{
            "ultrafast", "superfast", "veryfast", "faster", "fast", 
            "medium", "slow", "slower", "veryslow"
        });
        presetCombo.setSelectedItem("medium");
        gbc.gridx = 1;
        panel.add(presetCombo, gbc);
        
        return panel;
    }
    
    private JPanel createAudioPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Audio"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Codec
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Codec:"), gbc);
        
        audioCodecCombo = new JComboBox<>(new String[]{
            "(auto)", "aac", "libmp3lame", "libvorbis", "libopus", "flac", "copy"
        });
        audioCodecCombo.setPreferredSize(new Dimension(160, audioCodecCombo.getPreferredSize().height));
        gbc.gridx = 1;
        panel.add(audioCodecCombo, gbc);
        
        // Bitrate
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Bitrate (kbps):"), gbc);
        
        audioBitrateField = new JTextField(8);
        audioBitrateField.setToolTipText("Vacío = automático");
        gbc.gridx = 1;
        panel.add(audioBitrateField, gbc);
        
        // Sample rate
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Sample Rate (Hz):"), gbc);
        
        sampleRateCombo = new JComboBox<>(new String[]{
            "(original)", "48000", "44100", "22050", "16000"
        });
        gbc.gridx = 1;
        panel.add(sampleRateCombo, gbc);
        
        return panel;
    }
    
    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Salida"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Formato:"), gbc);
        
        formatCombo = new JComboBox<>(new String[]{
            "(original)", "mp4", "mkv", "avi", "webm", "mov", "flv"
        });
        gbc.gridx = 1;
        panel.add(formatCombo, gbc);
        
        return panel;
    }
    
    private JPanel createThumbnailPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Thumbnail"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Tiempo
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tiempo:"), gbc);
        
        thumbTimeField = new JTextField("00:00:01", 10);
        thumbTimeField.setToolTipText("HH:MM:SS o segundos");
        gbc.gridx = 1;
        panel.add(thumbTimeField, gbc);
        
        // Resolución
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Resolución:"), gbc);
        
        JPanel resPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        thumbWidthField = new JTextField(5);
        thumbHeightField = new JTextField(5);
        resPanel.add(thumbWidthField);
        resPanel.add(new JLabel("x"));
        resPanel.add(thumbHeightField);
        resPanel.add(new JLabel("(vacío = original)"));
        gbc.gridx = 1;
        panel.add(resPanel, gbc);
        
        // Formato
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Formato:"), gbc);
        
        thumbFormatCombo = new JComboBox<>(new String[]{"jpg", "png", "bmp", "webp"});
        gbc.gridx = 1;
        panel.add(thumbFormatCombo, gbc);
        
        // Calidad
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Calidad (1-31):"), gbc);
        
        thumbQualitySpinner = new JSpinner(new SpinnerNumberModel(2, 1, 31, 1));
        gbc.gridx = 1;
        panel.add(thumbQualitySpinner, gbc);
        
        return panel;
    }
    
    /**
     * Obtiene la configuración actual del panel.
     */
    public VideoSettings getSettings() {
        VideoSettings settings = new VideoSettings();
        
        // Vídeo
        if (!widthField.getText().isEmpty()) {
            settings.setWidth(Integer.parseInt(widthField.getText()));
        }
        if (!heightField.getText().isEmpty()) {
            settings.setHeight(Integer.parseInt(heightField.getText()));
        }
        
        String vCodec = (String) videoCodecCombo.getSelectedItem();
        if (!"(auto)".equals(vCodec)) {
            settings.setVideoCodec(vCodec);
        }
        
        if (!videoBitrateField.getText().isEmpty()) {
            settings.setVideoBitrate(Integer.parseInt(videoBitrateField.getText()));
        }
        
        if (crfSlider.getValue() != 23) {  // Solo si se cambió del default
            settings.setCrf(crfSlider.getValue());
        }
        
        settings.setPreset((String) presetCombo.getSelectedItem());
        
        // Audio
        String aCodec = (String) audioCodecCombo.getSelectedItem();
        if (!"(auto)".equals(aCodec)) {
            settings.setAudioCodec(aCodec);
        }
        
        if (!audioBitrateField.getText().isEmpty()) {
            settings.setAudioBitrate(Integer.parseInt(audioBitrateField.getText()));
        }
        
        String sampleRate = (String) sampleRateCombo.getSelectedItem();
        if (!"(original)".equals(sampleRate)) {
            settings.setAudioSampleRate(Integer.parseInt(sampleRate));
        }
        
        // Salida
        String format = (String) formatCombo.getSelectedItem();
        if (!"(original)".equals(format)) {
            settings.setOutputFormat(format);
        }
        
        // Thumbnail
        settings.setThumbnailTime(thumbTimeField.getText());
        if (!thumbWidthField.getText().isEmpty()) {
            settings.setThumbnailWidth(Integer.parseInt(thumbWidthField.getText()));
        }
        if (!thumbHeightField.getText().isEmpty()) {
            settings.setThumbnailHeight(Integer.parseInt(thumbHeightField.getText()));
        }
        settings.setThumbnailFormat((String) thumbFormatCombo.getSelectedItem());
        settings.setThumbnailQuality((Integer) thumbQualitySpinner.getValue());
        
        return settings;
    }
}
