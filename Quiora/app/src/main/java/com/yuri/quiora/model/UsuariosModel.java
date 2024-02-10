package com.yuri.quiora.model;

public class UsuariosModel {
    private String nome;
    private String email;
    private String senha;
    private String uid;
    private String nivelAcesso;
    private String premium;

    public UsuariosModel() {
    }

    public UsuariosModel(String nome, String email, String senha, String uid, String nivelAcesso, String premium) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.uid = uid;
        this.nivelAcesso = nivelAcesso;
        this.premium = premium;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }
}
