package br.com.motusia.api.identity.dto;

public class DificuldadeAreaDto {
    private String areaCompetencia;
    private double taxaErro;

    public DificuldadeAreaDto(String areaCompetencia, double taxaErro) {
        this.areaCompetencia = areaCompetencia;
        this.taxaErro = taxaErro;
    }

    public String getAreaCompetencia() {
        return areaCompetencia;
    }

    public void setAreaCompetencia(String areaCompetencia) {
        this.areaCompetencia = areaCompetencia;
    }

    public double getTaxaErro() {
        return taxaErro;
    }

    public void setTaxaErro(double taxaErro) {
        this.taxaErro = taxaErro;
    }
}