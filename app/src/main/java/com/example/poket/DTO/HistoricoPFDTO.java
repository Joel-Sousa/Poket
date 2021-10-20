package com.example.poket.DTO;

public class HistoricoPFDTO {
    private String idHistorico;
    private String idPF;
    private String idConta;
    private String nomeConta;
    private String valorConta;

    public String getIdHistorico() {
        return idHistorico;
    }

    public void setIdHistorico(String idHistorico) {
        this.idHistorico = idHistorico;
    }

    public String getIdPF() {
        return idPF;
    }

    public void setIdPF(String idPF) {
        this.idPF = idPF;
    }

    public String getIdConta() {
        return idConta;
    }

    public void setIdConta(String idConta) {
        this.idConta = idConta;
    }

    public String getNomeConta() {
        return nomeConta;
    }

    public void setNomeConta(String nomeConta) {
        this.nomeConta = nomeConta;
    }

    public String getValorConta() {
        return valorConta;
    }

    public void setValorConta(String valorConta) {
        this.valorConta = valorConta;
    }
}
