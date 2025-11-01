/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.notificacionback.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.notificacionback.assembler.ResumenOfertasAssembler;
import mx.gob.imss.dpes.notificacionback.model.NotificacionCorreo;
import mx.gob.imss.dpes.notificacionback.model.ResumenOfertas;
import mx.gob.imss.dpes.notificacionback.model.Template;
import mx.gob.imss.dpes.notificacionback.restclient.CorreoClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

/**
 * @author salvador.pocteco
 */
@Provider
public class CreateCorreoOfertasService extends ServiceDefinition<NotificacionCorreo<ResumenOfertas>, NotificacionCorreo<ResumenOfertas>> {

  @Inject
  @RestClient
  private CorreoClient client;
  
  @Inject
  private ReadTemplateService readTemplateService;

  @Inject
  private ResumenOfertasAssembler resumenOfertasAssembler;

  @Override
  public Message<NotificacionCorreo<ResumenOfertas>> execute(Message<NotificacionCorreo<ResumenOfertas>> request) throws BusinessException {

    log.log(Level.INFO, "El request general es : {0}", request.getPayload());
    Template template = request.getPayload().getTemplate();
    template.setName("/template/ofertas.html");

    readTemplateService.execute( new Message<>(template) );
    resumenOfertasAssembler.assemble(request.getPayload());

    request.getPayload().getCorreo().getCorreoPara().add(
      request.getPayload().getTipo().getRequestPageRequestModel().getModel().getEmail()
    );
    request.getPayload().getCorreo().setAsunto("Simulación de préstamo");

    Response respuesta = client.create( request.getPayload().getCorreo() );

    if (respuesta.getStatus() == 200) {      
      return new Message<>(request.getPayload());
    }
    return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
  }
}
