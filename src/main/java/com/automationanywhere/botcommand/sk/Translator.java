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
import static com.automationanywhere.commandsdk.model.DataType.STRING;


import com.automationanywhere.botcommand.data.Value;

import com.automationanywhere.botcommand.data.impl.StringValue;


import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;

import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import com.automationanywhere.core.security.SecureString;

import com.ibm.cloud.sdk.core.http.HttpConfigOptions;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
;
/**
 * @author Stefan Karsten
 *
 */

@BotCommand
@CommandPkg(label = "Call Watson Translator ", name = "watsontranslator",
        description = "Calls Watson Language Translator",
        node_label = "Translate{{textstr}} from-to {{fromto}} with IBM Watson Translator", icon = "",return_type=STRING, return_label="Assign the output to variable", return_required=true)
public class Translator  {
	@Execute
    public Value<String> nlp(@Idx(index = "1", type = TEXT)  @Pkg(label = "Text" , default_value_type = STRING) @NotEmpty String textstr,
    	            	@Idx(index = "2", type = TEXT)  @Pkg(label = "from-to" , default_value_type = STRING) @NotEmpty String fromto,
                        @Idx(index = "3", type = TEXT) @Pkg(label = "Watson URL"  , default_value_type = DataType.STRING ) @NotEmpty String url,
                        @Idx(index = "4", type = AttributeType.CREDENTIAL) @Pkg(label = "APIKEY" ) @NotEmpty SecureString apikey)
     {
    	
		
    	IamAuthenticator authenticator = new IamAuthenticator(apikey.getInsecureString());
    	
    	LanguageTranslator languageTranslator = new LanguageTranslator("2018-05-01", authenticator);
    	languageTranslator.setServiceUrl(url);
    	
    	HttpConfigOptions configOptions = new HttpConfigOptions.Builder()
    	    	  .disableSslVerification(true)
    	    	  .build();
        languageTranslator.configureClient(configOptions);


    	TranslateOptions translateOptions = new TranslateOptions.Builder()
    	  .addText(textstr)
    	  .modelId(fromto)
    	  .build();

    	TranslationResult result = languageTranslator.translate(translateOptions)
    	  .execute().getResult();
    	
    	Value<String> resultvalue = new StringValue(result.getTranslations().get(0).getTranslation());

    	return resultvalue;
    	

    }   
}
	
