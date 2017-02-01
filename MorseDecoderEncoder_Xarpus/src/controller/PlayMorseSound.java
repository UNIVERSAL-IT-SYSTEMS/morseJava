package controller;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class PlayMorseSound implements Runnable{
	private String output = null;
	private static float strenght, depth , speed;
	public static boolean stopSound = false;
	public static boolean playing = false;
	
	public PlayMorseSound(String output) {
		this.output = output;
	}

	private static synchronized void playSound(float strenght , float depth , float speed) {
		float sampleRate = 44100;
		int sampleSizeInBits = 8;
		
		    byte[] buf = new byte[ 1 ];
		   
			AudioFormat af = new AudioFormat( sampleRate,
		    		sampleSizeInBits,1, true, false );
		     SourceDataLine sdl;
			try {
			sdl = AudioSystem.getSourceDataLine( af );
		    sdl.open();
		    sdl.start();
		    for( int i = 0; i < sampleRate / speed; i++ ) {
		        double angle = i / ( sampleRate / 440 ) * depth * Math.pow(Math.PI, 2);
		        buf[ 0 ] = (byte )( Math.sin( angle ) * 50 * strenght * (1- Math.cos(2 * angle)));
		        sdl.write( buf, 0, 1 );   
		    }
		    sdl.drain();
		    sdl.stop();
	      } catch (Exception e) {
	        System.err.println(e.getMessage()); 
	      }}

	@Override
	public void run() {
		int i = 0;
		while (stopSound == false && i < output.length()){
		PlayMorseSound.playing = true;	
		if (output.charAt(i)== '.'){
			playSound(strenght , depth , speed * 12);
			try {
				Thread.sleep(175);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else if (output.charAt(i)== '-'){
			playSound(strenght , depth , speed * 6);
			try {
				Thread.sleep(175);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			try {
				Thread.sleep(1325);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		 }
			i++;
		}
		PlayMorseSound.playing = false;
	}
	
	public static void configureAudioSettings(float strenght , float depth , float speed){
		PlayMorseSound.strenght = strenght;
		PlayMorseSound.depth = depth;
		PlayMorseSound.speed = speed;
	}
}
