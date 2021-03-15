package com.pushmsg.pushsample.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@RestController
@EnableAutoConfiguration
public class PushMSGController {

	@Autowired
	protected ResourceLoader resourceLoader;

	@RequestMapping("/")
	String home() {
		test();
		return "Hello World!";
	}

	private final static String reqJson = "{\n" + "  \"message\":{\n"
			+ "    \"token\" : \"Android Studio上のLogcatコンソールに出てきたトークン" + "    \"notification\" : {\n"
			+ "      \"body\" : \"テストメッセージ\",\n" + "      \"title\" : \"テストタイトル\",\n" + "      }\n" + "   }\n" + "}";

	public void test() {
		System.out.println("start");

		try {
			// initialize firebasio
			initializeToken();

			// post param create
			URL url = new URL("https://fcm.googleapis.com/v1/projects/pushmsg-0520/messages:send");
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
			System.out.println("token" + getAccessToken());
			httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
			httpURLConnection.setDoOutput(true);

			httpURLConnection.connect();

			OutputStream out = httpURLConnection.getOutputStream();
			PrintStream ps = new PrintStream(out);
			ps.print(reqJson);
			ps.close();

			// return response output
			InputStream is = httpURLConnection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String s;
			while ((s = reader.readLine()) != null) {
				System.out.println(httpURLConnection.getResponseMessage() + httpURLConnection.getResponseCode());
			}
			reader.close();
			httpURLConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("end");
	}

	// initializeメソッドの実装
	public void initializeToken() throws IOException {

//	 		 File file = new File("~/serviceAccountKey.json");
//	 		 if (file.exists()){
//	 		     System.out.println("ファイルは存在します");
//	 		 }else{
//	 		     System.out.println("ファイルは存在しません");
//	 		 }

		Resource resource = resourceLoader.getResource("classpath:" + "serviceAccountKey.json");

		boolean bool = resource.exists();
		if (bool) {
			System.out.println("ファイルは存在します");
		} else {
			System.out.println("ファイルは存在しません");
		}

		// FileInputStream serviceAccount = new
		// FileInputStream("C:\\Users\\46120\\Downloads\\serviceAccountKey.json");
		InputStream serviceAccount = resource.getInputStream();

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

		FirebaseApp.initializeApp(options);
	}

	// アクセストークン取得メソッドの実装
	public String getAccessToken() throws IOException {
		GoogleCredential googleCredential = GoogleCredential
				.fromStream(new FileInputStream("C:\\Users\\46120\\Downloads\\serviceAccountKey.json"))
				.createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.messaging"));
		googleCredential.refreshToken();
		System.out.println(googleCredential.refreshToken());
		return googleCredential.getAccessToken();
	}

}
