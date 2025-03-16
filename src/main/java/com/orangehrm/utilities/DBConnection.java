package com.orangehrm.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.orangehrm.base.BaseClass;

public class DBConnection {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/orangehrm";
	public static final String DB_USERNAME = "root";
	public static final String DB_PASSWORD = "";
	private static final Logger logger = BaseClass.logger;
	
	public static Connection getDBConnection() {
		try {
			logger.info("Starting DB Connection ...");
			Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			logger.info("DB Connection Successful.");
			return conn;
		} catch (SQLException e) {
			logger.error("Error While Estabishing DB Connection!");
			e.printStackTrace();
		}
		return null;
	}

	//Get Employee Details From DB And Store In Map
	public static Map<String, String> getEmployeeDetails(String employee_id) {
		String query = "SELECT `emp_firstname`, `emp_lastname`,`emp_middle_name` FROM hs_hr_employee WHERE`employee_id`="
				+ employee_id + ";";

		Map<String, String> employeeDetails = new HashMap<>();

		try (Connection conn = getDBConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			logger.info("Executing Query: " + query);
			if (rs.next()) {
				String firstName = rs.getString("emp_firstname");
				String middleName = rs.getString("emp_middle_name");
				String lastName = rs.getString("emp_lastname");

				// Store In Map
				employeeDetails.put("firstName", firstName);
				employeeDetails.put("middleName", middleName!=null?middleName:"");
				employeeDetails.put("lastName", lastName);

				logger.info("Query Executed Successfully.");
				logger.info("Employee Data Fetched.");
			} else {
				logger.error("Employee Not Found");
			}
		} catch (Exception e) {
			logger.error("Error While Executing Query!");
			e.printStackTrace();
		}
		return employeeDetails;

	}
}
