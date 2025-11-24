package br.com.motusia.api.identity.dto;

public class AlunoDashboardDto {
    private Long id;
    private String nome;
    private String nivelAtual;
    private double taxaAcertoUltimos10;
    private boolean teveIntervencaoManual;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNivelAtual() {
        return nivelAtual;
    }

    public void setNivelAtual(String nivelAtual) {
        this.nivelAtual = nivelAtual;
    }

    public double getTaxaAcertoUltimos10() {
        return taxaAcertoUltimos10;
    }

    public void setTaxaAcertoUltimos10(double taxaAcertoUltimos10) {
        this.taxaAcertoUltimos10 = taxaAcertoUltimos10;
    }

    public boolean isTeveIntervencaoManual() {
        return teveIntervencaoManual;
    }

    public void setTeveIntervencaoManual(boolean teveIntervencaoManual) {
        this.teveIntervencaoManual = teveIntervencaoManual;
    }
}