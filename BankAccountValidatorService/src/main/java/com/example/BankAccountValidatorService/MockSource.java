package com.example.BankAccountValidatorService;

import java.util.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MockSource {
    private List<String> accountData = new ArrayList<String>();
    
    MockSource() {
    }
    
    String get(String request) {
    	JsonObject requestJson = JsonParser.parseString(request).getAsJsonObject();
    	String accountNumber = requestJson.get("accountNumber").toString();
    	accountNumber = accountNumber.substring(1, accountNumber.length() - 1);
        return "{\"isValid\": " + Boolean.toString(accountValid(accountNumber)) + "}";
    }
    
    void addAccount(String accountNumber) {
    	this.accountData.add(accountNumber);
    }
    
    boolean accountValid(String accountNumber) {
    	return this.accountData.contains(accountNumber);
    }
}