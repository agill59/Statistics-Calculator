package ui;

import model.BinomialCalculator;
import model.NormalCalculator;
import model.RawData;
import persistence.JsonReader;
import persistence.JsonWriter;
import model.EventLog;
import model.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

// GUI Application; References: InternalFrameDemo.Java found on docs.Oracle.com on javaswing tutorials,
// https://www.guru99.com/java-swing-gui.html
public class GUI extends JFrame implements ActionListener {

    private static JTextField probOf1BC = new JTextField(20);
    private static JTextField trials = new JTextField(20);
    private static JTextField successes = new JTextField(20);
    private static JTextField probBC = new JTextField(20);
    private static JTextField meanNC = new JTextField(20);
    private static JTextField sdNC = new JTextField(20);
    private static JTextField point1 = new JTextField(20);
    private static JTextField point2 = new JTextField(20);
    private static JTextField probNC = new JTextField(20);
    private static JTextField listX = new JTextField(20);
    private static JTextField listY = new JTextField(20);
    private static JTextField meanY = new JTextField(20);
    private static JTextField sdY = new JTextField(20);
    private final JDesktopPane desktop;
    private BinomialCalculator binomialCalculator;
    private NormalCalculator normalCalculator;
    private RawData rd;
    private JButton saveButtonBC;
    private JButton saveButtonNC;
    private JButton saveButtonRD;
    private JButton loadButtonBC;
    private JButton loadButtonNC;
    private JButton loadButtonRD;
    EventLog el = EventLog.getInstance();
    ImageIcon calculateIcon = createImageIcon("./data/calculator pic.png");



    private ActionListener bcProbability = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Double probability = Double.parseDouble(probOf1BC.getText());
            int trials = Integer.parseInt(GUI.trials.getText());
            int successes = Integer.parseInt(GUI.successes.getText());
            binomialCalculator = new BinomialCalculator(probability, trials);
            probBC.setText("" + binomialCalculator.probability(successes));

        }
    };
    private ActionListener ncProbability = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Double mean = Double.parseDouble(meanNC.getText());
            Double sd = Double.parseDouble(sdNC.getText());
            Double pt1 = Double.parseDouble(point1.getText());
            Double pt2 = Double.parseDouble(point2.getText());
            normalCalculator = new NormalCalculator(mean, sd);
            probNC.setText("" + normalCalculator.normalProbability(pt1, pt2));

        }
    };
    private ActionListener rawData = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            rd = new RawData(listX.getText(), listY.getText());
            meanY.setText("" + rd.getMeanY());
            sdY.setText("" + rd.getStandardDevY());
        }
    };
    private ActionListener saveBC = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JsonWriter jsonWriter = new JsonWriter("./data/binomialCalculator.json");
            try {
                jsonWriter.open();
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(desktop, "File Not Found");
            }
            jsonWriter.writeBC(binomialCalculator);
            jsonWriter.close();


        }
    };
    private ActionListener saveNC = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JsonWriter jsonWriter = new JsonWriter("./data/normalCalculator.json");
            try {
                jsonWriter.open();
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(desktop, "File Not Found");
            }
            jsonWriter.writeNC(normalCalculator);
            jsonWriter.close();
        }
    };
    private ActionListener saveRD = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JsonWriter jsonWriter = new JsonWriter("./data/rawData.json");
            try {
                jsonWriter.open();
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(desktop, "File Not Found");
            }
            jsonWriter.writeRD(rd);
            jsonWriter.close();
        }
    };

    private ActionListener loadBC = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JsonReader jsonReader = new JsonReader("./data/binomialCalculator.json");
            try {
                binomialCalculator = jsonReader.readBC();
                probOf1BC.setText("" + binomialCalculator.getProbabilityOfSuccess());
                trials.setText("" + binomialCalculator.getTrials());
                successes.setText("" + binomialCalculator.getSuccesses());
                probBC.setText("" + binomialCalculator.getProbability());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(desktop, "File Not Found");
            }
        }
    };

    private ActionListener loadNC = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JsonReader jsonReader = new JsonReader("./data/normalCalculator.json");
            try {
                normalCalculator = jsonReader.readNC();
                meanNC.setText("" + normalCalculator.getMean());
                sdNC.setText("" + normalCalculator.getSd());
                point1.setText("" + normalCalculator.getPoint1());
                point2.setText("" + normalCalculator.getPoint2());
                probNC.setText("" + normalCalculator.getProbability());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(desktop, "File Not Found");
            }
        }
    };

    private ActionListener loadRD = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JsonReader jsonReader = new JsonReader("./data/rawData.json");
            try {
                rd = jsonReader.readRD();
                String strX = (rd.listToString(rd.getListX()));
                listX.setText(strX);
                String strY = (rd.listToString(rd.getListY()));
                listY.setText(strY);
                meanY.setText("" + rd.getMeanY());
                sdY.setText("" + rd.getStandardDevY());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(desktop, "File Not Found");
            }
        }
    };

    //Effects: run the GUI application
    public GUI() {
        super("Calculator");
        int indent = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(indent, indent,
                screenSize.width - indent * 2,
                screenSize.height - indent * 2);

        desktop = new JDesktopPane();
        setContentPane(desktop);
        setJMenuBar(createMenuBar(this));
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);


    }

    //Effects: creates a menu bar at top of desktop frame
    private JMenuBar createMenuBar(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);

        quitProgram(menu);

        menu = new JMenu("Binomial Calculator");
        menuBar.add(menu);


        createBinomialCalculator(menu);


        menu = new JMenu("Normal Calculator");
        menuBar.add(menu);

        createNormalCalculator(menu);


        menu = new JMenu("Raw Data");
        menuBar.add(menu);

        createRawData(menu);


        return menuBar;
    }

    //Modifies: This
    //Effects: Creates a new tab under raw data menu tab
    private void createRawData(JMenu menu) {
        JMenuItem menuItem = new JMenuItem("New");
        menuItem.setMnemonic(KeyEvent.VK_R);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_R, InputEvent.ALT_MASK));
        menuItem.setActionCommand("Raw Data");
        menuItem.addActionListener(this);
        menu.add(menuItem);
    }

    //Modifies: This
    //Effects: Creates a new tab under normal calculator menu tab
    private void createNormalCalculator(JMenu menu) {
        JMenuItem menuItem = new JMenuItem("New");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, InputEvent.ALT_MASK));
        menuItem.setActionCommand("Normal Calculator");
        menuItem.addActionListener(this);
        menu.add(menuItem);
    }

    //Modifies: This
    //Effects: Creates a new tab under binomial calculator menu tab
    private void createBinomialCalculator(JMenu menu) {
        JMenuItem menuItem = new JMenuItem("new");
        menuItem.setMnemonic(KeyEvent.VK_B);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_B, InputEvent.ALT_MASK));
        menuItem.setActionCommand("Binomial Calculator");
        menuItem.addActionListener(this);
        menu.add(menuItem);
    }

    //Modifies: This
    //Effects: Creates a quit tab on file menu bar
    private void quitProgram(JMenu menu) {
        JMenuItem menuItem = new JMenuItem("Quit");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, InputEvent.ALT_MASK));
        menuItem.setActionCommand("quit");
        menuItem.addActionListener(this);
        menu.add(menuItem);
    }

    //Modifies: desktop
    //Effects: creates a new frame on the desktop
    private void createFrame() {
        MyInternalFrame frame = new MyInternalFrame();
        frame.setVisible(true);
        desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException ignored) {
            return;
        }
    }

    //Modifies: desktop
    //Effects: creates a new frame with a binomial calculator on the desktop
    private void createBinomialFrame() {
        MyInternalFrame frame = new MyInternalFrame();
        frame.setVisible(true);
        desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException ignored) {
            return;
        }
        setUpBinomialCalculator(frame);
    }

    // Modifies: This
    // Effects: Creates the layout of the binomial calculator frame
    private void setUpBinomialCalculator(MyInternalFrame frame) {
        JPanel p = new JPanel();
        p.setName("Binomial Calculator");
        p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        p.setLayout(new GridLayout(0, 1));
        panelSetupBC(p);
        frame.add(p);
    }

    // Modifies: This
    // Effects: Add buttons,text fields and their appropriate labels
    private void panelSetupBC(JPanel p) {
        p.add(new JLabel("Probability of 1 trial"));
        p.add(probOf1BC);
        p.add(new JLabel("Trials"));
        p.add(trials);
        p.add(new JLabel("Successes"));
        p.add(successes);
        p.add(new JLabel("Probability"));
        probBC.setEditable(false);
        p.add(probBC);
        probBC.setBackground(Color.WHITE);
        JButton calculate = new JButton(calculateIcon);
        calculate.setText("Calculate");
        calculate.addActionListener(bcProbability);
        p.add(calculate);
        saveButtonBC = new JButton();
        saveButtonBC.setText("Save");
        saveButtonBC.addActionListener(saveBC);
        p.add(saveButtonBC);
        loadButtonBC = new JButton();
        loadButtonBC.setText("Load");
        loadButtonBC.addActionListener(loadBC);
        p.add(loadButtonBC);
    }

    //Effects: Determines which button is pressed and creates appropriate frame
    public void actionPerformed(ActionEvent e) {
        if ("Binomial Calculator".equals(e.getActionCommand())) {
            createBinomialFrame();
        } else if ("Normal Calculator".equals(e.getActionCommand())) {
            createNormalFrame();
        } else if ("Raw Data".equals(e.getActionCommand())) {
            createRawDataFrame();
        } else {
            quit();
        }
    }

    //Modifies: desktop
    //Effects: creates a new frame with a raw data calculator on the desktop
    private void createRawDataFrame() {

        MyInternalFrame frame = new MyInternalFrame();
        frame.setVisible(true);
        desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException ignored) {
            return;
        }
        setUpRawData(frame);
    }

    // Modifies: This
    // Effects: Creates the layout of the raw data frame
    private void setUpRawData(MyInternalFrame frame) {
        JPanel p = new JPanel();
        p.setName("Raw Data");
        p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        p.setLayout(new GridLayout(0, 1));
        panelSetupRD(p);
        frame.add(p);
    }

    // Modifies: This
    // Effects: Add buttons,text fields and their appropriate labels
    private void panelSetupRD(JPanel p) {
        p.add(new JLabel("List X"));
        p.add(listX);
        p.add(new JLabel("List Y"));
        p.add(listY);
        p.add(new JLabel("Mean Y"));
        meanY.setEditable(false);
        p.add(meanY);
        meanY.setBackground(Color.WHITE);
        p.add(new JLabel("Standard Deviation Y"));
        sdY.setEditable(false);
        p.add(sdY);
        sdY.setBackground(Color.WHITE);
        JButton calculate = new JButton(calculateIcon);
        calculate.setText("Calculate");
        calculate.addActionListener(rawData);
        p.add(calculate);
        rdSaveLoadButtons(p);
    }

    // Modifies: This
    // Effects: Add save/load buttons and their appropriate labels
    private void rdSaveLoadButtons(JPanel p) {
        saveButtonRD = new JButton();
        saveButtonRD.setText("Save");
        saveButtonRD.addActionListener(saveRD);
        p.add(saveButtonRD);
        loadButtonRD = new JButton();
        loadButtonRD.setText("Load");
        loadButtonRD.addActionListener(loadRD);
        p.add(loadButtonRD);
    }

    //Modifies: desktop
    //Effects: creates a new frame with a normal calculator on the desktop
    private void createNormalFrame() {

        MyInternalFrame frame = new MyInternalFrame();
        frame.setVisible(true);
        desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException ignored) {
            return;
        }
        setUpNormalCalculator(frame);
    }

    // Modifies: This
    // Effects: Creates the layout of the normal calculator frame
    private void setUpNormalCalculator(MyInternalFrame frame) {
        JPanel p = new JPanel();
        p.setName("Normal Calculator");
        p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        p.setLayout(new GridLayout(0, 1));
        panelSetupNC(p);
        frame.add(p);
    }

    // Modifies: This
    // Effects: Add buttons,text fields and their appropriate labels
    private void panelSetupNC(JPanel p) {
        p.add(new JLabel("Mean"));
        p.add(meanNC);
        p.add(new JLabel("Standard Deviation"));
        p.add(sdNC);
        p.add(new JLabel("Point 1"));
        p.add(point1);
        p.add(new JLabel("Point 2"));
        p.add(point2);
        p.add(new JLabel("Probability"));
        probNC.setEditable(false);
        p.add(probNC);
        probNC.setBackground(Color.WHITE);
        JButton calculate = new JButton(calculateIcon);
        calculate.setText("Calculate");
        calculate.addActionListener(ncProbability);
        p.add(calculate);
        ncSaveLoadButtons(p);
    }

    // Modifies: This
    // Effects: Add save/load buttons and their appropriate labels
    private void ncSaveLoadButtons(JPanel p) {
        saveButtonNC = new JButton();
        saveButtonNC.setText("Save");
        saveButtonNC.addActionListener(saveNC);
        p.add(saveButtonNC);
        loadButtonNC = new JButton();
        loadButtonNC.setText("Load");
        loadButtonNC.addActionListener(loadNC);
        p.add(loadButtonNC);
    }

    private static ImageIcon createImageIcon(String path) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image imgURL = tk.getImage(path);
        Image scaledImage = imgURL.getScaledInstance(50,50,50);
        if (imgURL != null) {
            return new ImageIcon(scaledImage);
        } else {
            return null;
        }

    }

    //Effects: Quit GUI
    protected void quit() {
        for (Event event: el) {
            System.out.println(event);
        }
        System.exit(0);
    }

    //Effects: Creates GUI when main is run
    private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        GUI frame = new GUI();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }

    //Effects: run GUI class
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }


}
