package com.pushmsg.pushsample;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Component
@Order(1)
public class FireBaseConfig  {
	 public static void initializeToken() throws IOException{

		FileInputStream serviceAccount =
		  new FileInputStream("serviceAccountKey.json");
		
		FirebaseOptions options = new FirebaseOptions.Builder()
		  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
		  .build();
		
		FirebaseApp.initializeApp(options);

	    }
}
