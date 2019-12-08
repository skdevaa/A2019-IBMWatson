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
import static com.automationanywhere.commandsdk.model.DataType.STRING;


import java.util.ArrayList;
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

import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import com.automationanywhere.core.security.SecureString;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.natural_language_understanding.v1.model.EntitiesResult;
import com.ibm.watson.natural_language_understanding.v1.model.Features;


/**
 * @author Stefan Karsten
 *
 */

@BotCommand
@CommandPkg(label = "Call Watson NLP ", name = "watsonnlp",
        description = "Calls Watson Natural Language Understanding",
        node_label = "Analyze {{textstr}} with IBM Watson NLP ", icon = "",return_type=RECORD, return_sub_type = STRING,return_label="Assign the output to variable", return_required=true)
public class NLU  {
	@Execute
    public Value<Record> nlp(@Idx(index = "1", type = TEXT)  @Pkg(label = "Text" , default_value_type = STRING) @NotEmpty String textstr,
                        @Idx(index = "2", type = TEXT) @Pkg(label = "Watson URL"  , default_value_type = DataType.STRING ) @NotEmpty String url,
                        @Idx(index = "3", type = AttributeType.CREDENTIAL) @Pkg(label = "APIKEY" ) @NotEmpty SecureString apikey)
     {
    	
		Record record;
    	Value<Record> valuerecord = new RecordValue();
    	List<Schema> schemas = new ArrayList<Schema>();
		List<Value> values = new ArrayList<Value>();
		
		String apiunsecure = apikey.getInsecureString();
    	IamAuthenticator authenticator = new IamAuthenticator(apiunsecure);
    	NaturalLanguageUnderstanding naturalLanguageUnderstanding = new NaturalLanguageUnderstanding("2019-07-12", authenticator);
    	naturalLanguageUnderstanding.setServiceUrl(url);


    	EntitiesOptions entities= new EntitiesOptions.Builder()
    	  .sentiment(true)
    	  .limit(20)
    	  .build();

    	Features features = new Features.Builder()
    	  .entities(entities)
    	  .build();

    	AnalyzeOptions parameters = new AnalyzeOptions.Builder()
    	  .text(textstr)
    	  .features(features)
    	  .build();

    	AnalysisResults response = naturalLanguageUnderstanding
    	  .analyze(parameters)
    	  .execute()
    	  .getResult();
    	
    	List<EntitiesResult> entityResult =  response.getEntities();

    	for (int i = 0; i < entityResult.size(); i++) {
    		 String type = entityResult.get(i).getType().toString();
    		 String text = entityResult.get(i).getText().toString();
    		 Schema schema =  new Schema();
    		 schema.setName(type); 
    		 schemas.add(schema);
    		 values.add(new StringValue(text));
		}
    	record = new Record();
    	record.setSchema(schemas);
    	record.setValues(values);

    	valuerecord.set(record);
		return valuerecord;
    	

    }   
}
	
