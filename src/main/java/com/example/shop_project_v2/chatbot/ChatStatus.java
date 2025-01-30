package com.example.shop_project_v2.chatbot;

public enum ChatStatus {
	WAITING("상담 대기중"), ING("상담중"), CLOSE("상담 끝");
	
    private final String value;

	ChatStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
