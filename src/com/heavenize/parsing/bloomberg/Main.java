package com.heavenize.parsing.bloomberg;

import java.io.*;
import java.util.regex.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.LinkedList;

public class Main {

	private static String directoryToExtract = "C:/Users/cgran/OneDrive/Documents/Projects/02-ReadBloomberg/Dividend History Per Share/";
	private static String exportPathName = "C:/Users/cgran/OneDrive/Documents/Projects/tester1/";
	private static String finalFilename = "Combined_Div_Info.csv";
	
	private static String csvDivider = ";";
	private static String tickerRegex = "^(BBG00.[A-Z0-9]{6})";

	public static void main(String args[]) throws Exception {

		String dateFormat = "MM/dd/yyyy";

		File[] files = new File(directoryToExtract).listFiles();
		List<Security> securities = new LinkedList<Security>();
		
		for(File file : files) {
			String line = extractInfoLine(file.getAbsolutePath());
			Security security = parseSecurity(line);
			securities.add(security);
		}
		PrintCSV.writeCSV(dateFormat, exportPathName, finalFilename, securities);

	}

	// takes in the filename returns the line containing the info
	private static String extractInfoLine(String filename) {
		String line = null;
		try(FileReader fr = new FileReader(filename);
				BufferedReader br = new BufferedReader(fr);) {

			line = br.readLine();

			// initialize pattern and matcher for regEx comparison
			Pattern p2 = Pattern.compile(tickerRegex);
			while(line != null) {
				Matcher m2 = p2.matcher(line);
				if(m2.find()) {
					break;		// if line matches leaves loop
				} else {
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
	// from the line of information obtains the security for a file
	private static Security parseSecurity(String line) throws Exception {
		String[] blocks = line.split(";", 5);

		Security security = null;

		if (blocks.length != 0) {
			String ticker = (blocks[0].split("\\|"))[0];
			if(blocks.length<=1) { 		// if check if file only contains bloomberg ticker
				security = new Security(ticker);
				
			} else {
				security = new Security(ticker, Integer.parseInt(blocks[2])); // assume the nb of dividends is preceded by the symbol for integers		
				ChunkInfo chunkInfo = extractChunkInfo(blocks);

				List<Chunk> chunks = splitIntoChunkList(chunkInfo);
				for(Chunk currentChunk : chunks) {
					security.addDividend(parseChunk(currentChunk));
				}
				security.setFrequency(security.getDividend(0).getFrequency());

			}
		}
		return security;
	}
	
	
	private static ChunkInfo extractChunkInfo(String[] blocks) {
		int nbCategories = Integer.parseInt(blocks[3]);	// assume the nb of categories per div is preceded by the symbol for int
		String divInfo = blocks[4];
		ChunkInfo chunkInfo = new ChunkInfo(nbCategories, divInfo);
		return chunkInfo;

	}

	// method that divides the dividend information block into chunks by dividend
	private static LinkedList<Chunk> splitIntoChunkList(ChunkInfo chunkInfo) {
		int sizeChunk = chunkInfo.getNbCategories()*2;
		String[] blocks = chunkInfo.getInfo().split(csvDivider);

		int i = 0;
		LinkedList<Chunk> chunks = new LinkedList<Chunk>();
		Chunk chunk = new Chunk(sizeChunk);
		for(String currentString : blocks) {
			if(i/sizeChunk == 1) {				// when all elements added to the chunk add to chunklist
				chunks.add(chunk);
				chunk = new Chunk(sizeChunk);
				i=0;
			}
			chunk.addValue(currentString);
			i++;
		}
		return chunks;
	}

	/**
	 * method that parses one chunk into values for a dividend
	 * @param chunk
	 * @return
	 * @throws Exception
	 */
	public static Dividend parseChunk(Chunk chunk) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Dividend div = new Dividend();
		// assume form Declared Date/ExDate/Record Date/Pay Date/Amount/Frequency/Type
		int placement = 1;
		for(int i=0; i<chunk.getSize();i+=2) {
			switch(placement) {
			case 1:
				if(!chunk.getValue(i+1).equals(" ")) {
					Date date = format.parse(chunk.getValue(i+1)); // skip int that indicates type
					div.setDeclaredDate(date);
				}
				break;
			case 2:
				if(!chunk.getValue(i+1).equals(" ")) {
					Date date = format.parse(chunk.getValue(i+1)); // skip int that indicates type
					div.setExDate(date);
				}
				break;
			case 3:
				if(!chunk.getValue(i+1).equals(" ")) {
					Date date = format.parse(chunk.getValue(i+1)); // skip int that indicates type
					div.setRecordDate(date);
				}
				break;
			case 4: 
				if(!chunk.getValue(i+1).equals(" ")) {
					Date date = format.parse(chunk.getValue(i+1)); // skip int that indicates type
					div.setPayDate(date);
				}
				break;
			case 5:
				if(chunk.getValue(i+1).equals(" ") | chunk.getValue(i+1).equals("N.A.")) {
					div.setAmount(0);
				} else {
					double d = Double.parseDouble(chunk.getValue(i+1)); // skip int that indicates type
					div.setAmount(d);
				}
				break;
			case 6:
				div.setFrequency(chunk.getValue(i+1)); // skip int that indicates type
				break;
			case 7: 
				div.setType(chunk.getValue(i+1)); // skip int that indicates type
				break;
			}
			placement++;
		}
		return div;
	}



}
