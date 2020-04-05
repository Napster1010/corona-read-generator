package com.config;

public class CoronaConfig {
	
	// ******************************* Database config parameters **********************************//
	
	//Host IP address where the database resides
	public static final String HOST = "172.17.0.91";	
	//Port on which the Postgres database is running	
	public static final String PORT = "5432";	
	//Name of the database
	public static final String DATABASE_NAME = "ngb";	
	//User name for connecting to DB
	public static final String USERNAME = "aktiwari";	
	//Password for connecting to DB
	public static final String PASSWORD = "aktiwari";	
	//Schema name 
	public static final String DEFAULT_SCHEMA = "ngb";
	
	// ******************************* Inputs for the Program *************************************//

	//Current month (strictly in format MMM-yyyy)
	public static final String CURRENT_BILL_MONTH = "FEB-2020";	
	//Previous month (strictly in format MMM-yyyy) 
	public static final String PREVIOUS_BILL_MONTH = "JAN-2020";	
	//List of groups (Should be comma delimited. ex: GJK3,GJK4,GJK5)
	public static final String GROUP_NOS = "GJK3,GJK4";
	//Name of the exception log file
	public static final String EXCEPTION_FILE_NAME = "CoronaReadGeneratorExceptions";
	//Name of the output PMR file
	public static final String OUTPUT_PMR_FILE_NAME = "Test_Output";
}
