package com.clt.google.drive.sql;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.clt.google.drive.App;
import com.clt.google.drive.dto.FileDto;
import com.clt.postgres.jdbc.DBConnector;

public class MetaDataQuery {
	Logger logger = Logger.getLogger(MetaDataQuery.class.getName());
	DBConnector connector = new DBConnector();

	public String insertMeta(FileDto dto) throws IOException, GeneralSecurityException {
		App googleDrive = new App();
		String SQLinsert = "INSERT INTO FILES(key,name,parent,size,type,description,pkey) " + "VALUES(?,?,?,?,?,?,?)";
		String key = "";

		try (PreparedStatement prepareStatement = connector.dbcon().prepareStatement(SQLinsert,
				Statement.RETURN_GENERATED_KEYS)) {
			prepareStatement.setString(1, dto.getKey());
			prepareStatement.setString(2, dto.getName());
			prepareStatement.setString(3, dto.getParent());
			prepareStatement.setLong(4, dto.getSize());
			prepareStatement.setString(5, dto.getMimeType());
			prepareStatement.setString(6, dto.getDescription());
			prepareStatement.setString(7, dto.getParentKey());

			int rowsAffected = prepareStatement.executeUpdate();

			if (rowsAffected > 0) {
				try (ResultSet rs = prepareStatement.getGeneratedKeys()) {
					if (rs.next()) {
						key = rs.getString(1);
					}
				} catch (SQLException ex) {
					logger.error(ex.getMessage());
				}
			}

		} catch (SQLException ex) {
			logger.error(ex.getMessage());
			googleDrive.delete(dto.getKey());
		}
		googleDrive.printList();
		return key;	
	}
	
	public String deletemeta(String key) {
		String SQLdelete = "DELETE FROM FILES WHERE KEY = ?";

		try (PreparedStatement prepareStatement = connector.dbcon().prepareStatement(SQLdelete)) {
			prepareStatement.setString(1, key);

			int rowsAffected = prepareStatement.executeUpdate();

			if (rowsAffected > 0) {
				try (ResultSet rs = prepareStatement.getGeneratedKeys()) {
					if (rs.next()) {
						key = rs.getString(1);
					}
				} catch (SQLException ex) {
					logger.error(ex.getMessage());
				}
			}

		} catch (SQLException ex) {
			logger.error(ex.getMessage());
		}
		logger.debug("-Meta Deleted");
		return key;
	}
	
	public String selectKeyByName(String name) {
		String SQLdelete = "SELECT * FROM FILES WHERE NAME = ?";
		String key = "";
		
		try (PreparedStatement prepareStatement = connector.dbcon().prepareStatement(SQLdelete)) {
			prepareStatement.setString(1, name);

			ResultSet result = prepareStatement.executeQuery();
			result.next();
			key = result.getString("key");

		} catch (SQLException ex) {
			logger.error(ex.getMessage());
		}
		return key;
	} 
	
	public String selectNameByKey(String key) {
		String SQLdelete = "SELECT * FROM FILES WHERE KEY = ?";
		String name = "";
		
		try (PreparedStatement prepareStatement = connector.dbcon().prepareStatement(SQLdelete)) {
			prepareStatement.setString(1, key);

			ResultSet result = prepareStatement.executeQuery();
			result.next();
			name = result.getString("name");

		} catch (SQLException ex) {
			logger.error(ex.getMessage());
		}
		return name;
	} 
}
