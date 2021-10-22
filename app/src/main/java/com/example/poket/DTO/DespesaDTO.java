package com.example.poket.DTO;

public class DespesaDTO {

    private String id;
    private String despesa;
    private String valorDespesa;
    private String tipoDespesa;
    private String dataDespesa;
    private String observacao;

    private String idConta;
    private String conta;
    private String valorConta;
//    private String despesaFixa;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDespesa() {
        return despesa;
    }

    public void setDespesa(String despesa) {
        this.despesa = despesa;
    }

    public String getValorDespesa() {
        return valorDespesa;
    }

    public void setValorDespesa(String valorDespesa) {
        this.valorDespesa = valorDespesa;
    }

    public String getTipoDespesa() {
        return tipoDespesa;
    }

    public void setTipoDespesa(String tipoDespesa) {
        this.tipoDespesa = tipoDespesa;
    }

    public String getDataDespesa() {
        return dataDespesa;
    }

    public void setDataDespesa(String dataDespesa) {
        this.dataDespesa = dataDespesa;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

//    public String getDespesaFixa() {
//        return despesaFixa;
//    }

//    public void setDespesaFixa(String despesaFixa) {
//        this.despesaFixa = despesaFixa;
//    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getValorConta() {
        return valorConta;
    }

    public void setValorConta(String contaValor) {
        this.valorConta = contaValor;
    }

    public String getIdConta() {
        return idConta;
    }

    public void setIdConta(String idConta) {
        this.idConta = idConta;
    }
}
