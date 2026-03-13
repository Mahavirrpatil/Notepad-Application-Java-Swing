import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Notepad extends JFrame implements ActionListener {
    JTextArea textArea;
    JFileChooser fileChooser;
    JLabel lb = new JLabel("Welcome to MY_Notepad", JLabel.CENTER);
    JTabbedPane tabbedpane = new JTabbedPane();

    public Notepad() {
        setTitle("MY_Notepad ");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Jlabel
        JPanel jp = new JPanel();
        lb.setFont(new Font("arial", Font.BOLD, 40));
        jp.add(lb);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newFile = new JMenuItem("New");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");
        JMenuItem exit = new JMenuItem("Exit");

        newFile.addActionListener(this);
        openFile.addActionListener(this);
        saveFile.addActionListener(this);
        exit.addActionListener(this);

        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(exit);

        // Edit Menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem cut = new JMenuItem("Cut");
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem paste = new JMenuItem("Paste");

        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);

        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);

        // cut.setShortcut(new MenuShortcut(KeyEvent.VK_c));

        JMenu about = new JMenu("About");
        JMenuItem help = new JMenuItem("Help");
        about.add(help);

        about.addActionListener(this);
        help.addActionListener(this);

        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(about);
        setJMenuBar(menuBar);

        fileChooser = new JFileChooser();

        // JTabbedPane
        add(tabbedpane);
        tabbedpane.addTab("Welcome", jp);
        tabbedpane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedpane.getSelectedIndex();
                if (selectedIndex != -1) {
                    setTitle("MY_Notepad - " + tabbedpane.getTitleAt(selectedIndex));
                }
            }
        });
    }


    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "New":
    try {
        JTextArea tab = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(tab);
        tab.setFont(new Font("Plain", Font.PLAIN, 20));
        String str = "Untitled";

        // Create Swing-based popup menu
        JPopupMenu pm = new JPopupMenu();
        JMenuItem ext = new JMenuItem("Exit");
        ext.addActionListener(ev -> System.exit(0)); // Handle exit action
        pm.add(ext);

        // Add mouse listener to handle popup trigger
        tab.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                showPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }

            private void showPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    pm.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        tabbedpane.addTab(str, scrollPane);
        tabbedpane.setSelectedComponent(scrollPane);

    } catch (Exception ex) {
        System.out.println("Error in new file");
    }
    break;

            case "Open":
                openFile();
                break;

            case "Save":
                saveFile();
                break;

            case "Exit":
                System.exit(0);
                break;

            case "Cut":
            case "Copy":
            case "Paste":
                // Ensure the current tab's text area is accessed
                JTextArea currentTextArea = getCurrentTextArea();
                if (currentTextArea != null) {
                    if (command.equals("Cut")) {
                        currentTextArea.cut();
                    } else if (command.equals("Copy")) {
                        currentTextArea.copy();
                    } else if (command.equals("Paste")) {
                        currentTextArea.paste();
                    }
                }
                break;

            case "Help":
                JOptionPane.showMessageDialog(this, "This Application is developed by Mahavir");
                break;
        }
    }

    private void openFile() {
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                JTextArea tab = new JTextArea();
                JScrollPane scrollPane = new JScrollPane(tab);
                tab.read(br, null);
                tab.setFont(new Font("Plain", Font.PLAIN, 20));
                String str = file.getName();
                tabbedpane.addTab(str, scrollPane);
                tabbedpane.setSelectedComponent(scrollPane);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error opening file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                JTextArea currentTextArea = getCurrentTextArea();
                if (currentTextArea != null) {
                    currentTextArea.write(bw);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JTextArea getCurrentTextArea() {
        JScrollPane currentScrollPane = (JScrollPane) tabbedpane.getSelectedComponent();
        if (currentScrollPane != null) {
            return (JTextArea) currentScrollPane.getViewport().getView();
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Notepad().setVisible(true));
    }
}
