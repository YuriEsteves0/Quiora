package com.yuri.quiora.model;

import java.util.List;

public class ChatRoomModel {
    private List<String> idsUsu;
    private String chatRoomUid;

    public ChatRoomModel() {
    }

    public ChatRoomModel(String chatRoomUid, List<String> idsUsu){
        this.chatRoomUid = chatRoomUid;
        this.idsUsu = idsUsu;
    }

    public List<String> getIdsUsu() {
        return idsUsu;
    }

    public void setIdsUsu(List<String> idsUsu) {
        this.idsUsu = idsUsu;
    }

    public String getChatRoomUid() {
        return chatRoomUid;
    }

    public void setChatRoomUid(String chatRoomUid) {
        this.chatRoomUid = chatRoomUid;
    }
}
