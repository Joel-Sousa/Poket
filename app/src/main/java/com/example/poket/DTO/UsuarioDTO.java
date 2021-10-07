package com.example.poket.DTO;

import java.util.Objects;

public class UsuarioDTO {

    private String uid;
    private String apelido;
    private String email;
    private String senha;
    private String repetirSenha;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
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

    public String getRepetirSenha() {
        return repetirSenha;
    }

    public void setRepetirSenha(String repetirSenha) {
        this.repetirSenha = repetirSenha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioDTO that = (UsuarioDTO) o;
        return Objects.equals(senha, that.senha) && Objects.equals(repetirSenha, that.repetirSenha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senha, repetirSenha);
    }
}
