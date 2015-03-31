package com.ibm.cloudoe.samples;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.util.EntityUtils;

import com.google.gson.*;

public class ConvertServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(DemoServlet.class.getName());
	private static final long serialVersionUID = 1L;

	// If running locally complete the variables below with the information in VCAP_SERVICES
	private String baseURL = "<url>"; //api 
	private String fileName = "risk-analysis.json";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		File file = new File(fileName);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		JsonParser parser1 = new JsonParser();
		JsonObject jsonObject1 = parser1.parse(reader.readLine()).getAsJsonObject();
		JsonArray options = jsonObject1.get("options").getAsJsonArray();
		
		URL callAPI = new URL(baseURL + ); // add from req.
		BufferedReader br = new BufferedReader(new InputStreamReader(callAPI.openStream()));
		String str = br.readLine();
		JsonParser parser2 = new JsonParser();
		JsonObject jsonObject2 = parser2.parse(str).getAsJsonObject();
		JsonObject newOption = new JsonObject();
		newOption.addProperty("key", options.size() + 1);
		newOption.addProperty("name", "whatever"); // add from req.
		newOption.add("values", jsonObject2);
		newOption.addProperty("description_html", "");
		newOption.add("app_data", new JsonObject());
		options.add(newOption);
		
		File output = new File("tradeoff.json");
		BufferedWriter out = new BufferedWriter(new FileWriter(output));
		out.write(jsonObject1.toString());
		
		out.close();
		reader.close();
		br.close();
	}

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
