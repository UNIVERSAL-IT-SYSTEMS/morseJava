package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;

import controller.CodeToSoundPhraser;
import controller.FileSave;
import controller.PlayMorseSound;
import model.MorseCodeClass;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class MainWindow {

	private JFrame frmMorseCoder;
	private JTextField textFieldMorse;
	private JTextField textFieldReview;
	private static String log;
	/**
	 * Launch the application.
	 */
	public static void setLog(String input){
		if (input!=null)
		log = log + "\nMessage from decoder:\n" + input;
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmMorseCoder.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */	
	private String getCurretDate(){
		return LocalDateTime.now().toString();
	}
	
	
	private void initialize() {
		MorseCodeClass objMorseCode = new MorseCodeClass();	
		log = "Log started:" + getCurretDate() + "\n";
		frmMorseCoder = new JFrame();
		frmMorseCoder.setTitle("Morse Encoder");
		frmMorseCoder.setBounds(100, 100, 672, 424);
		frmMorseCoder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMorseCoder.getContentPane().setLayout(null);
		
		textFieldMorse = new JTextField();
		textFieldMorse.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldMorse.setBounds(10, 54, 297, 133);
		frmMorseCoder.getContentPane().add(textFieldMorse);
		textFieldMorse.setColumns(10);
		
		textFieldReview = new JTextField();
		textFieldReview.setBounds(10, 220, 297, 141);
		frmMorseCoder.getContentPane().add(textFieldReview);
		textFieldReview.setColumns(10);
		
		JLabel lblProvjera = new JLabel("Morse output");
		lblProvjera.setBounds(10, 198, 108, 14);
		frmMorseCoder.getContentPane().add(lblProvjera);
		
		JLabel lblAlfanumerickiTekst = new JLabel("Insert alfanumerical characters below:");
		lblAlfanumerickiTekst.setBounds(10, 29, 245, 14);
		frmMorseCoder.getContentPane().add(lblAlfanumerickiTekst);
		
		JLabel lblPodesavanjeZvuka = new JLabel("Output tone settings");
		lblPodesavanjeZvuka.setBounds(421, 29, 154, 14);
		frmMorseCoder.getContentPane().add(lblPodesavanjeZvuka);
		
		JLabel lblStrenght = new JLabel("Sinewave");
		lblStrenght.setBounds(317, 65, 76, 14);
		frmMorseCoder.getContentPane().add(lblStrenght);
		
		JLabel lblDepth = new JLabel("Frequency:");
		lblDepth.setBounds(317, 133, 81, 14);
		frmMorseCoder.getContentPane().add(lblDepth);
		
		JLabel lblSpeed = new JLabel("Speed:");
		lblSpeed.setBounds(439, 133, 40, 14);
		frmMorseCoder.getContentPane().add(lblSpeed);
		
		JSpinner spinnerNumOfLoops = new JSpinner();
		spinnerNumOfLoops.setModel(new SpinnerNumberModel(new Integer(1), new Integer(0), null, new Integer(1)));
		spinnerNumOfLoops.setBounds(600, 130, 46, 20);
		frmMorseCoder.getContentPane().add(spinnerNumOfLoops);
		
		JLabel lblPonavljanje = new JLabel("Repeats:");
		lblPonavljanje.setBounds(543, 133, 53, 14);
		frmMorseCoder.getContentPane().add(lblPonavljanje);
		
		JButton btnAbout = new JButton("About");
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("About");
				
				JOptionPane.showMessageDialog(frame, "Written by Josip Duvnjak and Goran Glavocevic"
						+ "\n https://github.com/Xarpus/morseJava 2017. \n");
				
			}
		});
		btnAbout.setBounds(527, 351, 119, 23);
		frmMorseCoder.getContentPane().add(btnAbout);
		
		JSlider sliderStrenght = new JSlider();
		sliderStrenght.setMinimum(1);
		sliderStrenght.setValue(1);
		sliderStrenght.setMaximum(10);
		sliderStrenght.setPaintTicks(true);
		sliderStrenght.setPaintLabels(true);
		sliderStrenght.setMajorTickSpacing(1);
		sliderStrenght.setBounds(375, 54, 200, 53);
		frmMorseCoder.getContentPane().add(sliderStrenght);
		
		JSpinner spinnerDepth = new JSpinner();
		spinnerDepth.setModel(new SpinnerNumberModel(new Float(3), new Float(1), new Float(70), new Float(1)));
		spinnerDepth.setBounds(389, 130, 40, 20);
		frmMorseCoder.getContentPane().add(spinnerDepth);
		
		JSpinner spinnerSpeed = new JSpinner();
		spinnerSpeed.setModel(new SpinnerNumberModel(new Float(1), new Float(1), new Float(10), new Float(1)));
		spinnerSpeed.setBounds(485, 130, 48, 20);
		frmMorseCoder.getContentPane().add(spinnerSpeed);
		JButton btnPokreni = new JButton("Start");
		
		btnPokreni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PlayMorseSound.stopSound = false;
				float strenght = sliderStrenght.getValue();
				float depth = (float)spinnerDepth.getValue();
				float speed = (float)spinnerSpeed.getValue() / (float)1.8;
				int numOfLoop = (int) spinnerNumOfLoops.getValue();
				log = log + "New user input:" + getCurretDate() + "\n" + 
				"Settings:" + strenght + "|" + depth + "|" + speed + "|" + numOfLoop + "\n";
				
				log = log + "MorseInput:"+ textFieldMorse.getText()+ "\n" ;
				CodeToSoundPhraser.numOfLoops = numOfLoop;
				PlayMorseSound.configureAudioSettings(strenght, depth, speed);
				textFieldReview.setText(CodeToSoundPhraser.coder(textFieldMorse.getText().toUpperCase(), objMorseCode));
				log = log + "MorseOutput:"+ textFieldReview.getText()+ "\n\r";
			}
		});
		btnPokreni.setBounds(412, 198, 89, 23);
		frmMorseCoder.getContentPane().add(btnPokreni);
		
		JButton btnSpremiLog = new JButton("Save log");
		btnSpremiLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileSave.logSave(log);
				JFrame frame = new JFrame("Spremanje");
				JOptionPane.showMessageDialog(frame, "Saved!");
			}
		});
		btnSpremiLog.setBounds(317, 351, 89, 23);
		frmMorseCoder.getContentPane().add(btnSpremiLog);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PlayMorseSound.stopSound = true;
			}
		});
		btnStop.setBounds(412, 232, 89, 23);
		frmMorseCoder.getContentPane().add(btnStop);
		
		JButton btnDekoder = new JButton("Decoder");
		btnDekoder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SoundDetector.getInstance();
			}
		});
		btnDekoder.setBounds(412, 279, 89, 23);
		frmMorseCoder.getContentPane().add(btnDekoder);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(515, 65, 81, 14);
		frmMorseCoder.getContentPane().add(lblNewLabel);
		
		JLabel lblSquarewave = new JLabel("Squarewave");
		lblSquarewave.setBounds(583, 63, 73, 14);
		frmMorseCoder.getContentPane().add(lblSquarewave);

	}
}
