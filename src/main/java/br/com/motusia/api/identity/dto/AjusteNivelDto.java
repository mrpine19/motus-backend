package br.com.motusia.api.identity.dto;

public class AjusteNivelDto {
    private Long novoNivelId;
    private String justificativa;

    public Long getNovoNivelId() {
        return novoNivelId;
    }

    public void setNovoNivelId(Long novoNivelId) {
        this.novoNivelId = novoNivelId;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
}