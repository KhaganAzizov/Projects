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
    String prjct_name,prjct_desc,prjct_paidornot,prjct_mail,prjct_contact;
    boolean startedquestions=false;
    int questnum=1;
    SendMessage smessage=new SendMessage();
    Message message=new Message();
    ResourceBundle res;
    @Override
    public void onUpdateReceived(Update update) {
        message = update.getMessage();
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
            if(message.getText().equals("/go")){
                resetasking();
                ask_quest(questnum);
                startedquestions=true;
            }
            else if(startedquestions&&questnum<=5){
                switch (questnum){
                    case 1->prjct_name=message.getText();
                    case 2->prjct_desc=message.getText();
                    case 3->prjct_paidornot=message.getText();
                    case 4->prjct_mail=message.getText();
                    case 5->prjct_contact=message.getText();
                }
                questnum++;
                ask_quest(questnum);
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

    public void ask_quest(int questnum){
        switch (questnum){
            case 1->ask_prjct_name();
            case 2->ask_prjct_desc();
            case 3->ask_prjct_paidornot();
            case 4->ask_mail();
            case 5->ask_contacts();
            case 6->prjct_announce();
        }
    }
    public void ask_prjct_name(){
        smessage.setText(res.getString("ask_prjct_name"));
        smessage.setChatId(message.getChatId());
        try {
            execute(smessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void ask_prjct_desc(){
        smessage.setText(res.getString("ask_prjct_desc"));
        smessage.setChatId(message.getChatId());
        try {
            execute(smessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void ask_prjct_paidornot(){
        smessage.setText(res.getString("ask_prjct_paidornot"));
        smessage.setChatId(message.getChatId());
        try {
            execute(smessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void ask_mail(){
        smessage.setText(res.getString("ask_mail"));
        smessage.setChatId(message.getChatId());
        try {
            execute(smessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void ask_contacts(){
        smessage.setText(res.getString("ask_contacts"));
        smessage.setChatId(message.getChatId());
        try {
            execute(smessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void prjct_announce(){
        System.out.println("Finished");
    }
    private void resetasking(){
        startedquestions=false;
        questnum=1;
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
