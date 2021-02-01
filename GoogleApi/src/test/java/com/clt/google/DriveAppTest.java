package com.clt.google;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import org.junit.Ignore;
import org.junit.Test;

import com.clt.google.drive.App;
import com.clt.google.drive.dto.FileDto;
import com.clt.util.Utility;

public class DriveAppTest {
	App app = new App();
	
	@Test
	public void printList() {
		app.printList();
    }
	@Ignore
	@Test
	public void insertFile() throws IOException, GeneralSecurityException {
		FileDto dto = new FileDto();
		dto.setFilePath("files/photo.jpg");
		File file = new File(dto.getFilePath());
		dto.setName("photo.jpg");
		dto.setFilePath(file.getPath());
		dto.setMimeType(Utility.getMimeType(file.getPath()));
		dto.setDescription("#yonghoo#"+file.getName());
		app.upload(dto);
	}
	@Ignore
	@Test
	public void insertFiles() throws IOException, GeneralSecurityException {
		FileDto dto = new FileDto();
		File dir = new File("files");
		File files[] = dir.listFiles();

		for(File file : files) {
			dto.setName(file.getName());
			dto.setFilePath(file.getPath());
			dto.setMimeType(Utility.getMimeType(file.getPath()));
	//		dto.setParent("1UmGxX-Gl-dnXaSGt2UK3oy8lhxE4HSD8");
			dto.setDescription("#yonghoo#"+file.getName());
//			app.upload(dto);
		}
	}
	
	@Ignore
	@Test
	public void createFolder() throws IOException, GeneralSecurityException {
		FileDto dto = new FileDto();
		dto.setName("789");
		app.createFolder(dto);
	}
	@Ignore
	@Test
	public void download() throws IOException, GeneralSecurityException {
		app.download("1XcbyYRoSwyDox0MCmSHkFEiA2rtY-XDR");
	}
	@Ignore
	@Test
	public void delete() throws IOException, GeneralSecurityException {
		app.delete("1XcbyYRoSwyDox0MCmSHkFEiA2rtY-XDR");
	}
}