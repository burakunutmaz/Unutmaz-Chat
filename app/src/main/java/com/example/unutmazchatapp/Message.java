package com.example.unutmazchatapp;

public class Message {
    private String sentBy;
    private String data;
    private String messageId;
    private String hour;

    public Message(){

    }

    public Message(String sentBy, String data, String messageId, String hour){
        this.sentBy = sentBy;
        this.data = data;
        this.messageId = messageId;
        this.hour = hour;
    }

    public String getSentBy() {
        return sentBy;
    }

    public String getData() {
        return data;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getHour(){
        return hour;
    }


}

