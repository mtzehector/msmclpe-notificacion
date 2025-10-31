/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.notificacionback.service;

import java.util.ArrayList;
import java.util.List;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenSimulacion;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Adjunto;
import mx.gob.imss.dpes.notificacionback.assembler.ReporteResumenSimulacionAssembler;
import mx.gob.imss.dpes.notificacionback.model.NotificacionCorreo;
import mx.gob.imss.dpes.notificacionback.model.Template;
import mx.gob.imss.dpes.notificacionback.restclient.CorreoClient;

/**
 * @author salvador.pocteco
 */
@Provider
public class CreateCorreoReporteResumenSimulacionService extends ServiceDefinition<NotificacionCorreo<ResumenSimulacion>, NotificacionCorreo<ResumenSimulacion>> {

  @Inject
  @RestClient
  private CorreoClient client;
  
  @Inject
  private ReadTemplateService readTemplateService;

  @Inject
  ReporteResumenSimulacionAssembler reporteResumenSimulacionAssembler;

  @Override
  public Message<NotificacionCorreo<ResumenSimulacion>> execute(Message<NotificacionCorreo<ResumenSimulacion>> request) throws BusinessException {

    log.log(Level.INFO, "El request general es : {0}", request.getPayload());
    Template template = request.getPayload().getTemplate();
    template.setName("/template/correo1.html");
    
    readTemplateService.execute( new Message<>(template) );
    
    reporteResumenSimulacionAssembler.assemble(request.getPayload());
    
    
    request.getPayload().getCorreo().getCorreoPara().add(
      request.getPayload().getTipo().getEmail()
    );
    
    request.getPayload().getCorreo().setAsunto("Simulación de préstamo ID "+request.getPayload().getTipo().getFolio());
    List<Adjunto> adjunto = new ArrayList<>();
    adjunto.add(request.getPayload().getCorreo().getAdjuntos().get(0));       
    request.getPayload().getCorreo().setAdjuntos(adjunto);  
    Response respuesta = client.create( request.getPayload().getCorreo() );

    if (respuesta.getStatus() == 200) {      
      return new Message<>(request.getPayload());
    }
    return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
  }
}
