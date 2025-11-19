package br.com.motusia.api.esg.dto;

import java.math.BigDecimal;

/**
 * DTO para transportar os dados agregados do Dashboard Executivo ESG.
 * Os campos correspondem às colunas da VIEW VW_ESG_DASHBOARD.
 */
public class EsgDashboardDto {

    private String nomePatrocinador;
    private Long idPatrocinador;
    private Long totalAlunosImpactados;
    private BigDecimal percentualReducaoSkillsGap;
    private BigDecimal horasMediasUsoPlataforma;
    private BigDecimal taxaRetencaoAlunos;
    private BigDecimal roiSocial;
    private String odsAlinhados;

    public String getNomePatrocinador() {
        return nomePatrocinador;
    }

    public void setNomePatrocinador(String nomePatrocinador) {
        this.nomePatrocinador = nomePatrocinador;
    }

    public Long getIdPatrocinador() {
        return idPatrocinador;
    }

    public void setIdPatrocinador(Long idPatrocinador) {
        this.idPatrocinador = idPatrocinador;
    }

    public Long getTotalAlunosImpactados() {
        return totalAlunosImpactados;
    }

    public void setTotalAlunosImpactados(Long totalAlunosImpactados) {
        this.totalAlunosImpactados = totalAlunosImpactados;
    }

    public BigDecimal getPercentualReducaoSkillsGap() {
        return percentualReducaoSkillsGap;
    }

    public void setPercentualReducaoSkillsGap(BigDecimal percentualReducaoSkillsGap) {
        this.percentualReducaoSkillsGap = percentualReducaoSkillsGap;
    }

    public BigDecimal getHorasMediasUsoPlataforma() {
        return horasMediasUsoPlataforma;
    }

    public void setHorasMediasUsoPlataforma(BigDecimal horasMediasUsoPlataforma) {
        this.horasMediasUsoPlataforma = horasMediasUsoPlataforma;
    }

    public BigDecimal getTaxaRetencaoAlunos() {
        return taxaRetencaoAlunos;
    }

    public void setTaxaRetencaoAlunos(BigDecimal taxaRetencaoAlunos) {
        this.taxaRetencaoAlunos = taxaRetencaoAlunos;
    }

    public BigDecimal getRoiSocial() {
        return roiSocial;
    }

    public void setRoiSocial(BigDecimal roiSocial) {
        this.roiSocial = roiSocial;
    }

    public String getOdsAlinhados() {
        return odsAlinhados;
    }

    public void setOdsAlinhados(String odsAlinhados) {
        this.odsAlinhados = odsAlinhados;
    }
}