package com.orangehrm.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoggerManager {

	//This Method Return Logger Instance Of Class
	public static Logger getLogger(Class<?> clazz) {
		return LogManager.getLogger();
	}
}
