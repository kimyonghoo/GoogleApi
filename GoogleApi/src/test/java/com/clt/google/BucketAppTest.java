package com.clt.google;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.clt.google.storage.App;
import com.clt.google.storage.dto.BucketDto;
import com.clt.google.storage.dto.ObjectDto;
import com.clt.util.Utility;

import m2soft.ers.invoker.InvokerException;
import m2soft.ers.invoker.http.ReportingServerInvoker;

public class BucketAppTest {
	App app = new App();
	
	@Test
	@Ignore
	public void showBucketTest() throws IOException, GeneralSecurityException {
		BucketDto dto = new BucketDto();
    	dto.setName("one-bl-archive-bucket");
    	app.getBucketMetadata(dto);
    }
	
	@Test
	@Ignore
	public void createBucketTest() throws IOException, GeneralSecurityException {
		BucketDto dto = new BucketDto();
//    	dto.setName(Utility.getRandomString("abcdefghijklmnopqrstuvwxyz",7)+"-bucket");
		dto.setName("one-bl-archive-bucket");
    	Map<String, String> labels = new HashMap<String, String>();
    	labels.put("createdby", "yonghoo");
    	labels.put("reporttype", "bookingreceipt");
    	dto.setLabels(labels);
    	app.createBucket(dto);
    }
	
	@Test
	@Ignore
	public void addLabelTest () throws IOException, GeneralSecurityException {
		app.addBucketLabel("yonghoo2-bucket", "안녕", "하세요");
	}
	
	@Test
	@Ignore
	public void deleteBucketTest() throws IOException, GeneralSecurityException {
		BucketDto dto = new BucketDto();
		dto.setName("uabkjig-bucket");
		app.deleteBucket(dto);
	}
	
	@Test
	@Ignore
	public void showObjectListTest() throws IOException, GeneralSecurityException {
		BucketDto dto = new BucketDto();
		dto.setName("yonghoo-bucket");
		app.showObjectList(dto);
	}
	
	@Test
	@Ignore
	public void uploadTest () throws IOException, GeneralSecurityException {
		ObjectDto dto = new ObjectDto();
		Map<String, String> metadata = new HashMap<String, String>();
		metadata.put("생성자", "김용후");
		metadata.put("용도", "테스트");
		dto.setBucket("qbjmiug-bucket");
		dto.setName("hello.pdf");
		dto.setPath("P:\\Report\\2021012965422243.pdf");
		dto.setMetadata(metadata);
		app.uploadObject(dto);
	}
	
	@Test
	@Ignore
	public void downloadTest () throws IOException, GeneralSecurityException {
		ObjectDto dto = new ObjectDto();
		dto.setBucket("yonghoo-bucket");
		dto.setName("dd.pdf");
		dto.setPath("files/china_pdf.png");
		app.downloadObject(dto);
	}
	
	@Test
	@Ignore
	public void deleteTest () throws IOException, GeneralSecurityException {
		ObjectDto dto = new ObjectDto();
		dto.setBucket("yonghoo-bucket");
		dto.setName("worlds.pdf");
		app.deleteObject(dto);
	}
	
	@Test
	@Ignore
	public void reportUploadTest () throws IOException, GeneralSecurityException, InvokerException {
		ObjectDto dto = new ObjectDto();
		dto.setBucket("qbjmiug-bucket");
		for(int i=0; i<20; i++) {
			String filename = Utility.getRandomString("1234567890", 10)+".pdf";
			String path = "P:\\Report\\"+filename;
			
			ReportingServerInvoker invoker = new ReportingServerInvoker("http://localhost:8080/ReportingServer/service");
			invoker.setCharacterEncoding("utf-8"); //캐릭터셋
	
			invoker.addParameter("opcode", "500");
			invoker.addParameter("export_type", "pdf");
			invoker.addParameter("protocol", "file");
			invoker.addParameter("mrd_path", "sample.mrd");
			invoker.addParameter("mrd_param", "/rfn [sample.txt]");
			invoker.invoke(path);
			
			dto.setPath(path);
			app.uploadObject(dto);
		}
	}
	@Test
	@Ignore
	public void enableLifecycleManagementTest() throws IOException, GeneralSecurityException {
		app.enableLifecycleManagement("qbjmiug-bucket");
	}
}