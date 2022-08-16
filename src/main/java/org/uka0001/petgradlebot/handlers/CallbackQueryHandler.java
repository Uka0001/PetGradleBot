package org.uka0001.petgradlebot.handlers;

import org.uka0001.petgradlebot.messagesender.MessageSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;

@Component
public class CallbackQueryHandler implements Handler<CallbackQuery> {

  private final MessageSender messageSender;

  public CallbackQueryHandler(MessageSender messageSender) {
    this.messageSender = messageSender;
  }

  @Override
  public void choose(CallbackQuery callbackQuery) {
    if (callbackQuery.getData().equals("next")) {
      String poemText = "другий адвокат, якого ми знайшли";
      Integer messageId = callbackQuery.getMessage().getMessageId();
      EditMessageText editMessageText = new EditMessageText();
      editMessageText.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
      editMessageText.setMessageId(messageId);
      editMessageText.setText(poemText);
      editMessageText.setReplyMarkup(
              InlineKeyboardMarkup.builder()
                      .keyboardRow(
                              Collections.singletonList(
                              InlineKeyboardButton.builder()
                                      .text("Посилання")
                                      .url("http://google.com.ua")
                                      .build()
                      )).build());
      messageSender.sendEditMessage(editMessageText);
    }
  }
}
