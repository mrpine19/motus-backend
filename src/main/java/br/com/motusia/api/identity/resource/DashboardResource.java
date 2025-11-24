package br.com.motusia.api.identity.resource;

import br.com.motusia.api.identity.service.DashboardService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/dashboard")
@Produces(MediaType.APPLICATION_JSON)
public class DashboardResource {

    @Inject
    DashboardService dashboardService;

    @GET
    @Path("/voluntario/{idVoluntario}")
    public Response getDashboardVoluntario(@PathParam("idVoluntario") Long idVoluntario) {
        try {
            return Response.ok(dashboardService.getDashboardData(idVoluntario)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao processar o dashboard: " + e.getMessage()).build();
        }
    }
}