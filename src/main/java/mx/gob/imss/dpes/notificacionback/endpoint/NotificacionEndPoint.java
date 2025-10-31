package mx.gob.imss.dpes.notificacionback.endpoint;

import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.PageRequestModel;
import mx.gob.imss.dpes.notificacionback.entity.McltNotificacion;
import mx.gob.imss.dpes.notificacionback.model.NotificacionCorreo;
import mx.gob.imss.dpes.notificacionback.model.OfertaRequest;
import mx.gob.imss.dpes.notificacionback.model.ResumenOfertas;
import mx.gob.imss.dpes.notificacionback.service.EnviaOfertasService;
import mx.gob.imss.dpes.notificacionback.service.NotificacionPersistenceService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/notificacion")
@ApplicationScoped
public class NotificacionEndPoint extends BaseGUIEndPoint<McltNotificacion, McltNotificacion, McltNotificacion> {

  @Context
  private UriInfo uriInfo;

  @Inject
  private NotificacionPersistenceService service;

  @Inject
  private EnviaOfertasService enviaOfertasService;

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/buscar")
  @Override
  public Response load(McltNotificacion notificacion) throws BusinessException {
    List<McltNotificacion> list = service.load(new Message<>(notificacion));
    return Response.ok( list ).build();
  }

  @GET
  @Path("/{cveSolicitud}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response loadAll(@PathParam("cveSolicitud") Long cveSolicitud) throws BusinessException {
    return Response.ok( service.loadAll(cveSolicitud) ).build();
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Override
  public Response update(McltNotificacion notificacion) throws BusinessException {
    Message<McltNotificacion> execute = service.update(new Message<>(notificacion));
    return toResponse(execute);
  }

  @POST
  @Path("/enviarOfertas")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response enviarOfertas(PageRequestModel<OfertaRequest> request)
      throws BusinessException {

    NotificacionCorreo<ResumenOfertas> notificacion = new NotificacionCorreo<>();
    notificacion.setTipo(new ResumenOfertas());
    notificacion.getTipo().setRequestPageRequestModel(request);
    return Response.ok(enviaOfertasService.execute(new Message<>(notificacion))).build();

  }
  
  @GET
  @Path("getByFolio/{folioNotificacion}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllByFolio(@PathParam("folioNotificacion") String folioNotificacion) 
          throws BusinessException {
    return Response.ok(service.getAllByFolio(folioNotificacion)).build();
  }
}