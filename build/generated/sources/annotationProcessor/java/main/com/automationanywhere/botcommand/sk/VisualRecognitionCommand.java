package com.automationanywhere.botcommand.sk;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.BotCommand;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import com.automationanywhere.core.security.SecureString;
import java.lang.ClassCastException;
import java.lang.Deprecated;
import java.lang.Object;
import java.lang.String;
import java.lang.Throwable;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class VisualRecognitionCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(VisualRecognitionCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    VisualRecognition command = new VisualRecognition();
    if(parameters.get("sourceFilePath") == null || parameters.get("sourceFilePath").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","sourceFilePath"));
    }

    if(parameters.get("language") == null || parameters.get("language").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","language"));
    }

    if(parameters.get("url") == null || parameters.get("url").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","url"));
    }

    if(parameters.get("apikey") == null || parameters.get("apikey").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","apikey"));
    }

    if(parameters.get("sourceFilePath") != null && parameters.get("sourceFilePath").get() != null && !(parameters.get("sourceFilePath").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","sourceFilePath", "String", parameters.get("sourceFilePath").get().getClass().getSimpleName()));
    }
    if(parameters.get("language") != null && parameters.get("language").get() != null && !(parameters.get("language").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","language", "String", parameters.get("language").get().getClass().getSimpleName()));
    }
    if(parameters.get("url") != null && parameters.get("url").get() != null && !(parameters.get("url").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","url", "String", parameters.get("url").get().getClass().getSimpleName()));
    }
    if(parameters.get("apikey") != null && parameters.get("apikey").get() != null && !(parameters.get("apikey").get() instanceof SecureString)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","apikey", "SecureString", parameters.get("apikey").get().getClass().getSimpleName()));
    }
    try {
      Optional<Value> result =  Optional.ofNullable(command.nlp(parameters.get("sourceFilePath") != null ? (String)parameters.get("sourceFilePath").get() : (String)null ,parameters.get("language") != null ? (String)parameters.get("language").get() : (String)null ,parameters.get("url") != null ? (String)parameters.get("url").get() : (String)null ,parameters.get("apikey") != null ? (SecureString)parameters.get("apikey").get() : (SecureString)null ));
      logger.traceExit(result);
      return result;
    }
    catch (ClassCastException e) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.IllegalParameters","nlp"));
    }
    catch (BotCommandException e) {
      logger.fatal(e.getMessage(),e);
      throw e;
    }
    catch (Throwable e) {
      logger.fatal(e.getMessage(),e);
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.NotBotCommandException",e.getMessage()),e);
    }
  }
}
