package com.projest;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static java.lang.StrictMath.toIntExact;

public class ProjestBot extends TelegramLongPollingBot {
    private final String BotToken="";
    private final String BotUsername="projest_bot";
    String prjct_name,prjct_desc,prjct_req,prjct_paidornot,prjct_mail,prjct_contact;
    boolean startedquestions=false;
    int questnum=1;
    SendMessage smessage=new SendMessage();
    Message message=new Message();
    ResourceBundle res;
    ForceReplyKeyboard frc=new ForceReplyKeyboard();
    @Override
    public void onUpdateReceived(Update update) {
        message = update.getMessage();
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (message.getText().equals("/start")) {
                resetasking();
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
                smessage.setText(res.getString("prjct_begining_info"));
                smessage.setChatId(message.getChatId());
                try {
                    execute(smessage);
                }catch (TelegramApiException e){
                    e.printStackTrace();
                }
                ask_quest(questnum);
                startedquestions=true;
            }
            else if(startedquestions&&questnum<=6){
                switch (questnum){
                    case 1->prjct_name=res.getString("prjct_name")+" "+message.getText();
                    case 2->prjct_desc=res.getString("prjct_desc")+" "+message.getText();
                    case 3->prjct_req=res.getString("prjct_req")+" "+message.getText();
                    case 4->prjct_paidornot=res.getString("prjct_paidornot")+" "+message.getText();
                    case 5->prjct_mail=res.getString("prjct_mail")+" "+message.getText();
                    case 6->prjct_contact=res.getString("prjct_contact")+" "+message.getText();
                }
                questnum++;
                ask_quest(questnum);
            }
            if(questnum==7){
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton().setText(res.getString("send_to_channel")).setCallbackData("senddata"));
                rowsInline.add(rowInline);
                markupInline.setKeyboard(rowsInline);
                String prjct_msg=prjct_name+"\n"+prjct_desc+"\n"+prjct_req+"\n"+prjct_paidornot+"\n"+prjct_mail+"\n"+prjct_contact;
                smessage.setChatId(message.getChatId());
                smessage.setText(res.getString("send_msg_like")+"\n"+prjct_msg);
                smessage.setReplyMarkup(markupInline);
                try{
                    execute(smessage);
                }catch (TelegramApiException e){
                    e.printStackTrace();
                }
                finally {
                    resetasking();
                    smessage.setReplyMarkup(null);
                }
            }
        }
        else if(update.hasCallbackQuery()){
            if(update.getCallbackQuery().getData().equals("senddata")){

                String msg_to_channel=prjct_name+"\n"+prjct_desc+"\n"+prjct_req+"\n"+prjct_paidornot+"\n"+prjct_mail+"\n"+prjct_contact;
                smessage.setChatId("-1001498084709");
                smessage.setText(msg_to_channel);
                try{
                    execute(smessage);
                    long message_id = update.getCallbackQuery().getMessage().getMessageId();
                    EditMessageText new_message = new EditMessageText()
                            .setChatId(update.getCallbackQuery().getMessage().getChatId())
                            .setMessageId(toIntExact(message_id))
                            .setText(res.getString("send_succes"));
                    execute(new_message);
                }catch (TelegramApiException e){
                    e.printStackTrace();
                }
                finally {
                    resetasking();
                }
            }
            else {
                resetasking();
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
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void ask_quest(int questnum){
        switch (questnum){
            case 1->ask_prjct_name();
            case 2->ask_prjct_desc();
            case 3->ask_prjct_req();
            case 4->ask_prjct_paidornot();
            case 5->ask_mail();
            case 6->ask_contacts();
            case 7->prjct_announce();
        }
    }
    public void ask_prjct_name(){
        smessage.setText(res.getString("ask_prjct_name"));
        smessage.setChatId(message.getChatId());
        smessage.setReplyMarkup(frc);
        try {
            execute(smessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void ask_prjct_desc(){
        smessage.setText(res.getString("ask_prjct_desc"));
        smessage.setChatId(message.getChatId());
        smessage.setReplyMarkup(frc);
        try {
            execute(smessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void ask_prjct_req(){
        smessage.setText(res.getString("ask_prjct_req"));
        smessage.setChatId(message.getChatId());
        smessage.setReplyMarkup(frc);
        try {
            execute(smessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void ask_prjct_paidornot(){
        smessage.setText(res.getString("ask_prjct_paidornot"));
        smessage.setChatId(message.getChatId());
        smessage.setReplyMarkup(frc);
        try {
            execute(smessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void ask_mail(){
        smessage.setText(res.getString("ask_mail"));
        smessage.setChatId(message.getChatId());
        smessage.setReplyMarkup(frc);
        try {
            execute(smessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void ask_contacts(){
        smessage.setText(res.getString("ask_contacts"));
        smessage.setChatId(message.getChatId());
        smessage.setReplyMarkup(frc);
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
