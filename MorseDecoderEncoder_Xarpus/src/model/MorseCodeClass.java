package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class MorseCodeClass {

	static String[] morseCodeSymbols = new String[] {
			// NULLS don't exist in morse code
			"-.-.--", 						// !
			".-..-.", 						// "
			null, 							// #
			"...-..-", 						// $
			null, 							// %

			".-...", 						// &
			".----.", 						// '

			"-.--.", 						// (
			"-.--.-", 						// )
			null, 							// *

			".-.-.", 						// +
			"--..-- ", 						// ,
			"-....-", 						// -
			".-.-.-", 						// .
			"-..-.", 						// /

			"-----", 						// 0
			".----", 						// 1
			"..---", 						// 2
			"...--", 						// 3
			"....-",					 	// 4
			".....", 						// 5
			"-....", 						// 6
			"--...", 						// 7
			"---..",					    // 8
			"----.", 						// 9

			"---...",					    // :
			"-.-.-.", 						// ;

			null, 							// <

			"-...-", 						// =

			null, 							// >

			"..--..", 						// ?

			".--.-.", 						// @

			".-", "-...", "-.-.", "-..",    // A-D
			".", "..-.", "--.", "....",     // E-H
			"..", ".---", "-.-", ".-..",    // I-L
			"--", "-.", 					// M-N
			"---", ".--.", "--.-", ".-.",	// O-R
			"...", "-", "..-", "...-",		// S-V
			".--", "-..-", "-.--", "--.."	// W-Z

	};

	public static Map<Character, String> dictionary = new HashMap<Character, String>();
	{
		for (char ch = '!'; ch <= 'Z'; ++ch)
			dictionary.put((ch), morseCodeSymbols[ch - '!']);

	}
	
	public static <T, E> Character getKeyByValue(E value) {
	    for (Entry<Character, String> entry : dictionary.entrySet()) {
	        if (Objects.equals(value, entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
}
