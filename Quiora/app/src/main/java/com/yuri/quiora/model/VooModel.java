package com.yuri.quiora.model;

import java.util.List;

public class VooModel {
    private List<UsuariosModel> uidUsu;
    private String de;
    private String uidGerenteVoo;
    private String para;
    private String uidViagem;
    private int qntPassageiros;
    private String horarioChegada;
    private String horarioSaida;
    private String dataViagem;
    private String nomeCompanhia;
    private String preco;

    public VooModel() {

    }

    public String getUidGerenteVoo() {
        return uidGerenteVoo;
    }

    public void setUidGerenteVoo(String uidGerenteVoo) {
        this.uidGerenteVoo = uidGerenteVoo;
    }

    public List<UsuariosModel> getUidUsu() {
        return uidUsu;
    }

    public String getDataViagem() {
        return dataViagem;
    }

    public void setDataViagem(String dataViagem) {
        this.dataViagem = dataViagem;
    }

    public void setUidUsu(List<UsuariosModel> uidUsu) {
        this.uidUsu = uidUsu;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getUidViagem() {
        return uidViagem;
    }

    public void setUidViagem(String uidViagem) {
        this.uidViagem = uidViagem;
    }

    public int getQntPassageiros() {
        return qntPassageiros;
    }

    public void setQntPassageiros(int qntPassageiros) {
        this.qntPassageiros = qntPassageiros;
    }

    public String getHorarioChegada() {
        return horarioChegada;
    }

    public void setHorarioChegada(String horarioChegada) {
        this.horarioChegada = horarioChegada;
    }

    public String getHorarioSaida() {
        return horarioSaida;
    }

    public void setHorarioSaida(String horarioSaida) {
        this.horarioSaida = horarioSaida;
    }

    public String getNomeCompanhia() {
        return nomeCompanhia;
    }

    public void setNomeCompanhia(String nomeCompanhia) {
        this.nomeCompanhia = nomeCompanhia;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }
}
