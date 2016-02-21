package Scanner;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by anders on 22/12/15.
 */
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("ImgurScanner");

        Container contentPane = frame.getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel labelUrl = new JLabel("URL", JLabel.TRAILING);
        JTextField urlText = new JTextField(25);
        JButton pasteButton = new JButton("Paste");

        JLabel labelDest = new JLabel("Destination", JLabel.TRAILING);
        JTextField destText = new JTextField(25);
        JButton browseButton = new JButton("Browse");

        contentPane.add(labelUrl);
        contentPane.add(urlText);
        contentPane.add(pasteButton);
        contentPane.add(labelDest);
        contentPane.add(destText);
        contentPane.add(browseButton);

        labelUrl.setLabelFor(urlText);
        labelDest.setLabelFor(destText);

        SpringUtilities.makeCompactGrid(contentPane, 2, 3, 6, 6, 6, 6);

        JButton downloadButton = new JButton("Download");
        JProgressBar progress = new JProgressBar(0, 10);
        progress.setVisible(false);

        contentPane.add(downloadButton);
        contentPane.add(progress);

        layout.putConstraint(SpringLayout.NORTH, downloadButton, 6, SpringLayout.SOUTH, destText);
        layout.putConstraint(SpringLayout.SOUTH, contentPane, 6, SpringLayout.SOUTH, downloadButton);
        layout.putConstraint(SpringLayout.WEST, downloadButton, 0, SpringLayout.WEST, destText);
        layout.putConstraint(SpringLayout.EAST, downloadButton, 0, SpringLayout.EAST, destText);

        layout.putConstraint(SpringLayout.NORTH, progress, 0, SpringLayout.NORTH, downloadButton);
        layout.putConstraint(SpringLayout.SOUTH, progress, 0, SpringLayout.SOUTH, downloadButton);
        layout.putConstraint(SpringLayout.WEST, progress, 0, SpringLayout.WEST, downloadButton);
        layout.putConstraint(SpringLayout.EAST, progress, 0, SpringLayout.EAST, downloadButton);

        pasteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable t = c.getContents(this);

                if(t == null) return;

                try {
                    urlText.setText((String) t.getTransferData(DataFlavor.stringFlavor));
                }
                catch (Exception err) {
                    err.printStackTrace();
                }
            }
        });

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
                try {
                    DownloadHandler downloadHandler = new DownloadHandler();
                    downloadHandler.setUrl(new URL(urlText.getText()));
                    downloadHandler.setDestination(new File(destText.getText()));
                    downloadHandler.setClientID(args[0]);
                    downloadHandler.begin();
                }
                catch(MalformedURLException err) {
                    err.printStackTrace();
                }
            }
        });

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();

        frame.setVisible(true);
    }
}
