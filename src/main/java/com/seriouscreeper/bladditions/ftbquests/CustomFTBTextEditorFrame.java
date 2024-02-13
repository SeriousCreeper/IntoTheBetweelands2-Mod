package com.seriouscreeper.bladditions.ftbquests;

import com.feed_the_beast.ftbquests.net.edit.MessageEditObject;
import com.feed_the_beast.ftbquests.quest.Quest;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class CustomFTBTextEditorFrame extends JFrame {
    private static BufferedImage logo;
    public final Quest quest;
    private final String originalTitle;
    private final String originalSubtitle;
    private final String originalDescription;
    public final JTextField title;
    public final JTextField subtitle;
    public final JTextArea description;
    public final JButton save;
    public final JButton setColor;
    public final JButton reset;

    public static void open(Quest quest) {
        (new com.seriouscreeper.bladditions.ftbquests.CustomFTBTextEditorFrame(quest)).requestFocus();
    }

    private CustomFTBTextEditorFrame(Quest q) {
        super("Custom FTB Quests Text Editor | " + q.chapter.getTitle() + " | " + q.getTitle());
        this.quest = q;
        this.originalTitle = this.quest.title;
        this.originalSubtitle = this.quest.subtitle;
        this.originalDescription = String.join("\n", this.quest.description);
        if (logo == null) {
            try {
                InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("ftbquests", "textures/logotransparent.png")).getInputStream();
                Throwable var3 = null;

                try {
                    logo = ImageIO.read(stream);
                } catch (Throwable var13) {
                    var3 = var13;
                    throw var13;
                } finally {
                    if (stream != null) {
                        if (var3 != null) {
                            try {
                                stream.close();
                            } catch (Throwable var12) {
                                var3.addSuppressed(var12);
                            }
                        } else {
                            stream.close();
                        }
                    }

                }
            } catch (Exception var15) {
            }
        }

        if (logo != null) {
            this.setIconImage(logo);
        }

        this.setResizable(true);
        this.setDefaultCloseOperation(2);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, 3));
        panel.add(this.title = new JTextField(this.originalTitle, 75));
        panel.add(this.subtitle = new JTextField(this.originalSubtitle, 75));
        panel.add(this.description = new JTextArea(this.originalDescription, 30, 75));
        this.title.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true), I18n.format("ftbquests.title", new Object[0])));
        this.subtitle.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true), I18n.format("ftbquests.quest.subtitle", new Object[0])));
        this.description.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true), I18n.format("ftbquests.quest.description", new Object[0])));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(this.reset = new JButton("Reset"));
        buttonPanel.add(this.save = new JButton("Save"));
        buttonPanel.add(this.setColor = new JButton("Highlight"));
        this.reset.addActionListener(this::resetClicked);
        this.save.addActionListener(this::saveClicked);
        this.setColor.addActionListener(this::highlight);
        panel.add(buttonPanel);
        this.setContentPane(panel);
        this.pack();
        this.setLocationRelativeTo((Component)null);
        this.setVisible(true);
    }

    private void resetClicked(ActionEvent event) {
        this.title.setText(this.originalTitle);
        this.subtitle.setText(this.originalSubtitle);
        this.description.setText(this.originalDescription);
    }

    private void saveClicked(ActionEvent event) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            this.quest.title = this.title.getText().trim();
            this.quest.subtitle = this.subtitle.getText().trim();
            this.quest.description.clear();
            this.quest.description.addAll(Arrays.asList(this.description.getText().split("\n")));
            this.quest.clearCachedData();
            this.setTitle("FTB Quests Text Editor | " + this.quest.chapter.getTitle() + " | " + this.quest.getTitle());
            (new MessageEditObject(this.quest)).sendToServer();
        });
    }

    private void highlight(ActionEvent event) {
        String selectedText = this.description.getSelectedText();
        String newText = TextFormatting.GOLD + selectedText + TextFormatting.RESET;
        this.description.replaceSelection(newText);
    }
}
