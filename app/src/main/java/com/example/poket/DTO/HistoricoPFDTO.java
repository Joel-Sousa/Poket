package com.example.poket.DTO;

public class HistoricoPFDTO {
    private String idHistorico;
    private String idPF;
    private Double valorHistoricoPF;
    private String dataHistorico;

    private String idConta;
    private String conta;
    private Double valorConta;

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

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public Double getValorHistoricoPF() {
        return valorHistoricoPF;
    }

    public void setValorHistoricoPF(Double valorHistoricoPF) {
        this.valorHistoricoPF = valorHistoricoPF;
    }

    public String getDataHistorico() {
        return dataHistorico;
    }

    public void setDataHistorico(String dataHistorico) {
        this.dataHistorico = dataHistorico;
    }

    public Double getValorConta() {
        return valorConta;
    }

    public void setValorConta(Double valorConta) {
        this.valorConta = valorConta;
    }
}
