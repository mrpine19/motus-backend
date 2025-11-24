package br.com.motusia.api.progress.resource;

import br.com.motusia.api.progress.dto.RespostaDesafioDto;
import br.com.motusia.api.progress.service.PontuacaoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/desafios/submeter/{alunoId}")
@Produces(MediaType.APPLICATION_JSON)
public class PontuacaoResource {

    @Inject
    PontuacaoService pontuacaoService;

    @POST
    public Response submeterResposta(@PathParam("alunoId") Long alunoId, RespostaDesafioDto respostaDto) {
        try {
            return Response.ok(pontuacaoService.submeterResposta(alunoId, respostaDto)).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}