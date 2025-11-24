package br.com.motusia.api.progress.resource;

import br.com.motusia.api.progress.service.RankingService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/ranking")
@Produces(MediaType.APPLICATION_JSON)
public class RankingResource {

    @Inject
    RankingService rankingService;

    @GET
    @Path("/turma/{idTurma}")
    public Response getRankingDaTurma(@PathParam("idTurma") Long idTurma) {
        try {
            return Response.ok(rankingService.calcularRankingSemanal(idTurma)).build();
        } catch (Exception e) {
            return Response.serverError().entity("Erro ao calcular o ranking: " + e.getMessage()).build();
        }
    }
}