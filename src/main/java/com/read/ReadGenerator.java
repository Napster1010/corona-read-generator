package com.read;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadGenerator {
	private static final SimpleDateFormat csvDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat excelDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	public static void main(String[] args) throws Exception{
		// initialize excel workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("data");
		final String columnHeaders[] = new String[] { "acct_id", "current_read_dttm", "reader_rem_cd", "kwh_reading",
				"kw_reading", "kva_reading", "kvah_reading", "lf_reading", "pf_reading", "mtr_reader_name",
				"assessed_units", "division", "group_no", "diary_no", "old_cons_no" };
		addColumnHeaders(sheet, columnHeaders);

		// Exception log file
		File exceptionFile = new File("C:\\\\Users\\\\Napster\\\\Documents\\\\Read generator files\\\\CoronaReadGenerator.txt");
		PrintWriter writer = new PrintWriter(exceptionFile);

		// read the csv file
		Path csvPath = Paths.get("C:\\Users\\Napster\\Documents\\Read generator files\\read_for_next_month.csv");
		Scanner scanner = new Scanner(csvPath);

		int currRowIndex = 1;
		scanner.nextLine();

		// counters
		int rowCount = 0, exceptionCount = 0;
		while (scanner.hasNextLine()) {
			++rowCount;
			String delimitedRead = scanner.nextLine();
			System.out.println(delimitedRead);
			final String[] readTokens = delimitedRead.split("\\|");
			System.out.println(Arrays.toString(readTokens));
			final HashMap<String, String> readMap = prepareMapFromTokens(readTokens);

			String[] readRow = null;
			try {
				readRow = prepareReadRow(readMap);
			} catch (Exception ex) {
				writer.println("Exception number: " + ++exceptionCount);
				writer.println("-----------------------------------------------");
				writer.println("Consumer Number: " + readMap.get("consumerNo"));
				writer.println("Error: " + ex.getMessage() + "\n");
				continue;
			}

			System.out.println(Arrays.toString(readRow));
			addReadEntry(sheet, readRow, currRowIndex++);
			System.out.println();
		}

		// write the workbook to a file
		FileOutputStream outputStream = new FileOutputStream(
				new File("C:\\Users\\Napster\\Documents\\Read generator files\\Test_Output.xlsx"));
		workbook.write(outputStream);
		System.out.println("\n\nExcel created Successfully!!");
		System.out.println("Number of rows found in the input file: " + rowCount);
		System.out.println("Number of exceptions caught: " + exceptionCount);
		
		writer.close();
		outputStream.close();
	}

	private static HashMap<String, String> prepareMapFromTokens(String[] readTokens) {
		HashMap<String, String> readMap = new HashMap<String, String>();
		readMap.put("consumerNo", readTokens[0]);
		readMap.put("billMonth", readTokens[1]);
		readMap.put("readDate", readTokens[2]);
		readMap.put("readType", readTokens[3]);
		readMap.put("reading", readTokens[4]);
		readMap.put("meterMd", (StringUtils.isEmpty(readTokens[5]) ? "0" : readTokens[5]));
		readMap.put("meterPf", (StringUtils.isEmpty(readTokens[6]) ? "0" : readTokens[6]));
		readMap.put("assessment", (StringUtils.isEmpty(readTokens[7]) ? "0" : readTokens[7]));
		readMap.put("totalConsumption", readTokens[8]);
		readMap.put("groupNo", readTokens[9]);
		readMap.put("readingDiaryNo", readTokens[10]);

		return readMap;
	}

	// add one month to read date
	private static String getNewReadDate(String readDate) throws Exception {
		Date parsedReadDate = csvDateFormat.parse(readDate);
		Date newReadDate = DateUtils.addMonths(parsedReadDate, 1);

		return excelDateFormat.format(newReadDate);
	}

	// get reading
	private static String getReading(String oldReading, String readType, String totalConsumption) throws Exception {
		BigDecimal newReading = null;
		BigDecimal reading = new BigDecimal(oldReading);
		BigDecimal consumption = new BigDecimal(totalConsumption);
		if ("NORMAL".equals(readType)) {
			newReading = reading.add(consumption);
		} else if ("ASSESSMENT".equals(readType)) {
			newReading = reading;
		} else if ("PFL".equals(readType)) {
			newReading = reading.add(new BigDecimal("190"));
		}

		return newReading.toString();
	}

	private static String[] prepareReadRow(HashMap<String, String> readMap) throws Exception {
		String readRow[] = new String[15];

		readRow[0] = readMap.get("consumerNo"); // consumer number
		readRow[1] = getNewReadDate(readMap.get("readDate")); // read date

		// read type
		String readType = readMap.get("readType");
		String newReadType = "";
		if ("NORMAL".equals(readType) || "PFL".equals(readType))
			newReadType = "NRML";
		else if ("ASSESSMENT".equals(readType))
			newReadType = "M_SD";
		else
			throw new Exception("Invalid Read Type!");

		readRow[2] = newReadType;

		// reading
		readRow[3] = getReading(readMap.get("reading"), readType, readMap.get("totalConsumption"));
		// meter_md
		readRow[4] = readMap.get("meterMd");
		// kva reading
		readRow[5] = "0";
		// kvah reading
		readRow[6] = "0";
		// lf reading
		readRow[7] = "0";
		// meter pf
		readRow[8] = readMap.get("meterPf");
		// meter reader name
		readRow[9] = "PMR_MANUAL";
		// assessment
		readRow[10] = readMap.get("assessment");
		// division
		readRow[11] = "";
		// group no
		readRow[12] = readMap.get("groupNo");
		// reading diary no
		readRow[13] = readMap.get("readingDiaryNo");
		// old cons no
		readRow[14] = "0";

		return readRow;
	}

	private static void addReadEntry(XSSFSheet sheet, String[] readTokens, int rowIndex) {
		int colIndex = 0;
		Row row = sheet.createRow(rowIndex);
		for (String val : readTokens) {
			Cell cell = row.createCell(colIndex++);
			cell.setCellValue(val);
		}
	}

	private static void addColumnHeaders(XSSFSheet sheet, String columnHeaders[]) {
		Row row = sheet.createRow(0);
		int colIndex = 0;
		for (String header : columnHeaders) {
			Cell headerCell = row.createCell(colIndex++);
			headerCell.setCellValue(header);
		}
	}

}
