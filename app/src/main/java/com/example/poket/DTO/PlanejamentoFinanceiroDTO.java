package com.example.poket.DTO;

public class PlanejamentoFinanceiroDTO {

    private String idPF;
    private String nomePF;
    private String tipoPF;
    private String valorAtual;
    private String valorObjetivado;
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
