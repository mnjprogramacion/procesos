package ui;

import model.VideoSettings;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de configuración básica con presets de calidad predefinidos.
 */
public class BasicSettingsPanel extends JPanel {
    private JComboBox<String> qualityCombo;
    private JComboBox<String> formatCombo;
    private JTextField thumbTimeField;

    // Preset descriptions for tooltip
    private static final String[] QUALITY_NAMES = {
        "Baja", "Media", "Alta", "Muy alta", "Máxima (lossless)"
    };

    public BasicSettingsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createQualityPanel());
        add(Box.createVerticalStrut(10));
        add(createOutputPanel());
        add(Box.createVerticalStrut(10));
        add(createThumbnailPanel());
        add(Box.createVerticalGlue());
    }

    private JPanel createQualityPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Calidad"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Preset:"), gbc);

        qualityCombo = new JComboBox<>(QUALITY_NAMES);
        qualityCombo.setSelectedIndex(1); // Media por defecto
        gbc.gridx = 1;
        panel.add(qualityCombo, gbc);

        // Description label
        JLabel descLabel = new JLabel();
        descLabel.setFont(descLabel.getFont().deriveFont(Font.ITALIC, 11f));
        descLabel.setForeground(Color.GRAY);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(descLabel, gbc);

        qualityCombo.addActionListener(e -> descLabel.setText(getPresetDescription()));
        descLabel.setText(getPresetDescription());

        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Salida"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Formato:"), gbc);

        formatCombo = new JComboBox<>(new String[]{
            "(original)", "mp4", "mkv", "avi", "webm"
        });
        gbc.gridx = 1;
        panel.add(formatCombo, gbc);

        return panel;
    }

    private JPanel createThumbnailPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Thumbnail"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tiempo:"), gbc);

        thumbTimeField = new JTextField("00:00:01", 10);
        thumbTimeField.setToolTipText("HH:MM:SS o segundos");
        gbc.gridx = 1;
        panel.add(thumbTimeField, gbc);

        return panel;
    }

    private String getPresetDescription() {
        int idx = qualityCombo.getSelectedIndex();
        switch (idx) {
            case 0: return "CRF 32 · 640x360 · Rápido · Archivo pequeño";
            case 1: return "CRF 28 · 1280x720 · Equilibrado";
            case 2: return "CRF 23 · 1920x1080 · Buena calidad";
            case 3: return "CRF 18 · Resolución original · Alta calidad";
            case 4: return "CRF 0 · Resolución original · Sin pérdida";
            default: return "";
        }
    }

    /**
     * Obtiene la configuración actual del panel.
     */
    public VideoSettings getSettings() {
        VideoSettings settings = new VideoSettings();
        int idx = qualityCombo.getSelectedIndex();

        switch (idx) {
            case 0: // Baja
                settings.setCrf(32);
                settings.setPreset("veryfast");
                settings.setWidth(640);
                settings.setHeight(360);
                settings.setAudioBitrate(96);
                break;
            case 1: // Media
                settings.setCrf(28);
                settings.setPreset("medium");
                settings.setWidth(1280);
                settings.setHeight(720);
                settings.setAudioBitrate(128);
                break;
            case 2: // Alta
                settings.setCrf(23);
                settings.setPreset("medium");
                settings.setWidth(1920);
                settings.setHeight(1080);
                settings.setAudioBitrate(192);
                break;
            case 3: // Muy alta
                settings.setCrf(18);
                settings.setPreset("slow");
                settings.setAudioBitrate(256);
                break;
            case 4: // Máxima (lossless)
                settings.setCrf(0);
                settings.setPreset("veryslow");
                settings.setAudioBitrate(320);
                break;
        }

        // Formato
        String format = (String) formatCombo.getSelectedItem();
        if (!"(original)".equals(format)) {
            settings.setOutputFormat(format);
        }

        // Thumbnail
        settings.setThumbnailTime(thumbTimeField.getText());

        return settings;
    }
}
