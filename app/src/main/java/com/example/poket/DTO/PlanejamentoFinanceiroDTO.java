package com.example.poket.DTO;

public class PlanejamentoFinanceiroDTO {

    private String idPF;
    private String nomePF;
    private String tipoPF;
    private Double valorAtual;
    private Double valorObjetivado;
    private String dataInicial;
    private String dataFinal;

    public String getIdPF() {
        return idPF;
    }

    public void setIdPF(String idPF) {
        this.idPF = idPF;
    }

    public String getNomePF() {
        return nomePF;
    }

    public void setNomePF(String nomePF) {
        this.nomePF = nomePF;
    }

    public String getTipoPF() {
        return tipoPF;
    }

    public void setTipoPF(String tipoPF) {
        this.tipoPF = tipoPF;
    }

    public Double getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(Double valorAtual) {
        this.valorAtual = valorAtual;
    }

    public Double getValorObjetivado() {
        return valorObjetivado;
    }

    public void setValorObjetivado(Double valorObjetivado) {
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
