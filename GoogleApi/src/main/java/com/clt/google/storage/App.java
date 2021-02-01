package com.clt.google.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.clt.google.storage.dto.BucketDto;
import com.clt.google.storage.dto.ObjectDto;
import com.clt.util.Utility;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.BucketInfo.LifecycleRule;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageClass;
import com.google.common.collect.ImmutableList;

public class App {
	Logger logger = Logger.getLogger(App.class.getName());

	public void getBucketMetadata(BucketDto dto) throws IOException, GeneralSecurityException {
		Storage storage = Service.getStorage();
		Bucket bucket = storage.get(dto.getName(), Storage.BucketGetOption.fields(Storage.BucketField.values()));

		logger.debug("BucketName: " + bucket.getName());
		logger.debug("DefaultEventBasedHold: " + bucket.getDefaultEventBasedHold());
		logger.debug("DefaultKmsKeyName: " + bucket.getDefaultKmsKeyName());
		logger.debug("Id: " + bucket.getGeneratedId());
		logger.debug("IndexPage: " + bucket.getIndexPage());
		logger.debug("Location: " + bucket.getLocation());
		logger.debug("LocationType: " + bucket.getLocationType());
		logger.debug("Metageneration: " + bucket.getMetageneration());
		logger.debug("NotFoundPage: " + bucket.getNotFoundPage());
		logger.debug("RetentionEffectiveTime: " + bucket.getRetentionEffectiveTime());
		logger.debug("RetentionPeriod: " + bucket.getRetentionPeriod());
		logger.debug("RetentionPolicyIsLocked: " + bucket.retentionPolicyIsLocked());
		logger.debug("RequesterPays: " + bucket.requesterPays());
		logger.debug("SelfLink: " + bucket.getSelfLink());
		logger.debug("StorageClass: " + bucket.getStorageClass().name());
		logger.debug("TimeCreated: " + Utility.getDateFromTimestamp(bucket.getCreateTime()));
		logger.debug("VersioningEnabled: " + bucket.versioningEnabled());
		if (bucket.getLabels() != null) {
			logger.debug("\n\n\nLabels:");
			for (Map.Entry<String, String> label : bucket.getLabels().entrySet()) {
				logger.debug(label.getKey() + "=" + label.getValue());
			}
		}
		if (bucket.getLifecycleRules() != null) {
			logger.debug("\n\n\nLifecycle Rules:");
			for (BucketInfo.LifecycleRule rule : bucket.getLifecycleRules()) {
				logger.debug(rule);
			}
		}
	}

	public void showBucketList() throws IOException, GeneralSecurityException {
		Storage storage = Service.getStorage();
		Page<Bucket> buckets = storage.list();

		for (Bucket bucket : buckets.iterateAll()) {
			logger.info(bucket.getName());
		}
	}

	public void createBucket(BucketDto dto) throws IOException, GeneralSecurityException {
		Storage storage = Service.getStorage();
		StorageClass storageClass = StorageClass.STANDARD;
		Map<String, String> labels = dto.getLabels() != null ? dto.getLabels() : null;
		try {
			logger.debug("Try to make bucket '" + dto.getName() + "'");
			@SuppressWarnings("unused")
			Bucket bucket = storage.create(BucketInfo.newBuilder(dto.getName()).setStorageClass(storageClass)
					.setLabels(labels).setLocation(Utility.getProperty("LOCATION")).build());

			logger.debug(">> Created bucket name '" + dto.getName() + "'");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public void deleteBucket(BucketDto dto) throws IOException, GeneralSecurityException {
		Storage storage = Service.getStorage();
		Bucket bucket = storage.get(dto.getName());
		bucket.delete();
		logger.debug("Bucket " + bucket.getName() + " was deleted");
	}

	public void addBucketLabel(String bucketName, String labelKey, String labelValue) throws IOException, GeneralSecurityException {
		Map<String, String> labelsToAdd = new HashMap<>();
		labelsToAdd.put(labelKey, labelValue);

		Storage storage = Service.getStorage();
		Bucket bucket = storage.get(bucketName);
		Map<String, String> labels = bucket.getLabels();
		if (labels != null) {
			labelsToAdd.putAll(labels);
		}
		bucket.toBuilder().setLabels(labelsToAdd).build().update();

		logger.debug(">> Added label '" + labelKey + "' with value '" + labelValue + "' to bucket '" + bucketName + "'.");
	}

	public void showObjectList(BucketDto dto) throws IOException, GeneralSecurityException {
		Storage storage = Service.getStorage();
		Bucket bucket = storage.get(dto.getName());
		Page<Blob> blobs = bucket.list();

		for (Blob blob : blobs.iterateAll()) {
			logger.info(blob.getName());
		}
	}

	public void uploadObject(ObjectDto dto) throws IOException, GeneralSecurityException {
		Storage storage = Service.getStorage();
		File file = new File(dto.getPath());
		String name = dto.getName() != null ? dto.getName() : file.getName();
		Map<String, String> newMetadata = dto.getMetadata() != null ? dto.getMetadata() : null;
		try {
			BlobId blobId = BlobId.of(dto.getBucket(), name);
			BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
					.setMetadata(newMetadata)
					.build();
			logger.debug(">> Origin file: " + file.getAbsolutePath());
			storage.create(blobInfo, Files.readAllBytes(Paths.get(dto.getPath())));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		logger.debug(">> " + dto.getPath() + " uploaded to bucket " + dto.getBucket() + " as " + name);
	}

	public void downloadObject(ObjectDto dto) throws IOException, GeneralSecurityException {
		Storage storage = Service.getStorage();
		Blob blob = storage.get(BlobId.of(dto.getBucket(), dto.getName()));
		blob.downloadTo(Paths.get(dto.getPath()));
		logger.debug(">> Downloaded object " + dto.getName() + " from bucket name " + dto.getBucket() + " to "
				+ dto.getPath());
	}

	public void deleteObject(ObjectDto dto) throws IOException, GeneralSecurityException {
		Storage storage = Service.getStorage();
		storage.delete(dto.getBucket(), dto.getName());
		logger.debug(">> Object " + dto.getName() + " was deleted from " + dto.getBucket());
	}
	
	public void enableLifecycleManagement(String bucketName) throws IOException, GeneralSecurityException {
	    Storage storage = Service.getStorage();
	    Bucket bucket = storage.get(bucketName);
	    bucket
	        .toBuilder()
	        .setLifecycleRules(
	            ImmutableList.of(
	                new LifecycleRule(
	                    LifecycleRule.LifecycleAction.newDeleteAction(),
	                    LifecycleRule.LifecycleCondition.newBuilder().setAge(10).build())))
	        .build()
	        .update();

	    logger.debug(">> Lifecycle management was enabled and configured for bucket " + bucketName);
	  }

}
