package controller;

import java.util.ArrayList;

import model.MorseCodeClass;

public class MorseDecoderThread implements Runnable{
	private ArrayList<String> buffer;
	private long key_up_time;
	private boolean FLAG;	  
	private static Character decoded_char;
	private MorseCodeClass novo = new MorseCodeClass();
	
	public MorseDecoderThread(long key_up_time, ArrayList<String> buffer, boolean FLAG){
		this.key_up_time = key_up_time;
		this.buffer = buffer;
		this.FLAG = FLAG;
	}	
	
	public void setFLAG(boolean fLAG) {
		FLAG = fLAG;
	}
	public static Character getDecodedChar(){
		return decoded_char;
	}
	public static void resetChar(){
		 decoded_char= null;
	}
	
	@Override
	public void run() {
		    boolean new_word = false;
		    String bit_string = "";
		    while (FLAG){
		        try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        key_up_time = MorseProtocol.getKey_up_time();
		        long key_up_length = System.currentTimeMillis() - key_up_time;
		        
		        if (buffer.size() > 0 && key_up_length >= 1.5){
		            new_word = true;
		            bit_string = bit_string + String.join("", buffer);        
		            buffer.clear();
		            }
		        else if (new_word && key_up_length > 625){
		            new_word = false;
		            decoded_char = MorseCodeClass.getKeyByValue(bit_string);
		            System.out.println(decoded_char);
		            bit_string = "";
		            }
		    }
	}
}
