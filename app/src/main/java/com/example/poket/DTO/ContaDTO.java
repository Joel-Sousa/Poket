package com.example.poket.DTO;

import java.util.Objects;

public class ContaDTO {
    private String uid;
    private String id;
    private String conta;
    private String valor;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContaDTO contaDTO = (ContaDTO) o;
        return Objects.equals(uid, contaDTO.uid) && Objects.equals(id, contaDTO.id) && Objects.equals(conta, contaDTO.conta) && Objects.equals(valor, contaDTO.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, id, conta, valor);
    }

    @Override
    public String toString() {
        return conta;
    }
}
