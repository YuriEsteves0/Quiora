package com.yuri.quiora.model;

public class PedidoModel {

    private String idUsuPedido;
    private String msgPedido;

    public PedidoModel(String idUsuPedido, String msgPedido) {
        this.idUsuPedido = idUsuPedido;
        this.msgPedido = msgPedido;
    }

    public PedidoModel() {
    }

    public String getIdUsuPedido() {
        return idUsuPedido;
    }

    public void setIdUsuPedido(String idUsuPedido) {
        this.idUsuPedido = idUsuPedido;
    }

    public String getMsgPedido() {
        return msgPedido;
    }

    public void setMsgPedido(String msgPedido) {
        this.msgPedido = msgPedido;
    }
}
