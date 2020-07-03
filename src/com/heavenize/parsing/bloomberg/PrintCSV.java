package com.heavenize.parsing.bloomberg;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class PrintCSV {


	public static void writeCSV(String dateFormat, String pathName, Security sec) {

		// create directory if doesn't exists if directory exists
		File directory = new File(pathName);
		if(!directory.exists()) {
			directory.mkdirs();
		}

		File file = new File(pathName+"exported_" + sec.getBloombergTicker() + ".csv");
		if(!file.exists()) {
			try(PrintWriter pw = new PrintWriter(file)) {

				// print header
				StringBuilder sb = new StringBuilder();

				sb.append("Bloomberg Ticker,Declared Date,Ex Date,Record Date,Pay Date,Amount,Frequency,Type");
				sb.append("\n");
				pw.write(sb.toString());

				// iterate through the dividends
				sb = new StringBuilder();
				if(sec.getDividends() == null) {
					sb.append(sec.getBloombergTicker());
					pw.write(sb.toString());
				} else {
					for(Dividend div : sec.getDividends()) {
						sb = new StringBuilder();
						sb.append(sec.getBloombergTicker() + ",");
						sb.append(dateToString(div.getDeclaredDate(), dateFormat) + ",");
						sb.append(dateToString(div.getExDate(), dateFormat) + ",");
						sb.append(dateToString(div.getRecordDate(), dateFormat) + ",");
						sb.append(dateToString(div.getPayDate(), dateFormat) + ",");
						sb.append(div.getAmount() + ",");
						sb.append(div.getFrequency() + ",");
						sb.append(div.getType() + "\n");

						pw.write(sb.toString());
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private static String dateToString(Date date, String format) {
		if(date == null) return "";
		DateFormat dateFormat = new SimpleDateFormat(format);
		String string = dateFormat.format(date);

		return string;
	}

	// method combines the extracted files in the directory
	public static void combineFiles(String pathName, String finalFilename) {
		File[] files = new File(pathName).listFiles();
		File finalFile = new File(pathName + finalFilename);
		try(PrintWriter pw = new PrintWriter(finalFile)){

			// print header once
			StringBuilder sb = new StringBuilder();

			sb.append("Bloomberg Ticker,Declared Date,Ex Date,Record Date,Pay Date,Amount,Frequency,Type");
			sb.append("\n");
			pw.write(sb.toString());

			for(File file : files) {
				try(FileReader fr = new FileReader(file);
						BufferedReader br = new BufferedReader(fr);) {

					String line = br.readLine();
					line = br.readLine();

					while(line != null) {
						pw.write(line+"\n");
						line = br.readLine();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


	}

}
 