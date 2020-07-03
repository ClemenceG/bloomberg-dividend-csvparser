package com.heavenize.parsing.bloomberg;

import java.io.*;
import java.util.regex.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

public class Main {

	private static String directoryToExtract = "C:/Users/cgran/OneDrive/Documents/Projects/02-ReadBloomberg/Dividend History Per Share/";
	private static String exportPathName = "C:/Users/cgran/OneDrive/Documents/Projects/exportedTest/";
	private static String finalFilename = "Combined_Div_Info.csv";

	public static void main(String args[]) throws Exception {

		String dateFormat = "MM/dd/yyyy";

		File[] files = new File(directoryToExtract).listFiles();
		
		for(File file : files) {
			String line = extractInfoLine(file.getAbsolutePath());

			// divide line into 3 parts: security info, document info, dividend info
			String[] blocks = line.split(";", 5);

			
			
			if(blocks.length<=1) {
				Security security = extractSecurityTicker(blocks);
				PrintCSV.writeCSV(dateFormat, exportPathName, security);

			} else {
				Security security = extractSecurityInfo(blocks);
				ChunkInfo chunkInfo = extractChunkInfo(blocks);

				ArrayList<Chunk> chunks = splitIntoChunkList(chunkInfo);
				for(Chunk c : chunks) {
					security.addDividend(parseChunk(c));
				}
				security.setFrequency(security.getDividend(0).getFrequency());

				PrintCSV.writeCSV(dateFormat, exportPathName, security);
			}

		}
		PrintCSV.combineFiles(exportPathName, finalFilename);

	}

	// takes in the filename returns the line containing the info
	private static String extractInfoLine(String filename) {
		String line = null;
		try(FileReader fr = new FileReader(filename);
				BufferedReader br = new BufferedReader(fr);) {

			line = br.readLine();

			// initialize pattern and matcher for regEx comparison
			Pattern p2 = Pattern.compile("^(BBG00.[A-Z0-9]{6})");
			while(line != null) {
				/*Pattern p1 = Pattern.compile("DVD_[A-Z]{3-4}");
					Matcher m1 = p1.matcher(line);*/
				Matcher m2 = p2.matcher(line);
				if(m2.find()) break;		// if line matches leaves loop
				else {
					line = br.readLine();
				}
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}

		return line;
	}

	// method that split the line
	// if error, only contains the ticker
	private static Security extractSecurityTicker(String[] blocks) {
		String ticker = (blocks[0].split("\\|"))[0];
		Security security = new Security(ticker);
		return security;
	}
	// divide line into 3 parts: security info, document info, dividend info
	private static Security extractSecurityInfo(String[] blocks) {
		String ticker = (blocks[0].split("\\|"))[0];
		Security security = new Security(ticker, Integer.parseInt(blocks[2])); // assume the nb of dividends is preceded by the symbol for integers		
		return security;
	}

	private static ChunkInfo extractChunkInfo(String[] blocks) {
		int nbCategories = Integer.parseInt(blocks[3]);	// assume the nb of categories per div is preceded by the symbol for int
		String divInfo = blocks[4];
		ChunkInfo chunkInfo = new ChunkInfo(nbCategories, divInfo);
		return chunkInfo;

	}

	// method that divides the dividend information block into chunks by dividend
	private static ArrayList<Chunk> splitIntoChunkList(ChunkInfo chunkInfo) {
		int sizeChunk = chunkInfo.getNbCategories()*2;
		String[] blocks = chunkInfo.getInfo().split(";");

		int i = 0;
		ArrayList<Chunk> chunks = new ArrayList<Chunk>();
		Chunk chunk = new Chunk(sizeChunk);
		for(String s : blocks) {
			if(i == chunk.getSize()) {
				chunks.add(chunk);
				chunk = new Chunk(sizeChunk);
				i=0;
			}
			chunk.addValue(s);
			i++;
		}
		return chunks;
	}

	// method that parses one chunk into values for a dividend
	public static Dividend parseChunk(Chunk chunk) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Dividend div = new Dividend();
		// assume form Declared Date/ExDate/Record Date/Pay Date/Amount/Frequency/Type
		int placement = 1;
		for(int i=0; i<chunk.getSize();i+=2) {
			if(chunk.getValue(i+1).equals(" ")) {

			}else if(placement == 1) {
				if(chunk.getValue(i+1).equals(" ")) {
					div.setDeclaredDate(null);
				} else {
					Date date = format.parse(chunk.getValue(i+1)); // skip int that indicates type
					div.setDeclaredDate(date);
				}
			} else if(placement == 2)	{
				if(chunk.getValue(i+1).equals(" ")) {
					div.setExDate(null);
				} else {
					Date date = format.parse(chunk.getValue(i+1)); // skip int that indicates type
					div.setExDate(date);
				}
			} else if(placement == 3) {
				if(chunk.getValue(i+1).equals(" ")) {
					div.setRecordDate(null);
				} else {
					Date date = format.parse(chunk.getValue(i+1)); // skip int that indicates type
					div.setRecordDate(date);
				}
			} else if(placement == 4) {
				if(chunk.getValue(i+1).equals(" ")) {
					div.setPayDate(null);
				} else {
					Date date = format.parse(chunk.getValue(i+1)); // skip int that indicates type
					div.setPayDate(date);
				}
			} else if(placement == 5) {
				if(chunk.getValue(i+1).equals(" ") | chunk.getValue(i+1).equals("N.A.")) {
					div.setAmount(0);
				} else {
					double d = Double.parseDouble(chunk.getValue(i+1)); // skip int that indicates type
					div.setAmount(d);
				}
			} else if(placement == 6) {
				div.setFrequency(chunk.getValue(i+1)); // skip int that indicates type
			}
			else if(placement == 7) {
				div.setType(chunk.getValue(i+1)); // skip int that indicates type
			}

			placement++;
		}
		return div;
	}



}
