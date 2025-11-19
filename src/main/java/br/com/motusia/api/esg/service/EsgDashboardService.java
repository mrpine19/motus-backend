package br.com.motusia.api.esg.service;

import br.com.motusia.api.esg.dto.EsgDashboardDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.ws.rs.NotFoundException;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class EsgDashboardService {

    @PersistenceContext
    EntityManager em;

    /**
     * Consulta os dados da VIEW SQL 'VW_ESG_DASHBOARD' para um patrocinador específico.
     * @param idPatrocinador O ID do patrocinador para filtrar os dados.
     * @return Um DTO com todos os KPIs para o dashboard executivo.
     */
    public EsgDashboardDto getEsgDashboardData(Long idPatrocinador) {
        Query query = em.createNativeQuery("SELECT * FROM VW_ESG_DASHBOARD WHERE id_patrocinador = ?1");
        query.setParameter(1, idPatrocinador);

        List<Object[]> result = query.getResultList();

        if (result.isEmpty()) {
            throw new NotFoundException("Nenhum dado encontrado no Dashboard ESG para o patrocinador informado.");
        }

        Object[] data = result.getFirst();
        EsgDashboardDto dto = new EsgDashboardDto();

        dto.setNomePatrocinador((String) data[0]);
        dto.setIdPatrocinador(((Number) data[1]).longValue());
        dto.setTotalAlunosImpactados(((Number) data[2]).longValue());
        dto.setPercentualReducaoSkillsGap(toBigDecimal(data[3]));
        dto.setHorasMediasUsoPlataforma(toBigDecimal(data[4]));
        dto.setTaxaRetencaoAlunos(toBigDecimal(data[5]));
        dto.setRoiSocial(toBigDecimal(data[6]));
        dto.setOdsAlinhados((String) data[7]);

        return dto;
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Number) {
            return new BigDecimal(value.toString());
        }
        return BigDecimal.ZERO;
    }
}