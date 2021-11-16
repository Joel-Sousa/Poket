package com.example.poket.DTO;

public class RendaDTO{

    private String id;
    private String renda;
    private Double valorRenda;
    private String tipoRenda;
    private String dataRenda;
    private String observacao;

    private String idConta;
    private String conta;
    private Double valorConta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRenda() {
        return renda;
    }

    public void setRenda(String renda) {
        this.renda = renda;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public Double getValorConta() {
        return valorConta;
    }

    public void setValorConta(Double contaValor) {
        this.valorConta = contaValor;
    }

    public Double getValorRenda() {
        return valorRenda;
    }

    public void setValorRenda(Double valorRenda) {
        this.valorRenda = valorRenda;
    }

    public String getTipoRenda() {
        return tipoRenda;
    }

    public void setTipoRenda(String tipoRenda) {
        this.tipoRenda = tipoRenda;
    }

    public String getDataRenda() {
        return dataRenda;
    }

    public void setDataRenda(String dataRenda) {
        this.dataRenda = dataRenda;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getIdConta() { return idConta;}

    public void setIdConta(String idConta) {
        this.idConta = idConta;
    }
}
