package com.yuri.quiora.model;

public class NotificacaoModel {
    private String msg;
    private String uidUsuRemetente;
    private String msgNova;
    private String uidUsuDestinatario;

    public NotificacaoModel() {
    }

    public NotificacaoModel(String msg, String msgNova, String uidUsuRemetente, String uidUsuDestinatario ) {
        this.msg = msg;
        this.uidUsuRemetente = uidUsuRemetente;
        this.uidUsuDestinatario = uidUsuDestinatario;
        this.msgNova = msgNova;
    }

    public String getMsg() {
        return msg;
    }

    public String getMsgNova() {
        return msgNova;
    }

    public void setMsgNova(String msgNova) {
        this.msgNova = msgNova;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUidUsuRemetente() {
        return uidUsuRemetente;
    }

    public void setUidUsuRemetente(String uidUsuRemetente) {
        this.uidUsuRemetente = uidUsuRemetente;
    }

    public String getUidUsuDestinatario() {
        return uidUsuDestinatario;
    }

    public void setUidUsuDestinatario(String uidUsuDestinatario) {
        this.uidUsuDestinatario = uidUsuDestinatario;
    }
}
