package Scanner;

import org.apache.commons.configuration2.ex.ConfigurationException;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by anders on 22/12/15.
 */
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("ImgurScanner");
        ConfigHandler conf = null;
        String id = null;

        try {
            conf = new ConfigHandler("config.properties");
            id = conf.getString("ClientID");
            System.out.print(id);
        }
        catch (Exception e) {
            System.out.print("Client ID not specified.");
            e.printStackTrace();
        }

        final ConfigHandler config = conf;
        final String clientID = id;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }

        Container contentPane = frame.getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel labelUrl = new JLabel("Gallery ID", JLabel.TRAILING);
        JTextField urlText = new JTextField(25);

        JLabel labelDest = new JLabel("Destination", JLabel.TRAILING);
        JTextField destText = new JTextField(25);
        JButton browseButton = new JButton("Browse");

        JPanel pagesPanel = new JPanel();
        JLabel labelPageLimit = new JLabel("Pages", JLabel.TRAILING);
        JSpinner pageLimit = new JSpinner();
        pageLimit.setValue(1);

        pagesPanel.add(labelPageLimit);
        pagesPanel.add(pageLimit);

        contentPane.add(labelUrl);
        contentPane.add(urlText);
        contentPane.add(pagesPanel);
        contentPane.add(labelDest);
        contentPane.add(destText);
        contentPane.add(browseButton);

        labelUrl.setLabelFor(urlText);
        labelDest.setLabelFor(destText);
        labelPageLimit.setLabelFor(pageLimit);

        SpringUtilities.makeCompactGrid(contentPane, 2, 3, 6, 6, 6, 6);

        JButton downloadButton = new JButton("Download");
        JProgressBar progress = new JProgressBar(0, 10);
        progress.setVisible(false);

        JButton optionsButton = new JButton("Options");


        contentPane.add(downloadButton);
        contentPane.add(progress);
        contentPane.add(optionsButton);

        layout.putConstraint(SpringLayout.NORTH, downloadButton, 6, SpringLayout.SOUTH, destText);
        layout.putConstraint(SpringLayout.SOUTH, contentPane, 6, SpringLayout.SOUTH, downloadButton);
        layout.putConstraint(SpringLayout.WEST, downloadButton, 0, SpringLayout.WEST, destText);
        layout.putConstraint(SpringLayout.EAST, downloadButton, 0, SpringLayout.EAST, destText);

        layout.putConstraint(SpringLayout.NORTH, progress, 0, SpringLayout.NORTH, downloadButton);
        layout.putConstraint(SpringLayout.SOUTH, progress, 0, SpringLayout.SOUTH, downloadButton);
        layout.putConstraint(SpringLayout.WEST, progress, 0, SpringLayout.WEST, downloadButton);
        layout.putConstraint(SpringLayout.EAST, progress, 0, SpringLayout.EAST, downloadButton);

        layout.putConstraint(SpringLayout.NORTH, optionsButton, 0, SpringLayout.NORTH, downloadButton);
        layout.putConstraint(SpringLayout.SOUTH, optionsButton, 0, SpringLayout.SOUTH, downloadButton);
        layout.putConstraint(SpringLayout.WEST, optionsButton, 0, SpringLayout.WEST, browseButton);
        layout.putConstraint(SpringLayout.EAST, optionsButton, 0, SpringLayout.EAST, browseButton);

        if(config.getString("ClientID").length() > 0) {
            downloadButton.setEnabled(true);
        }
        else {
            downloadButton.setEnabled(false);
        }

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setDialogTitle("Destination Directory");
                int result = chooser.showOpenDialog(contentPane);

                if(result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = chooser.getSelectedFile();
                    destText.setText(selectedFile.toString());
                }
            }
        });

        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DownloadHandler downloadHandler = new DownloadHandler(new Callback() {
                    @Override
                    public void callback() {
                        progress.setVisible(false);
                        progress.repaint();
                        downloadButton.setVisible(true);
                        downloadButton.repaint();
                    }
                });
                downloadHandler.setGalleryID(urlText.getText());
                downloadHandler.setDestination(new File(destText.getText()));
                downloadHandler.setClientID(clientID);
                downloadHandler.setApiGalleryBaseURL("https://api.imgur.com/3/");
                downloadHandler.setPageLimit((int)pageLimit.getValue());
                downloadHandler.setProgress(progress);

                downloadButton.setVisible(false);
                downloadButton.repaint();
                progress.setVisible(true);
                progress.repaint();

                downloadHandler.start();
            }
        });

        optionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFrame frame = new JFrame("Options");

                Container contentPane = frame.getContentPane();
                SpringLayout layout = new SpringLayout();
                contentPane.setLayout(layout);

                JLabel labelId = new JLabel("Client ID", JLabel.TRAILING);
                JTextField textId = new JTextField(25);
                JButton saveButton = new JButton("Save");

                contentPane.add(labelId);
                contentPane.add(textId);
                contentPane.add(saveButton);

                SpringUtilities.makeCompactGrid(contentPane, 1, 3, 6, 6, 6, 6);

                textId.setText(config.getString("ClientID"));

                saveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            config.setProperty("ClientID", textId.getText());
                            config.save();
                            frame.dispose();
                        }
                        catch(ConfigurationException ex) {
                            ex.printStackTrace();
                        }

                        if(config.getString("ClientID").length() > 0) {
                            downloadButton.setEnabled(true);
                        }
                        else {
                            downloadButton.setEnabled(false);
                        }
                    }
                });

                frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                frame.pack();

                frame.setVisible(true);
            }
        });

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();

        frame.setVisible(true);
    }
}
