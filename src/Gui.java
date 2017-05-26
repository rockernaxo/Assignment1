import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

import CIM.ComplexNumber;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.awt.event.ActionEvent;

public class Gui extends JFrame {

	private File fileEQ, fileSSH;

	public static void main(String[] args) {

		new Gui();
	}

	public Gui() {

		createMenuBar();

		// Make sure the program exits when the frame closes
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Assignment 1 - Y matrix calculation");
		setSize(700, 600);

		setLayout(new BorderLayout());
		JLabel arnold = new JLabel(
				new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("kth.png"))));
		add(arnold);

		// This will center the JFrame in the middle of the screen
		setLocationRelativeTo(null);

		// Make JFrame visible
		setVisible(true);
	}

	private void createMenuBar() {

		JMenuBar menubar = new JMenuBar();

		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);

		JMenuItem menuEQ = new JMenuItem("Upload EQ File");
		JMenuItem menuSSH = new JMenuItem("Upload SSH File");
		JMenuItem menuCal = new JMenuItem("Calculate Y Matrix");
		menuEQ.setToolTipText("Only CIM-XML files are suppported");
		menuSSH.setToolTipText("Only CIM-XML files are suppported");
		menuSSH.setEnabled(false);
		menuCal.setToolTipText("Computes the Y matrix using an innovative method");
		menuCal.setEnabled(false);

		menuEQ.addActionListener((ActionEvent event) -> {
			this.fileEQ = selectFile();
			if (this.fileEQ == null) {
				JOptionPane.showMessageDialog(null, "You need to load a valid CIM file");
			} else {
				menuSSH.setEnabled(true);
			}
		});

		menuSSH.addActionListener((ActionEvent event) -> {
			this.fileSSH = selectFile();
			if (this.fileSSH == null) {
				JOptionPane.showMessageDialog(null, "You need to load a valid CIM file");
			} else {
				menuCal.setEnabled(true);
			}
		});

		menuCal.addActionListener((ActionEvent event) -> {
			JOptionPane.showMessageDialog(null, "S base value = 100 MVA");
			ComplexNumber[][] solution = execute(this.fileEQ, this.fileSSH);

			Object[][] data = new Object[solution.length][solution.length];
			String[] a = new String[solution.length];
			for (int i = 0; i < solution.length; i++) {
				a[i] = String.valueOf(i);
				for (int j = 0; j < solution[i].length; j++) {
					data[i][j] = solution[i][j].toString();
				}
			}
			JTable matrix = new JTable(data, a);
			// Make text bigger
			matrix.setRowHeight(50);
			matrix.setFont(new Font("Arial", Font.BOLD, 24));

			JFrame frame = new JFrame("Y matrix");
			frame.setPreferredSize(new Dimension(640, 400));
			frame.add(matrix);
			frame.pack();
			frame.setVisible(true);
		});

		file.add(menuEQ);
		file.add(menuSSH);
		file.add(menuCal);

		menubar.add(file);

		setJMenuBar(menubar);
	}

	private File selectFile() {
		File selectedFile = null;
		// Choose a file with a dialog
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		// Filter only XML files
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CIM files", "xml");
		fileChooser.setFileFilter(filter);
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			selectedFile = fileChooser.getSelectedFile();
		}
		return selectedFile;
	}

	private ComplexNumber[][] execute(File fileEQ, File fileSSH) {
		// Call to the XML parser
		ReadCIMXML reader = new ReadCIMXML(fileEQ, fileSSH);
		//Create the relational database
		SQLdatabase data = new SQLdatabase (reader.getBreakerList(), reader.getbVoltList(), reader.getSubList(),
				reader.getVoltLvlList(), reader.getSynMach(), reader.getGenUnit(), reader.getRegControl(),
				reader.getPowtrafo(), reader.getPowtrafoEnd(), reader.getEnergCons(), reader.getRatiotap());
		// Call to the topology processor
		TopologyProcessor tp = new TopologyProcessor(reader.getConnectivityNode(), reader.getTerminal(),
				reader.getBreakerList());
		// Y matrix calculation
		YMatrix Y = new YMatrix(100, tp.getsCNList(), reader.getLines(), reader.getTerminal(), reader.getbVoltList(),
				reader.getPowtrafo(), reader.getPowtrafoEnd());
		System.out.println("End");
		ComplexNumber[][] y = Y.getY();
		return y;

	}
}
