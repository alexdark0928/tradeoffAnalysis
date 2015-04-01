package com.ibm.cloudoe.samples;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class ConvertServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(ConvertServlet.class.getName());
	private static final long serialVersionUID = 1L;

	// If running locally complete the variables below with the information in VCAP_SERVICES
	private String baseURL = "http://riskadvisor.mybluemix.net/api/category/"; //api
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String source = req.getParameter("source");
		JSONObject jsonObject1 = JSONObject.parse(source);
		JSONArray options = (JSONArray)jsonObject1.get("options");
		
		String cpName = req.getParameter("companyName");
		String cpYear = req.getParameter("year");
		URL callAPI = new URL(baseURL + cpName + "?year=" + cpYear ); // add from req.
		BufferedReader br = new BufferedReader(new InputStreamReader(callAPI.openStream()));
		String str = br.readLine();
		JSONObject jsonObject2 = JSONObject.parse(str);
		JSONObject newOption = new JSONObject();
		JSONObject values = new JSONObject();
		values.put("Funding risks", jsonObject2.get("Funding risks"));
		values.put("Competition risks", jsonObject2.get("Competition risks"));
		values.put("Downstream risks", jsonObject2.get("Downstream risks"));
		values.put("Macroeconomic risks", jsonObject2.get("Macroeconomic risks"));
		values.put("Input prices risks", jsonObject2.get("Input prices risks"));
		values.put("Suppliers risks", jsonObject2.get("Suppliers risks"));
		values.put("New product introduction risks", jsonObject2.get("New product introduction risks"));
		values.put("Intellectual Property Risks", jsonObject2.get("Intellectual Property Risks"));
		values.put("International risks", jsonObject2.get("International risks"));
		newOption.put("key", options.size() + 1);
		newOption.put("name", cpName); // add from req.
		newOption.put("values", values);
		newOption.put("description_html", "");
		newOption.put("app_data", new JSONObject());
		options.add(newOption);
		
		br.close();
		
		PrintWriter out = resp.getWriter();
		out.println(jsonObject1.toString());
		out.close();
	}

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
