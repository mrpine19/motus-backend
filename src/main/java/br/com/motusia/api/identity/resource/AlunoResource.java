package br.com.motusia.api.identity.resource;

import br.com.motusia.api.identity.dto.AjusteNivelDto;
import br.com.motusia.api.identity.dto.AlunoCreateDTO;
import br.com.motusia.api.identity.dto.AlunoUpdateDTO;
import br.com.motusia.api.identity.model.Aluno;
import br.com.motusia.api.identity.service.AlunoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/alunos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlunoResource {

    @Inject
    AlunoService alunoService;

    @POST
    public Response criarAluno(AlunoCreateDTO dto) {
        Aluno aluno = alunoService.criarAluno(dto);
        return Response.status(Response.Status.CREATED).entity(aluno).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizarAluno(@PathParam("id") Long id, AlunoUpdateDTO dto) {
        Aluno aluno = alunoService.atualizarAluno(id, dto);
        return Response.ok(aluno).build();
    }

    @DELETE
    @Path("/{id}")
    public Response inativarAluno(@PathParam("id") Long id) {
        alunoService.inativandoAluno(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{alunoId}/ajustar-nivel/voluntario/{voluntarioId}")
    public Response ajustarNivelManualmente(
            @PathParam("alunoId") Long alunoId,
            @PathParam("voluntarioId") Long voluntarioId,
            AjusteNivelDto dto) {

        alunoService.ajustarNivelManualmente(alunoId, voluntarioId, dto);
        return Response.ok().entity("Nível do aluno ajustado com sucesso.").build();
    }
}