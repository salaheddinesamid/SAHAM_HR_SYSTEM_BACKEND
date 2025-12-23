package com.saham.hr_system.modules.leave.dto;

public class NewLeaveRequestsCountMessage {

    private String sender;
    private long content;

    public NewLeaveRequestsCountMessage(long content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getContent() {
        return content;
    }

    public void setContent(long content) {
        this.content = content;
    }
}
