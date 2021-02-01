package com.clt.google.drive;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.clt.google.drive.dto.FileDto;
import com.clt.google.drive.sql.MetaDataQuery;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class App {
	Logger logger = Logger.getLogger(App.class.getName());
	static MetaDataQuery query = new MetaDataQuery();
	
    public String upload(FileDto dto) throws IOException, GeneralSecurityException{
		List<String> plist = new ArrayList<String>();
		
		File fileMetadata = new File();
		fileMetadata.setName(dto.getName());
		if(dto.getParent() != null) {
			String parentKey = query.selectKeyByName(dto.getParent());
			dto.setParentKey(parentKey);
			plist.add(dto.getParentKey());
			fileMetadata.setParents(plist);
		}
		if(dto.getDescription() != null) {
			fileMetadata.setDescription(dto.getDescription());
		}
		java.io.File filePath = new java.io.File(dto.getFilePath());
		FileContent mediaContent = new FileContent(dto.getMimeType(), filePath);
		File file = Service.getDrive().files().create(fileMetadata, mediaContent)
			.setFields("*")
			.execute();
		
		logger.debug("file uploaded !!!");
		dto.setMimeType(file.getMimeType());
		dto.setKey(file.getId());
		dto.setSize(file.getSize());
		dto.setCreDt(file.getCreatedTime().toString());
		dto.setDescription(file.getDescription());
		
		return query.insertMeta(dto);
	}
	public void download(String key) throws IOException, GeneralSecurityException {
		java.io.File file = new java.io.File(query.selectNameByKey(key));
		FileOutputStream outputStream = new FileOutputStream(file);
		Service.getDrive().files().get(key)
		    .executeMediaAndDownloadTo(outputStream);
		logger.debug("File downloaded into "+ file.getPath());
	}
	public void delete(String key) throws IOException, GeneralSecurityException {
		Service.getDrive().files().delete(key).execute();
		logger.debug("File deleted !!!");
		query.deletemeta(key);
	}
	public void printList() {
        // Print the names and IDs for up to 10 files.
        FileList result = null;
		try {
			result = Service.getDrive().files().list().setPageSize(10).setFields("nextPageToken, files(id, name, parents)").execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
        	logger.debug("<<No files found.>>");
        } else {
        	logger.info("<<GoogleDrive file list>>");
            for (File file : files) {
            	logger.info(file.getName() + "" + file.getId() + "" + file.getParents());
            }
        }
	}
	public String createFolder(FileDto dto) throws IOException, GeneralSecurityException {
		List<String> plist = new ArrayList();
		
		File fileMetadata = new File();
		fileMetadata.setName(dto.getName());
		fileMetadata.setMimeType("application/vnd.google-apps.folder");
		if(dto.getParent() != null) {
			String parentKey = query.selectKeyByName(dto.getParent());
			dto.setParentKey(parentKey);
			plist.add(parentKey);
			fileMetadata.setParents(plist);
		}

		File file = Service.getDrive().files().create(fileMetadata)
		    .setFields("*")
		    .execute();
		
		logger.debug("folder created !!!");
		dto.setMimeType(file.getMimeType());
		dto.setKey(file.getId());
		dto.setSize((long) 0);
		dto.setCreDt(file.getCreatedTime().toString());
		
		return query.insertMeta(dto);
	}
	public String getFolder(String name) {
		String key = "";
		
		return key;
	}
}