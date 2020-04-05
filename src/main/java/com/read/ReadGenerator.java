package com.read;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.spi.ServiceException;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import com.config.CoronaConfig;

public class ReadGenerator {
	private static final String basePostgresUrl = "jdbc:postgresql://";
	private static final SimpleDateFormat billMonthFormat = new SimpleDateFormat("MMM-yyyy");
	private static final SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat excelDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private static final String lockdownDate = "2020-03-22";

	public static void main(String[] args) throws Exception{
		
		//Configurations for connection
		final Configuration configuration = new Configuration();
		final String connectionUrl = buildConnectionUrl();						
		configuration.setProperty("hibernate.connection.url", connectionUrl);
		configuration.setProperty("hibernate.connection.username", CoronaConfig.USERNAME);
		configuration.setProperty("hibernate.connection.password", CoronaConfig.PASSWORD);
		configuration.setProperty("hibernate.default_schema", CoronaConfig.DEFAULT_SCHEMA);
		
		System.out.println("Trying to establish a connection with the database...");
		//Connect to the DB
		SessionFactory sessionFactory = null;
		try {
			sessionFactory = configuration.configure().buildSessionFactory();			
		}catch(ServiceException ex) {
			System.out.println("Couldn't connect to the database! Please check the config parameters");
			System.exit(0);
		}catch(Exception ex) {
			System.out.println("Some unexpected error occurred! Terminating application..");
			System.exit(0);
		}
		System.out.println("Successfully connected to the Database !!");
		
		//Open session
		final Session session = sessionFactory.openSession();

		//Fetch required reading related information for <CURRENT_MONTH>
		List<String[]> currentReads = fetchCurrentReadInformation(session);
		
		// initialize excel workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("data");
		final String columnHeaders[] = new String[] { "acct_id", "current_read_dttm", "reader_rem_cd", "kwh_reading",
				"kw_reading", "kva_reading", "kvah_reading", "lf_reading", "pf_reading", "mtr_reader_name",
				"assessed_units", "division", "group_no", "diary_no", "old_cons_no" };
		addColumnHeaders(sheet, columnHeaders);

		// Exception log file
		File exceptionFile = new File(CoronaConfig.EXCEPTION_FILE_PATH);
		PrintWriter writer = new PrintWriter(exceptionFile);

		int currRowIndex = 1;

		// counters
		int rowCount = 0, exceptionCount = 0;
		for(String[] readTokens: currentReads) {
			++rowCount;
			System.out.println(Arrays.toString(readTokens));
			final HashMap<String, String> readMap = prepareMapFromTokens(readTokens);

			Object[] readRow = null;
			try {
				//retrieve the tariff information
				String tariffCategory = getTariffCategory(session, readMap.get("consumerNo"));
				if(tariffCategory==null)
					throw new Exception("Couldn't retrieve tariff category for the consumer!");
				System.out.println(tariffCategory);
				
				//prepare the new read row
				readRow = prepareReadRow(readMap, tariffCategory, session);
			} catch (Exception ex) {
				writer.println("Exception number: " + ++exceptionCount);
				writer.println("-----------------------------------------------");
				writer.println("Consumer Number: " + readMap.get("consumerNo"));
				writer.println("Error: ");
				ex.printStackTrace(writer);
				continue;
			}

			System.out.println(Arrays.toString(readRow));
			addReadEntry(sheet, readRow, currRowIndex++);
			System.out.println();
		}

		// write the workbook to a file
		FileOutputStream outputStream = new FileOutputStream(
				new File(CoronaConfig.OUTPUT_PMR_FILE_PATH));
		workbook.write(outputStream);
		System.out.println("\n\nExcel created Successfully!!");
		System.out.println("Number of rows found in the input file: " + rowCount);
		System.out.println("Number of exceptions caught: " + exceptionCount);
		
		writer.close();
		outputStream.close();
	}
	
	//Build the connection URL
	private static String buildConnectionUrl() {
		String connectionUrl = basePostgresUrl
				.concat(CoronaConfig.HOST)
				.concat(":")
				.concat(CoronaConfig.PORT)
				.concat("/")
				.concat(CoronaConfig.DATABASE_NAME);						
				
		return connectionUrl;
	}
	
	//Fetch read information for a bill month
	private static List<String[]> fetchCurrentReadInformation(Session session) {
		
		String queryString = "select rm.consumerNo, rm.billMonth, rm.readingDate, rm.readingType, rm.reading"
				+ ", rmk.meterMD, rmp.meterPF, rm.assessment, (rm.totalConsumption/rm.mf) as totalConsumption"
				+ ", rm.groupNo, rm.readingDiaryNo"
				+ " from ReadMaster rm"
				+ " left join ReadMasterKW rmk on rm.id = rmk.readMasterId"
				+ " left join ReadMasterPF rmp on rm.id = rmp.readMasterId"
				+ " where rm.billMonth = :billMonth and rm.usedOnBill=true and rm.replacementFlag='NR'"
				+ " and rm.groupNo in (:groups)";
		
		Query<Object[]> query = session.createQuery(queryString, Object[].class);
		query.setParameter("billMonth", CoronaConfig.CURRENT_BILL_MONTH);
		query.setParameterList("groups", getGroups());
		
		List<Object[]> resultList = query.getResultList();
		List<String[]> reads = new ArrayList<>();  		
		for(Object[] read: resultList) {
			reads.add(Arrays.stream(read)
					.map(val -> val==null ? null : val.toString())
					.toArray(String[]::new));
		}
		
		return reads;		
	}
	
	//Return a string which has comma delimited groups
	private static List<String> getGroups() {
		StringTokenizer tokenizer = new StringTokenizer(CoronaConfig.GROUP_NOS, ",");
		List<String> groups = new ArrayList<>();
		
		while(tokenizer.hasMoreTokens()) {
			String group = tokenizer.nextToken().trim();
			groups.add(group);
		}
		return groups;
	}	
	
	//Retrieve tariff category for a consumer
	private static String getTariffCategory(Session session, String consumerNo) {
		String queryString = "select tariffCategory from ConsumerConnectionInformation where consumerNo = :consumerNo";
		Query<String> query = session.createQuery(queryString, String.class);
		query.setParameter("consumerNo", consumerNo);
		String tariffCategory = query.uniqueResult();		
		return tariffCategory;
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
		Date parsedReadDate = dbDateFormat.parse(readDate);
		Date newReadDate = DateUtils.addMonths(parsedReadDate, 1);

		return excelDateFormat.format(newReadDate);
	}

	// get reading and assessment
	private static Object[] getReadingAndAssessment(String oldReading, String readType,
			String totalConsumption, String tariffCategory, String consumerNo, String oldAssessment,
			Session session, Date currentReadingDate) throws Exception {
		
		BigDecimal newReading = null;
		BigDecimal newAssessment = null;
		boolean prorateValues = false;
		
		//check if there's a need to prorate reading, assessment for IP
		if(tariffCategory.equals("LV2") || tariffCategory.equals("LV4")) {
			Date newReadingDate = excelDateFormat.parse(getNewReadDate(dbDateFormat.format(currentReadingDate)));
			if(dbDateFormat.parse(lockdownDate).compareTo(newReadingDate) > 0) {
				System.out.println("Lockdown Date: " + dbDateFormat.parse(lockdownDate));
				System.out.println("Next month Date: " + newReadingDate);
				prorateValues = true;
			}
		}
		
		if(tariffCategory.equals("LV1") || tariffCategory.equals("LV3") || tariffCategory.equals("LV5") || !prorateValues) {
			BigDecimal reading = new BigDecimal(oldReading);
			BigDecimal consumption = new BigDecimal(totalConsumption);
			if ("NORMAL".equals(readType)) {
				newReading = reading.add(consumption);
				newAssessment = BigDecimal.ZERO;
			} else if ("ASSESSMENT".equals(readType)) {
				newReading = reading;
				newAssessment = new BigDecimal(oldAssessment);
			} else if ("PFL".equals(readType)) {
				newReading = reading.add(new BigDecimal("190"));
				newAssessment = BigDecimal.ZERO;
			}			
		}else {
			//Retrieve the previous bill month
			String prevBillMonth = getPreviousBillMonth(CoronaConfig.CURRENT_BILL_MONTH);
			System.out.println("Previous billMonth: " + prevBillMonth);
			
			//Retrieve the previous reading date
			Date prevReadingDate = getPreviousReadingDate(session, consumerNo, prevBillMonth);
			System.out.println("Previous readingDate: " + prevReadingDate);
			
			//by default the duration should be 30 days
			int noOfDays = 30;
			
			if(prevReadingDate!=null) {
				noOfDays = differenceInDays(prevReadingDate, currentReadingDate);
			}
			System.out.println("Number of days: " + noOfDays);

			//handle according to read type
			if ("NORMAL".equals(readType)) {
				newReading = calculateNewReading(oldReading, totalConsumption, noOfDays, currentReadingDate);
				newAssessment = BigDecimal.ZERO;
			} else if ("ASSESSMENT".equals(readType)) {
				newReading = new BigDecimal(oldReading);
				newAssessment = calculateNewAssessment(oldAssessment, noOfDays, currentReadingDate);
			}									
		}		

		return new Object[] {newReading.toString(), Double.parseDouble(newAssessment.toString())};
	}
	
	//get difference between days
	private static int differenceInDays(Date d1, Date d2) {
		return Days.daysBetween(new LocalDate(d1.getTime()), 
				new LocalDate(d2.getTime())).getDays();		
	}
	
	//get previous bill month in format MMM-yyyy
	private static String getPreviousBillMonth(String billMonth) throws Exception {
		Date billMonthDate = billMonthFormat.parse(billMonth);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(billMonthDate);
		calendar.add(Calendar.MONTH, -1);
		String previousBillMonth = billMonthFormat.format(calendar.getTime()).toUpperCase();
		return previousBillMonth;
	}
		
	//calculates new reading
	private static BigDecimal calculateNewReading(String oldReading, String totalConsumption, int noOfDays,
			Date currentReadingDate) throws Exception {
		BigDecimal reading = new BigDecimal(oldReading);
		BigDecimal consumption = new BigDecimal(totalConsumption);		
		//calculate per day consumption
		BigDecimal perDayConsumption = consumption.divide(new BigDecimal(noOfDays), 4, RoundingMode.HALF_UP);
		//calculate active days
		int activeDays = differenceInDays(currentReadingDate, dbDateFormat.parse(lockdownDate));
		//calculate new consumption
		BigDecimal newConsumption = perDayConsumption.multiply(new BigDecimal(activeDays)).setScale(4, RoundingMode.HALF_UP);
		
		//add the consumption to reading
		BigDecimal newReading = reading.add(newConsumption);
		return newReading;
	}
	
	//calculates new assessment	
	private static BigDecimal calculateNewAssessment(String oldAssessment, int noOfDays, 
			Date currentReadingDate) throws Exception {
		BigDecimal assessment = new BigDecimal(oldAssessment);
		//calculate per day assessment
		BigDecimal perDayAssessment = assessment.divide(new BigDecimal(noOfDays), 4, RoundingMode.HALF_UP);
		//calculate active days
		int activeDays = differenceInDays(currentReadingDate, dbDateFormat.parse(lockdownDate));
		//calculate new assessment
		BigDecimal newAssessment = perDayAssessment.multiply(new BigDecimal(activeDays));
		return newAssessment;
	}
	
	//get previous reading date. return null if not found
	private static Date getPreviousReadingDate(Session session, String consumerNo, 
			String prevBillMonth) throws Exception {
		
		String queryString = "select readingDate from ReadMaster where billMonth = :billMonth"
				+ " and consumerNo = :consumerNo and usedOnBill=true and replacementFlag='NR'";
		Query<Date> query = session.createQuery(queryString, Date.class);
		query.setParameter("billMonth", prevBillMonth);
		query.setParameter("consumerNo", consumerNo);
		Date previousReadingDate = query.uniqueResult();		
		
		return previousReadingDate;
	}

	//Prepare the new read for the consumer
	private static Object[] prepareReadRow(HashMap<String, String> readMap, String tariffCategory, 
			Session session) throws Exception {
		Object readRow[] = new Object[15];

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
		
		Object[] readingAndAssessment = getReadingAndAssessment(readMap.get("reading"), readType, 
				readMap.get("totalConsumption"), tariffCategory, readMap.get("consumerNo"), readMap.get("assessment"),
				session, dbDateFormat.parse(readMap.get("readDate"))); 

		// reading
		readRow[3] = readingAndAssessment[0];
		// meter_md
		readRow[4] = formatDouble(Double.parseDouble(readMap.get("meterMd")));
		// kva reading
		readRow[5] = "0";
		// kvah reading
		readRow[6] = "0";
		// lf reading
		readRow[7] = "0";
		// meter pf
		readRow[8] = formatDouble(Double.parseDouble(readMap.get("meterPf")));
		// meter reader name
		readRow[9] = "PMR_MANUAL";
		// assessment
		readRow[10] = formatDouble((Double)readingAndAssessment[1]);
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
	
	//format double to 4 decimal places and return the double
	private static Double formatDouble(Double val) {
		return Double.parseDouble(new DecimalFormat("#0.0000").format(val));
	}

	private static void addReadEntry(XSSFSheet sheet, Object[] readTokens, int rowIndex) {
		int colIndex = 0;
		Row row = sheet.createRow(rowIndex);
		for (Object val : readTokens) {
			Cell cell = row.createCell(colIndex++);
			if(val instanceof String)
				cell.setCellValue((String)val);
			else if(val instanceof Double)
				cell.setCellValue((Double)val);
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
