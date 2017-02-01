package controller;

import java.util.ArrayList;

import be.tarsos.dsp.SilenceDetector;

public class MorseProtocol implements Runnable{
	private long key_down_time = 0;
	private long key_down_length = 0;
	private static long key_up_time = 0;
	
	ArrayList<String> buffer = new ArrayList<String>();
	
	boolean FLAG;
	SilenceDetector silenceDetector;
	double threshold;
			
	String DOT = ".";
	String DASH = "-";	
	
	public MorseProtocol(boolean FLAG, SilenceDetector silenceDetector,
			double threshold){
		this.FLAG = FLAG;
		this. silenceDetector = silenceDetector;
		this.threshold = threshold;
	}
	
	void wait_for_keydown() throws InterruptedException{
	    while (silenceDetector.currentSPL() > threshold){
	    	Thread.sleep(1);
	        }
	}
	void wait_for_keyup() throws InterruptedException{
	    while (silenceDetector.currentSPL() < threshold){
	    	Thread.sleep(1);
	    }
	 }

	public static long getKey_up_time() {
		return key_up_time;
	}
		
	public ArrayList<String> getBuffer() {
		return buffer;
	}
	
	public void setFLAG(boolean fLAG) {
		FLAG = fLAG;
	}

	@Override
	public void run() {
		
	   while(FLAG){ 
		try {
		wait_for_keyup();
	    key_down_time = System.currentTimeMillis();
	    wait_for_keydown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    key_up_time = System.currentTimeMillis();
	    key_down_length = key_up_time - key_down_time;
	    
	    if(key_down_length > 300){
	    	buffer.add(DASH);
	    	System.out.println(DASH);
	    }
	    else{
	    	buffer.add(DOT);
	    	System.out.println(DOT);
	    	}
	    }//while
	}//run
}//end class

