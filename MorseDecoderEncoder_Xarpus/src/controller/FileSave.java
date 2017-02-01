package controller;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileSave {
	public static void logSave(String txt){
		try {
			PrintWriter out = new PrintWriter("morseDecoderLogs.txt");
			out.println(txt);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}}

