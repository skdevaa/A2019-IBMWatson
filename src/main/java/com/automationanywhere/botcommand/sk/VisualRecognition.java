/*
 * Copyright (c) 2019 Automation Anywhere.
 * All rights reserved.
 *
 * This software is the proprietary information of Automation Anywhere.
 * You shall use it only in accordance with the terms of the license agreement
 * you entered into with Automation Anywhere.
 */
/**
 * 
 */
package com.automationanywhere.botcommand.sk;



import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.RECORD;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.automationanywhere.botcommand.data.Value;

import com.automationanywhere.botcommand.data.impl.RecordValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.model.Schema;
import com.automationanywhere.botcommand.data.model.record.Record;


import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.rules.LocalFile;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;

import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import com.automationanywhere.core.security.SecureString;
import com.ibm.cloud.sdk.core.http.HttpConfigOptions;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.visual_recognition.v3.model.ClassResult;
import com.ibm.watson.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.visual_recognition.v3.model.ClassifierResult;
import com.ibm.watson.visual_recognition.v3.model.ClassifyOptions;

/**
 * @author Stefan Karsten
 *
 */


@BotCommand
@CommandPkg(label = "Call Watson Visual", name = "watsonvisualrecognition",
        description = "Calls Watson Visual Recognition",
        node_label = "Analyze {{sourceFilePath}} with IBM Watson Visual ", icon = "",return_type=RECORD, return_label="Assign the output to variable", return_required=true)
public class VisualRecognition  {
	@Execute
    public Value<Record> nlp( @Idx(index = "1", type = AttributeType.FILE) @LocalFile @Pkg(label = "File path") @NotEmpty String sourceFilePath,
    	                @Idx(index = "2", type = AttributeType.TEXT)  @Pkg(label = "Language") @NotEmpty String language,
                        @Idx(index = "3", type = TEXT) @Pkg(label = "Watson URL"  , default_value_type = DataType.STRING ) @NotEmpty String url,
                        @Idx(index = "4", type = AttributeType.CREDENTIAL) @Pkg(label = "APIKEY" ) @NotEmpty SecureString apikey) throws FileNotFoundException
     {
		
		
    	Value<Record> valuerecord = new RecordValue();
		Record record;
    	List<Schema> schemas = new ArrayList<Schema>();
		List<Value> values = new ArrayList<Value>();
		
		
		IamAuthenticator authenticator = new IamAuthenticator(apikey.getInsecureString());
		com.ibm.watson.visual_recognition.v3.VisualRecognition visualRecognition = new com.ibm.watson.visual_recognition.v3.VisualRecognition("2018-03-19", authenticator);
		HttpConfigOptions configOptions = new HttpConfigOptions.Builder()
				  .disableSslVerification(true)
				  .build();
				visualRecognition.configureClient(configOptions);
		InputStream imagesStream = new FileInputStream(sourceFilePath);
		ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
		  .imagesFile(imagesStream)
		   .imagesFilename(sourceFilePath)
		   .acceptLanguage(language)
		  .classifierIds(Arrays.asList("default"))
		  .build();
		ClassifiedImages result = visualRecognition.classify(classifyOptions).execute().getResult();
		List<ClassifierResult> classifiers = result.getImages().get(0).getClassifiers();
		
    	for (int j = 0; j < classifiers.size(); j++) {
			
   		 String type = classifiers.get(j).getName();
   		 List<ClassResult> classes = classifiers.get(j).getClasses();
   		 
   		 for (int k = 0; k < classes.size(); k++) {
   			String classname = classes.get(k).getXClass().toString();
      		 Schema schema =  new Schema();
       		 schema.setName(type);	 
       		 schemas.add(schema);
       		 values.add(new StringValue(classname));
	   	 } 
  
		}
    	
    	record = new Record();
    	record.setSchema(schemas);
    	record.setValues(values);

    	valuerecord.set(record);
		return valuerecord;
		
		
		
    }   
}
	
