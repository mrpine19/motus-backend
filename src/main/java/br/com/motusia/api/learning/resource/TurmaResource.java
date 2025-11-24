package br.com.motusia.api.learning.resource;

import br.com.motusia.api.learning.dto.TurmaCreateDTO;
import br.com.motusia.api.learning.dto.TurmaDTO;
import br.com.motusia.api.learning.model.Turma;
import br.com.motusia.api.learning.service.TurmaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/turmas")
@Produces(MediaType.APPLICATION_JSON)
public class TurmaResource {

    @Inject
    TurmaService turmaService;

    @GET
    public Response listarTurmas() {
        List<TurmaDTO> turmas = turmaService.listaTodasAsTurmas();
        return Response.ok(turmas).build();
    }

    @POST
    public Response adicionarTurma(TurmaCreateDTO turmaCreateDTO) {
        try {
            TurmaDTO turmaDTO = turmaService.criarTurma(turmaCreateDTO);
            return Response.ok(turmaDTO).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}