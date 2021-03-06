package learner;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.xml.crypto.Data;

/**
 * Created by Dylan on 18-1-2017.
 */
public class GUI {

    private JButton insertData;
    private JPanel panel1;
    private JButton startLearningButton;
    private JTabbedPane tabbedPane1;
    private JLabel warningLabel;
    private JLabel pathMessage;
    private JButton insertTestData;
    private JLabel pathMessage2;
    private JButton startButton;
    private JPanel Testing;
    private JPanel Training;
    private JTextField chiValue;
    private JTextField vocabularySize;
    private JLabel structureMessage;
    private JLabel classNames;
    private static String directoryPath;
    private DocumentLearner documentLearner;
    private File learnFile;
    private File testFile;
    private DataClass verifiedClass;

    public GUI() {

        JFrame frame = new JFrame("Interactive Learner");
        warningLabel.setText("<html>Important! The folder selected must contain the train folders seperated. <br> " +
                "Example: Train/M and Train/F where Train is the selected folder.<html>");
        pathMessage.setText("No train folder specified. Please select a folder first ");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) dim.getWidth() / 2, (int) dim.getHeight() / 2);
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        frame.setVisible(true);
        documentLearner = new DocumentLearner(this);


        insertTestData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    directoryPath = fileChooser.getSelectedFile().getAbsolutePath();
                    testFile = fileChooser.getSelectedFile();
                    if (fileChooser.getSelectedFile().isDirectory()) {
                        pathMessage2.setText("You have selected the folder: " + directoryPath);
                    }
                }
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (directoryPath == null) {
                    String message = "Test data needs to be entered before you can start testing";
                    Dialog dialog = new Dialog(message);

                } else {
                    ResultTable resultTable = documentLearner.classify(testFile);
                    String message = "<html>Documents classified: " + resultTable.getTotalResults() + "<br>";
                    for (DataClass c : DataClass.getClasses()) {
                        message += resultTable.getCorrect(c) + " out of " + resultTable.getTrueAmount(c) + " correct for: "
                        + c.getName() + "<br>";
                    }
                    message += "Accuracy: " + resultTable.getAccuracy() + "<html>";
                    Dialog dialog = new Dialog(message);
                }
            }
        });

        insertData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    learnFile = fileChooser.getSelectedFile();
                    directoryPath = fileChooser.getSelectedFile().getAbsolutePath();
                    //pathMessage.setText("selected folder: " + fileChooser.getSelectedFile().getName());
                    pathMessage.setText("selected folder: " + directoryPath);
                }

            }
        });
        startLearningButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int vocabularyData = -1;
                float chiData = -1;
                try {
                    vocabularyData = Integer.parseInt(vocabularySize.getText());
                } catch (NumberFormatException error) {
                    Dialog dialog = new Dialog("Invalid vocabulary size, please insert a whole number");
                    return;
                }
                try {
                    chiData = Float.parseFloat(chiValue.getText());
                } catch (NumberFormatException error1) {
                    Dialog dialog = new Dialog("Invalid chi value, please enter a decimal number");
                    return;
                }

                if (directoryPath == null) {
                    String message = "Train data needs to be entered before you can start training";
                    Dialog dialog = new Dialog(message);

                } else {
                    //System.out.println(file.getName());
                    documentLearner.loadTrainingData(learnFile, vocabularyData, chiData);
                }
            }
        });
    }
}
