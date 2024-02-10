package com.yuri.quiora.model;

public class MensagensModel {
    private String uidRemetente;
    private String msg;

    public MensagensModel() {
    }

    public MensagensModel(String uidRemetente, String msg) {
        this.uidRemetente = uidRemetente;
        this.msg = msg;
    }

    public String getUidRemetente() {
        return uidRemetente;
    }

    public void setUidRemetente(String uidRemetente) {
        this.uidRemetente = uidRemetente;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
