package org.uka0001.petgradlebot.services;

import org.uka0001.petgradlebot.messagesender.MessageSender;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class SendMessageService {

  private final MessageSender messageSender;

  public SendMessageService(MessageSender messageSender) {
    this.messageSender = messageSender;
  }

  public void test1(Message message) {
    SendMessage ms1 = SendMessage.builder()
            .text("<b>Bold</b> " +
                    "<i>italic</i>" +
                    " <code>mono</code> " +
                    "<a href=\"google.com\">Google</a>")
            .parseMode("HTML")
            .chatId(String.valueOf(message.getChatId()))
            .build();

    messageSender.sendMessage(ms1);
  }

  public void test2(Message message) {
    ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
    List<KeyboardRow> keyboardRows = new ArrayList<KeyboardRow>();
    KeyboardRow row1 = new KeyboardRow();
    KeyboardRow row2 = new KeyboardRow();
    KeyboardRow row3 = new KeyboardRow();
    row1.add("Button 1");
    row1.add("Button 2");
    row1.add("Button 3");
    row2.add(KeyboardButton.builder().text("Phone Number")
            .requestContact(true)
            .build());
    row3.add(KeyboardButton.builder()
            .requestLocation(true)
            .text("Location")
            .build());
    keyboardRows.add(row1);
    keyboardRows.add(row2);
    keyboardRows.add(row3);
    markup.setKeyboard(keyboardRows);
    markup.setResizeKeyboard(true);
    markup.setOneTimeKeyboard(true);
    SendMessage sendMessage = new SendMessage();
    sendMessage.setText("/start");
    sendMessage.setChatId(String.valueOf(message.getChatId()));
    sendMessage.setReplyMarkup(markup);
    messageSender.sendMessage(sendMessage);
  }
}
