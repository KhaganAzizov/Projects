package com.projest;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ProjestBot extends TelegramLongPollingBot {
    private final String BotToken="1228529018:AAFFONumcNaubKfghVoBM4FEZEE0azN3zyQ";
    private final String BotUsername="projest_bot";
    SendMessage smessage=new SendMessage();
    Message message=new Message();
    ResourceBundle res;
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (message.getText().equals("/start")) {
                smessage.setChatId(message.getChatId());
                smessage.setText("Choose language:");
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton().setText("AZ").setCallbackData("lng_az"));
                rowInline.add(new InlineKeyboardButton().setText("EN").setCallbackData("lng_en"));
                rowInline.add(new InlineKeyboardButton().setText("RU").setCallbackData("lng_ru"));
                rowsInline.add(rowInline);
                markupInline.setKeyboard(rowsInline);
                smessage.setReplyMarkup(markupInline);
                try {
                    execute(smessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(update.hasCallbackQuery()){
            String call_data = update.getCallbackQuery().getData();
            switch (call_data) {
                case "lng_az" -> Locale.setDefault(new Locale("az", "AZ"));
                case "lng_en" -> Locale.setDefault(new Locale("en", "US"));
                case "lng_ru" -> Locale.setDefault(new Locale("ru", "RU"));
            }
            res = ResourceBundle.getBundle("Messagetexts");
            smessage.setReplyMarkup(null);
            smessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
            smessage.setText(res.getString("Welcome"));
            try {
                execute(smessage);
            }catch (TelegramApiException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BotUsername;
    }

    @Override
    public String getBotToken() {
        return BotToken;
    }
}
