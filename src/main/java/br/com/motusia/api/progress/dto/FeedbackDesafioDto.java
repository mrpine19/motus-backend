package br.com.motusia.api.progress.dto;

public class FeedbackDesafioDto {

    private boolean acertou;
    private String feedback;
    private int pontosGanhos;
    private int novaStreak;
    private long totalDesafiosConcluidos;

    public FeedbackDesafioDto(boolean acertou, String feedback, int pontosGanhos, int novaStreak, long totalDesafiosConcluidos) {
        this.acertou = acertou;
        this.feedback = feedback;
        this.pontosGanhos = pontosGanhos;
        this.novaStreak = novaStreak;
        this.totalDesafiosConcluidos = totalDesafiosConcluidos;
    }

    public boolean isAcertou() {
        return acertou;
    }

    public void setAcertou(boolean acertou) {
        this.acertou = acertou;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getPontosGanhos() {
        return pontosGanhos;
    }

    public void setPontosGanhos(int pontosGanhos) {
        this.pontosGanhos = pontosGanhos;
    }

    public int getNovaStreak() {
        return novaStreak;
    }

    public void setNovaStreak(int novaStreak) {
        this.novaStreak = novaStreak;
    }

    public long getTotalDesafiosConcluidos() {
        return totalDesafiosConcluidos;
    }

    public void setTotalDesafiosConcluidos(long totalDesafiosConcluidos) {
        this.totalDesafiosConcluidos = totalDesafiosConcluidos;
    }
}