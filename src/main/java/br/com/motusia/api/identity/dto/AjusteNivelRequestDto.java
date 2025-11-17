package br.com.motusia.api.identity.dto;

public class AjusteNivelRequestDto {

    private Long alunoId;
    private Long novoNivelId;
    private Long voluntarioId;
    private String justificativa;

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Long getNovoNivelId() {
        return novoNivelId;
    }

    public void setNovoNivelId(Long novoNivelId) {
        this.novoNivelId = novoNivelId;
    }

    public Long getVoluntarioId() {
        return voluntarioId;
    }

    public void setVoluntarioId(Long voluntarioId) {
        this.voluntarioId = voluntarioId;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
}