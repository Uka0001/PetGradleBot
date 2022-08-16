package org.uka0001.petgradlebot.messagesender;


import org.uka0001.petgradlebot.HelloWorldBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class MessageSenderImpl implements MessageSender {
  private HelloWorldBot helloWorldBot;

  @Override
  public void sendMessage(SendMessage sendMessage) {
    try {
      helloWorldBot.execute(sendMessage);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void sendEditMessage(EditMessageText editMessageText) {
    try {
      helloWorldBot.execute(editMessageText);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  @Autowired
  public void setHelloWorldBot(HelloWorldBot helloWorldBot) {
    this.helloWorldBot = helloWorldBot;
  }
}
