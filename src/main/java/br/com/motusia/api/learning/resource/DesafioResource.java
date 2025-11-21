package br.com.motusia.api.learning.resource;

import br.com.motusia.api.identity.dto.AlunoDTO;
import br.com.motusia.api.learning.dto.DesafioDto;
import br.com.motusia.api.learning.dto.DesafioRequestDto;
import br.com.motusia.api.learning.service.DesafioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/desafios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DesafioResource {

    @Inject
    DesafioService desafioService;

    @GET
    public Response listarTodasAsAula() {
        List<DesafioRequestDto> desafios = desafioService.listaTodasAsAula();
        return Response.ok(desafios).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarDesafio(@PathParam("id") Long id){
        try {
            DesafioDto desafio = desafioService.buscarDesafio(id);
            return Response.ok(desafio).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    public Response criarDesafios(DesafioRequestDto desafioRequestDto) {
        desafioService.criarDesafios(desafioRequestDto);
        return Response.status(Response.Status.CREATED).build();
    }
}