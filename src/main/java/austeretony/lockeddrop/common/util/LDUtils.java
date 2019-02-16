package austeretony.lockeddrop.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class LDUtils {

    public static JsonElement getInternalJsonData(String path) throws IOException {	
        JsonElement rawData = null;		
        try (InputStream inputStream = LDUtils.class.getClassLoader().getResourceAsStream(path)) {				    	
            rawData = new JsonParser().parse(new InputStreamReader(inputStream, "UTF-8"));  
        }
        return rawData;
    }

    public static JsonElement getExternalJsonData(String path) throws IOException {		
        JsonElement rawData = null;		
        try (InputStream inputStream = new FileInputStream(new File(path))) {				    	
            rawData = new JsonParser().parse(new InputStreamReader(inputStream, "UTF-8"));  
        }		
        return rawData;
    }

    public static void createExternalJsonFile(String path, JsonElement data) throws IOException {		
        try (Writer writer = new FileWriter(path)) {    		    	        	
            new GsonBuilder().setPrettyPrinting().create().toJson(data, writer);
        }
    }
}
