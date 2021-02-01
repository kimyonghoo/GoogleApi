package com.clt.postgres.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.clt.google.storage.App;

public class DBConnector {

	Logger logger = Logger.getLogger(DBConnector.class.getName());
	private final String conUrl = "jdbc:postgresql://localhost/GOOGLEDRIVE";
	private final String username = "postgres";
	private final String password = "1234";

	public Connection dbcon() {
		Connection dbcon = null;

		try {
			dbcon = DriverManager.getConnection(conUrl, username, password);
			logger.debug("-Connected to the database");
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}

		return dbcon;
	}
}