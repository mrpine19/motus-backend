package br.com.motusia.api.esg.resource;

import br.com.motusia.api.esg.service.EsgDashboardService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/dashboard/esg")
@Produces(MediaType.APPLICATION_JSON)
public class EsgDashboardResource {

    @Inject
    EsgDashboardService esgDashboardService;

    @GET
    @Path("/{idPatrocinador}")
    public Response getEsgDashboard(@PathParam("idPatrocinador") Long idPatrocinador) {
        try {
            return Response.ok(esgDashboardService.getEsgDashboardData(idPatrocinador)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar dados do Dashboard ESG: " + e.getMessage()).build();
        }
    }
}