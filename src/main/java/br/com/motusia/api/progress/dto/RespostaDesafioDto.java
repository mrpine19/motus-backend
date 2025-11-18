package br.com.motusia.api.progress.dto;

public class RespostaDesafioDto {

    private Long desafioId;
    private String respostaSubmetida;
    private Double tempoGastoSegundos;

    public Long getDesafioId() {
        return desafioId;
    }

    public void setDesafioId(Long desafioId) {
        this.desafioId = desafioId;
    }

    public String getRespostaSubmetida() {
        return respostaSubmetida;
    }

    public void setRespostaSubmetida(String respostaSubmetida) {
        this.respostaSubmetida = respostaSubmetida;
    }

    public Double getTempoGastoSegundos() {
        return tempoGastoSegundos;
    }

    public void setTempoGastoSegundos(Double tempoGastoSegundos) {
        this.tempoGastoSegundos = tempoGastoSegundos;
    }
}