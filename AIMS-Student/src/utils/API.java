package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

import entity.payment.CreditCard;
import entity.payment.PaymentTransaction;



/**
 * @author ADMIN
 *
 */
public class API {
	// date format
	public static DateFormat DATE_FORMATER = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	
	private static Logger LOGGER = Utils.getLogger(Utils.class.getName());
	

	/**
	 * @param url
	 * @param method
	 * @param token
	 * @return
	 * @throws IOException
	 */
	private static HttpURLConnection setupConnection(String url, String method, String token) throws IOException{
		
		LOGGER.info("Request URL: " + url + "\n");
		URL line_api_url = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) line_api_url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("GET"); 
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", "Bearer " + token);
		return conn;
	}

	
	
	/**
	 * @param connection
	 * @return
	 * @throws IOException
	 */
	private static String readResponse(HttpURLConnection connection) throws IOException{
		BufferedReader in;
		String inputLine;
		
		if (connection.getResponseCode() / 100 == 2) {
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} else {
			in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
		}
		
		StringBuilder response = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		in.close();
		LOGGER.info("Respone Info: " + response.toString());
		return response.toString();

	}
	
	
	/**
	 * @param url
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static String get(String url, String token) throws Exception {
	
		HttpURLConnection conn = setupConnection(url,"GET",token);

      
		String response = readResponse(conn);

		return response;
	}

	
	/**
	 * @param url
	 * @param data
	 * @param token
	 * @return
	 * @throws IOException
	 */
	public static String post(String url, String data, String token) throws IOException {
		
		allowMethods("PATCH");
		
        
		HttpURLConnection conn = setupConnection(url,"POST",token);

       
		Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		writer.write(data);
		writer.close();

        
		String response = readResponse(conn);
		return response.toString();
	}

	
	/**
	 * @param methods
	 */
	private static void allowMethods(String... methods) {
		try {
			
			Field methodsField = HttpURLConnection.class.getDeclaredField("methods");
			methodsField.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

			String[] oldMethods = (String[]) methodsField.get(null);
			Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
			methodsSet.addAll(Arrays.asList(methods));
			String[] newMethods = methodsSet.toArray(new String[0]);

			methodsField.set(null/* static field */, newMethods);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

}