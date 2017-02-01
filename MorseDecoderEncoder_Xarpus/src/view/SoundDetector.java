package view;
/*
*      _______                       _____   _____ _____  
*     |__   __|                     |  __ \ / ____|  __ \ 
*        | | __ _ _ __ ___  ___  ___| |  | | (___ | |__) |
*        | |/ _` | '__/ __|/ _ \/ __| |  | |\___ \|  ___/ 
*        | | (_| | |  \__ \ (_) \__ \ |__| |____) | |     
*        |_|\__,_|_|  |___/\___/|___/_____/|_____/|_|     
*                                                         
* -------------------------------------------------------------
*
* TarsosDSP is developed by Joren Six at IPEM, University Ghent
*  
* -------------------------------------------------------------
*
*  Info: http://0110.be/tag/TarsosDSP
*  Github: https://github.com/JorenSix/TarsosDSP
*  Releases: http://0110.be/releases/TarsosDSP/
*  
*  TarsosDSP includes modified source code by various authors,
*  for credits and info, see README.
* 
*/
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.SilenceDetector;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import controller.MorseDecoderThread;
import controller.MorseProtocol;
import controller.Shared;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SoundDetector extends JFrame implements AudioProcessor {
	private static Thread t;
	private static SoundDetector instance; //SINGLETON
	public static SoundDetector getInstance() {
	    if (instance == null) {
	    		t = new Thread(new Runnable() {
				@Override
				public void run() {
				try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (Exception e) {
						//ignore failure to set default look en feel;
					}
					instance = new SoundDetector();
					instance.pack();
					instance.setSize(640,480);
					instance.setVisible(true);
				}
			});
			t.start();
	    }
	    return instance;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 3501426880288136245L;

	private final JTextArea textArea;
	ArrayList<Clip> clipList;
	int counter;
	double threshold;
	AudioDispatcher dispatcher;
	Mixer currentMixer;
	private final GaphPanel graphPanel;
	SilenceDetector silenceDetector;
	
	Thread oldLetterThread;
	Thread oldWordThread;
	MorseProtocol oldLetterGeneratorObj;
	MorseDecoderThread oldWordGeneratorObj;
	private JTextField textField_Morse;
	
	private SoundDetector() {
		getContentPane().setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle("Morse decoder");
		this.threshold = SilenceDetector.DEFAULT_SILENCE_THRESHOLD;
		
		
		JPanel inputPanel = new InputPanel();
		//add(inputPanel);
		inputPanel.addPropertyChangeListener("mixer",
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						try {
							setNewMixer((Mixer) arg0.getNewValue());
						} catch (LineUnavailableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UnsupportedAudioFileException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		
				
		JSlider thresholdSlider = initialzeThresholdSlider();		
		JPanel params = new JPanel(new BorderLayout());
		params.setBorder(new TitledBorder("2. Set the algorithm parameters"));
		
		JLabel label = new JLabel("Threshold");
		label.setToolTipText("Energy level when sound is counted (dB SPL).");
		params.add(label,BorderLayout.NORTH);
		params.add(thresholdSlider,BorderLayout.CENTER);
		
		JPanel inputAndParamsPanel = new JPanel(new BorderLayout());
		inputAndParamsPanel.add(inputPanel,BorderLayout.NORTH);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainWindow.setLog(textField_Morse.getText());
				setVisible(false);
				if (currentMixer != null)
					currentMixer.close();
	    		instance = null;
	    		dispose();
	    		
			}
		});
		inputPanel.add(btnClose, BorderLayout.EAST);
		
		JButton btnOcistiIspisDekodera = new JButton("Clean decoded text");
		btnOcistiIspisDekodera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textField_Morse.setText("");
			}
		});
		inputPanel.add(btnOcistiIspisDekodera, BorderLayout.SOUTH);
		inputAndParamsPanel.add(params,BorderLayout.SOUTH);

		
		JPanel panelWithTextArea = new JPanel(new BorderLayout());
		textArea = new JTextArea(8,30);
		textArea.setEditable(false);
		panelWithTextArea.add(inputAndParamsPanel,BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane(textArea);
		panelWithTextArea.add(scrollPane,BorderLayout.CENTER);
		
		textField_Morse = new JTextField();
		scrollPane.setColumnHeaderView(textField_Morse);
		textField_Morse.setColumns(10);
		
		getContentPane().add(panelWithTextArea,BorderLayout.NORTH);
		
		new Thread( new Runnable() {
			@Override
			public void run() {
				while(instance!=null)	{
				if (MorseDecoderThread.getDecodedChar()!=null){
				textField_Morse.setText(textField_Morse.getText() + 
				MorseDecoderThread.getDecodedChar().toString());
				MorseDecoderThread.resetChar();
				}

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}
			}}).start();

		graphPanel = new GaphPanel(threshold);
		graphPanel.setSize(80,100);
		getContentPane().add(graphPanel,BorderLayout.CENTER);

	}
	
	private static class GaphPanel extends JPanel{

		/**
		 * 
		 */
		private static final long serialVersionUID = 5969781241442094359L;
		private double threshold;
		private double maxLevel = -1000;
		private long currentModulo = System.currentTimeMillis()/15000;
		private List<Double> levels;
		private List<Long> startTimes;
		
		public GaphPanel(double defaultThreshold){			
			setThresholdLevel(defaultThreshold);
			levels = new ArrayList<Double>();
			startTimes=new ArrayList<Long>();
			setMinimumSize(new Dimension(80,60)); 
		}
		
		private void setMaxLevel(double newMaxLevel){
			if(newMaxLevel> maxLevel){
				maxLevel=newMaxLevel;
			}
		}
		
		public void setThresholdLevel(double newThreshold){
			threshold=newThreshold;
			repaint();
		}
		
		public void addDataPoint(double level,long ms){
			levels.add(level);
			startTimes.add(ms);
			setMaxLevel(level);
			repaint();
		}
		
		public void paintComponent(Graphics g) {
	        super.paintComponent(g); //paint background
	        g.setColor(Color.BLACK);
			g.fillRect(0, 0,getWidth(), getHeight());
			
			if(System.currentTimeMillis()/15000 > currentModulo){
				currentModulo = System.currentTimeMillis()/15000;
				levels.clear();
				startTimes.clear();
			}
			
	
			for(int i =0 ; i < levels.size();i++){
				g.setColor( levels.get(i) > threshold ? Color.GREEN:Color.ORANGE ); 
				int x = msToXCoordinate(startTimes.get(i));
				int y = levelToYCoordinate(levels.get(i));
				g.drawLine(x, y, x+1, y);
			}
			
			int thresholdYCoordinate = levelToYCoordinate(threshold);
			g.setColor(Color.ORANGE);
			g.drawLine(0, thresholdYCoordinate, getWidth(),thresholdYCoordinate);
			g.drawString(String.valueOf((int)threshold), 0, thresholdYCoordinate + 15);
			
			
			int maxYCoordinate = levelToYCoordinate(maxLevel);
			g.setColor(Color.RED);
			g.drawLine(0, maxYCoordinate, getWidth(),maxYCoordinate);
			g.drawString(String.valueOf(((int)(maxLevel*100))/100.0), getWidth() - 40, maxYCoordinate + 15);
			
	    }
		
		private int levelToYCoordinate(double level){
			int inPixels = (int)((120 + level)  / 120 * (getHeight()-1));
			int yCoordinate =  getHeight() - inPixels;
			return yCoordinate;
		}
		
		private int msToXCoordinate(long ms){
			return (int) ((ms % 15000)/15000.0 * getWidth());
		}
				
	}
	
	private JSlider initialzeThresholdSlider() {
		JSlider thresholdSlider = new JSlider(-120,0);
		thresholdSlider.setValue((int)threshold);
		thresholdSlider.setPaintLabels(true);
		thresholdSlider.setPaintTicks(true);
		thresholdSlider.setMajorTickSpacing(20);
		thresholdSlider.setMinorTickSpacing(10);
		thresholdSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				threshold = source.getValue();
				graphPanel.setThresholdLevel(threshold);
			    if (!source.getValueIsAdjusting()) {			        
			        try {
						setNewMixer(currentMixer);
						
			        	if (oldWordThread == null){}
			        	else if(oldWordThread.isAlive()==true){
			        		oldWordGeneratorObj.setFLAG(false);
			        		oldLetterGeneratorObj.setFLAG(false);
			        	}
						MorseProtocol letterGenerator = new MorseProtocol(rootPaneCheckingEnabled, 
								silenceDetector, threshold);
						Thread letterThread = new Thread(letterGenerator);
						letterThread.start();
						MorseDecoderThread wordGenerator = new MorseDecoderThread(letterGenerator.getKey_up_time(), 
								letterGenerator.getBuffer() , rootPaneCheckingEnabled);
						Thread wordThread = new Thread(wordGenerator);
						wordThread.start();
						
					} catch (LineUnavailableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (UnsupportedAudioFileException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    }
			}
		});
		return thresholdSlider;
	}

	private void setNewMixer(Mixer mixer) throws LineUnavailableException,
			UnsupportedAudioFileException {
		
		if(dispatcher!= null){
			dispatcher.stop();
		}
		currentMixer = mixer;
		
		float sampleRate = 44100;
		int bufferSize = 512;
		int overlap = 0;
		
		textArea.append("Started listening with " + Shared.toLocalString(mixer.getMixerInfo().getName()) + "\n\tparams: " + threshold + "dB\n");

		final AudioFormat format = new AudioFormat(sampleRate, 16, 1, true,
				true);
		final DataLine.Info dataLineInfo = new DataLine.Info(
				TargetDataLine.class, format);
		TargetDataLine line;
		line = (TargetDataLine) mixer.getLine(dataLineInfo);
		final int numberOfSamples = bufferSize;
		line.open(format, numberOfSamples);
		line.start();
		final AudioInputStream stream = new AudioInputStream(line);

		JVMAudioInputStream audioStream = new JVMAudioInputStream(stream);
		// create a new dispatcher
		dispatcher = new AudioDispatcher(audioStream, bufferSize,
				overlap);

		// add a processor, handle percussion event.
		silenceDetector = new SilenceDetector(threshold,false);
		dispatcher.addAudioProcessor(silenceDetector);
		dispatcher.addAudioProcessor(this);

		// run the dispatcher (on a new thread).
		new Thread(dispatcher,"Audio dispatching").start();
	}

	@Override
	public boolean process(AudioEvent audioEvent) {
		handleSound();
		return true;
	}

	@SuppressWarnings("null")
	private void handleSound(){
		
		if(silenceDetector.currentSPL() > threshold){
			textArea.append("Sound detected at:" + System.currentTimeMillis() + ", " + (int)(silenceDetector.currentSPL()) + "dB SPL\n");
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
		graphPanel.addDataPoint(silenceDetector.currentSPL(), System.currentTimeMillis());		
	}
	@Override
	public void processingFinished() {		
		
	}
}