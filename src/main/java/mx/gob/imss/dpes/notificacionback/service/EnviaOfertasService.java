package mx.gob.imss.dpes.notificacionback.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.Oferta;
import mx.gob.imss.dpes.notificacionback.model.NotificacionCorreo;
import mx.gob.imss.dpes.notificacionback.model.ResumenOfertas;
import mx.gob.imss.dpes.notificacionback.restclient.OfertaClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.logging.Level;
import javax.ws.rs.core.GenericType;

@Provider
public class EnviaOfertasService extends ServiceDefinition<NotificacionCorreo<ResumenOfertas>, NotificacionCorreo<ResumenOfertas>> {

  @Inject
  @RestClient
  OfertaClient ofertaClient;

  @Inject
  CreateCorreoOfertasService correoOfertasService;

  @Override
  public Message<NotificacionCorreo<ResumenOfertas>> execute(Message<NotificacionCorreo<ResumenOfertas>> request)
      throws BusinessException {
    log.log(Level.INFO,"Pidiendo ofertas...");
    Response response = ofertaClient.load(request.getPayload().getTipo().getRequestPageRequestModel());
    if (response.getStatus() == 200){

      List<Oferta> ofertas = response.readEntity(new GenericType<List<Oferta>>() {});
      request.getPayload().getTipo().setOfertas(ofertas);
      return correoOfertasService.execute(request);
    }
    return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
  }
}