package com.heavenize.parsing.bloomberg;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.lang.Iterable;

public class PrintCSV {
	
	private static String csvDivider = ",";
	
	public static void writeCSV(String dateFormat, String pathName, String finalFilename, Iterable<Security> securities) {

		// create directory if doesn't exists if directory exists
		File directory = new File(pathName);
		if(!directory.exists()) {
			directory.mkdirs();
		}

		File file = new File(pathName + finalFilename);
		try(PrintWriter pw = new PrintWriter(file)){

			// print header once
			StringBuilder sb = new StringBuilder();
			
			sb.append("Bloomberg Ticker,Declared Date,Ex Date,Record Date,Pay Date,Amount,Frequency,Type");
			pw.println(sb.toString());

			for(Security sec : securities) {
				// iterate through the dividends
				sb = new StringBuilder();
				if(sec.getDividends() == null) {
					sb.append(sec.getBloombergTicker());
					pw.println(sb.toString());
				} else {
					for(Dividend div : sec.getDividends()) {
						sb = new StringBuilder();
						sb.append(sec.getBloombergTicker());
						sb.append(csvDivider);
						sb.append(dateToString(div.getDeclaredDate(), dateFormat));
						sb.append(csvDivider);
						sb.append(dateToString(div.getExDate(), dateFormat));
						sb.append(csvDivider);
						sb.append(dateToString(div.getRecordDate(), dateFormat));
						sb.append(csvDivider);
						sb.append(dateToString(div.getPayDate(), dateFormat));
						sb.append(csvDivider);
						sb.append(div.getAmount());
						sb.append(csvDivider);
						sb.append(div.getFrequency());
						sb.append(csvDivider);
						sb.append(div.getType());

						pw.println(sb.toString());
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			}
	}

	private static String dateToString(Date date, String format) {
		if(date == null) {
			return "";
		}
		DateFormat dateFormat = new SimpleDateFormat(format);
		String string = dateFormat.format(date);

		return string;
	}


}
 