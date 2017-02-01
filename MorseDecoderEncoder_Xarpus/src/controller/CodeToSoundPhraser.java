package controller;
import model.MorseCodeClass;

public class CodeToSoundPhraser {
	public static int numOfLoops;
	
	public static String codeToASCII(String input){
		StringBuilder sb = new StringBuilder(input);
		for (int i = 0 ; i < input.length();i++){
			if (sb.charAt(i)=='�'){
				sb.setCharAt(i, 'D');
			}
			else if (sb.charAt(i)=='�'){
				sb.setCharAt(i, 'C');
			}
			else if (sb.charAt(i)=='�'){
				sb.setCharAt(i, 'C');
			}
			else if (sb.charAt(i)=='�'){
				sb.setCharAt(i, 'S');
			}
			else if (sb.charAt(i)=='�'){
				sb.setCharAt(i, 'Z');
			} 
		}
		
		return sb.toString();
	}
	
	public static String coder(String input , MorseCodeClass objMorseCode){
		input = codeToASCII(input);
		String output = "";
		String charDictionary = "";
		int loopsInMethod = 0;
		while (loopsInMethod < numOfLoops){
		for (int i=0; i<input.length();i++){
			if(Character.isWhitespace(input.charAt(i))){
				output = output + " ";
			}
			else{
		    charDictionary = MorseCodeClass.dictionary.get(input.charAt(i));
			output = output + charDictionary + " ";
			}
		}
		loopsInMethod++;
		
	}
		if (PlayMorseSound.playing != true){
		(new Thread(new PlayMorseSound(output))).start();
		}

		return output;}

}
