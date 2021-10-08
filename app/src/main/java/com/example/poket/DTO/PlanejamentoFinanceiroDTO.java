package com.example.poket.DTO;

public class PlanejamentoFinanceiroDTO {

    private String id;
    private String planejamentoFinanceiro;
    private String tipoPlanejamentoFinanceiro;
    private String idConta;
    private String conta;
    private String contaValor;
    private String valorAtual;
    private String valorObjetivado;
    private String dataInicial;
    private String dataFinal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanejamentoFinanceiro() {
        return planejamentoFinanceiro;
    }

    public void setPlanejamentoFinanceiro(String planejamentoFinanceiro) {
        this.planejamentoFinanceiro = planejamentoFinanceiro;
    }

    public String getTipoPlanejamentoFinanceiro() {
        return tipoPlanejamentoFinanceiro;
    }

    public void setTipoPlanejamentoFinanceiro(String tipoPlanejamentoFinanceiro) {
        this.tipoPlanejamentoFinanceiro = tipoPlanejamentoFinanceiro;
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

    public String getContaValor() {
        return contaValor;
    }

    public void setContaValor(String contaValor) {
        this.contaValor = contaValor;
    }

    public String getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(String valorAtual) {
        this.valorAtual = valorAtual;
    }

    public String getValorObjetivado() {
        return valorObjetivado;
    }

    public void setValorObjetivado(String valorObjetivado) {
        this.valorObjetivado = valorObjetivado;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }
}
