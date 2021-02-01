package com.clt.google.storage;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

import com.clt.util.Utility;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;

/**
 * Google Cloud Storage Service
 *
 */
public class Service {
    private static GoogleCredentials getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		return GoogleCredentials.fromStream(new FileInputStream("I:\\Git\\GoogleStorageJavaMaven\\GoogleApi\\src\\main\\java\\com\\clt\\google\\storage\\credentials.json"))
			        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
    }

    public static Storage getStorage() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Storage storage = StorageOptions.newBuilder()
        		.setProjectId(Utility.getProperty("PROJECT_ID"))
				.setCredentials(getCredentials(HTTP_TRANSPORT))
				.build()
				.getService();
        return storage;
    }
}
