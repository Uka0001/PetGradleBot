package org.uka0001.petgradlebot.handlers;

import org.uka0001.petgradlebot.cache.Cache;
import org.uka0001.petgradlebot.domain.BotUser;
import org.uka0001.petgradlebot.domain.Position;
import org.uka0001.petgradlebot.messagesender.MessageSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.uka0001.petgradlebot.domain.Position.INPUT_FULLNAME;

@Component
public class MessageHandler implements Handler<Message> {

  private final MessageSender messageSender;
  private final Cache<BotUser> cache;

  public MessageHandler(MessageSender messageSender, Cache<BotUser> cache) {
    this.messageSender = messageSender;
    this.cache = cache;
  }

  private BotUser createNewBotUser(Message message) {
    BotUser botUser = new BotUser();
    botUser.setId(message.getChatId());
    botUser.setUsername(message.getFrom().getUserName());
    botUser.setPosition(INPUT_FULLNAME);
    return botUser;
  }

  @Override
  public void choose(Message message) {
    BotUser user = cache.findById(message.getChatId());
    if (user != null) {
      switch (user.getPosition()) {
        case INPUT_FULLNAME:
          if (message.hasText()) {
            user.setFullName(message.getText());
            user.setPosition(Position.INPUT_PHONE_NUMBER);
            messageSender.sendMessage(
                    SendMessage.builder()
                            .text("Введіть номер телефона(кнока знизу)")
                            .chatId(String.valueOf(message.getChatId()))
                            .replyMarkup(ReplyKeyboardMarkup.builder()
                                    .keyboardRow(new KeyboardRow() {{
                                      add(KeyboardButton.builder()
                                              .text("Відправити номер")
                                              .requestContact(true)
                                              .build());
                                    }}).build())
                            .build());
          }
          break;
        case INPUT_PHONE_NUMBER:
          user.setPhoneNumber(message.getContact().getPhoneNumber());
          user.setPosition(Position.INPUT_PHONE_NUMBER);
          messageSender.sendMessage(SendMessage.builder()
                  .parseMode("HTML")
                  .chatId(String.valueOf(user.getId()))
                  .text("<b>id:</b>" + user.getId() +
                          "\n<b>ПІБ:</b> " + user.getFullName() + "\n" +
                          "<b>Номер телефону:</b>" + user.getPhoneNumber())
                  .build());


          break;

      }
    } else if (message.hasText()) {
      if (message.getText().equals("/reg")) {
        BotUser newBotUser = createNewBotUser(message);
        cache.add(newBotUser);
        messageSender.sendMessage(SendMessage.builder()
                .text("Відправте ваш ПІБ:")
                .chatId(String.valueOf(message.getChatId()))
                .build());
      }
      if (message.getText().equals("/start")) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText("перший адвокат знайдений для вас");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(
                Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text("другий адвокат знайдений для вас")
                                .callbackData("next")
                                .build()));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        messageSender.sendMessage(sendMessage);
      }
    }
  }
}
