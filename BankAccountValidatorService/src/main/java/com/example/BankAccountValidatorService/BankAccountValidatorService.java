package com.example.BankAccountValidatorService;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SpringBootApplication
@RestController
public class BankAccountValidatorService {
    private static HashMap<String, MockSource> sources = new HashMap<String, MockSource>();

    private final RestTemplate restTemplate = new RestTemplate();
    
    private boolean initialized = false;
    
    @Value("#{'${sources.names}'.split(',')}")
    private List<String> sourceNames;
   
    @Value("#{'${sources.urls}'.split(',')}")
    private List<String> sourceUrls;
	
    public static void main(String[] args) {
	SpringApplication.run(BankAccountValidatorService.class, args);
    }
	
    // setup source and account data
    public void setup() {
	Integer accountNumber = 12345678;
	for (String name : sourceNames) {
            sources.put(name, new MockSource());
	    // add 5 accounts to each source
	    for (int i = 0; i < 5; i++) {
		 sources.get(name).addAccount(accountNumber.toString());
		 accountNumber++;
	    }
        }
    }
	
    // setup source and account data mapping
    @PostMapping("/initialize")
    public String initializer() {
	if (sources.size() == 0) {
	    setup();
	    initialized = true;
	    return "{ \"Initialization\": \"success\" }";
	}
	return "{ \"Initialization\": \"failed\" }";
    }
	
    // Aggregation request mapping
    @GetMapping("/validator")
    public String validator(@RequestBody String body) {
	JsonObject response = new JsonObject();
	JsonArray responseArray = new JsonArray();
	if (initialized) {
	    JsonObject request = JsonParser.parseString(body).getAsJsonObject();
	    // Error checking: make sure request json contains accountNumber, else return empty result
	    if (request.has("accountNumber")) {
		JsonArray sourcesJson = new JsonArray();
		// Get optional sources field in request json and set json array value
		// Else if sources not in request json, add all source names in configuration file to source list
		if (request.has("sources")) {
		    sourcesJson = request.get("sources").getAsJsonArray();
		}
		else {
		    for (String name: sourceNames) {
			 sourcesJson.add(name);
		    }
		}
		// poll all sources in request json
		for (JsonElement sourceElement : sourcesJson) {
		     String sourceResponse = new String();
		     String sourceString = sourceElement.toString();
		     String trimmedSourceString = sourceString.substring(1, sourceString.length() - 1);
		     if (sourceNames.contains(trimmedSourceString)) {
			 String sourceRequestBody = "{ \"accountNumber\": " + request.get("accountNumber").toString() + " }";
			 HttpEntity<String> entity = new HttpEntity<String>(sourceRequestBody);
			 Integer sourceListPosition = sourceNames.indexOf(trimmedSourceString);
			 // Post request must be made since get requests using this function does not take request body
			 // Return response with valid field with input request of account number
			 ResponseEntity<String> responseEntity = restTemplate.exchange(sourceUrls.get(sourceListPosition), HttpMethod.POST, entity, String.class);
			 sourceResponse = responseEntity.getBody();
			 JsonObject sourceResponseObject = JsonParser.parseString(sourceResponse).getAsJsonObject();
			 sourceResponseObject.add("source", sourceElement);
			 // add valid/source field combination to array
			 responseArray.add(sourceResponseObject);
		     }
		}
	    }
	}
	response.add("result", responseArray);
	return response.toString();
    }
	
    // Data source response mapping
    @RequestMapping("/{source}")
    public String source1(@PathVariable String source, @RequestBody String body) {
    	// check if account is in list of valid accounts
        return sources.get(source).get(body);
    }
}
