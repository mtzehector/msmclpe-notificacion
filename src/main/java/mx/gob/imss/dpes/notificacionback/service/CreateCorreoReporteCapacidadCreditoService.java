/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.notificacionback.service;

import java.util.ArrayList;
import java.util.List;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.capacidadcredito.model.CartaCapacidadCredito;
import mx.gob.imss.dpes.notificacionback.assembler.ReporteCapacidadCreditoAssembler;
import mx.gob.imss.dpes.notificacionback.model.NotificacionCorreo;
import mx.gob.imss.dpes.notificacionback.model.Template;
import mx.gob.imss.dpes.notificacionback.restclient.CorreoClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Adjunto;

/**
 * @author salvador.pocteco
 */
@Provider
public class CreateCorreoReporteCapacidadCreditoService extends ServiceDefinition<NotificacionCorreo<CartaCapacidadCredito>, NotificacionCorreo<CartaCapacidadCredito>> {

  @Inject
  @RestClient
  private CorreoClient client;
  
  @Inject
  private ReadTemplateService readTemplateService;

  @Inject
  ReporteCapacidadCreditoAssembler reporteCapacidadCreditoAssembler;

  @Override
  public Message<NotificacionCorreo<CartaCapacidadCredito>> execute(Message<NotificacionCorreo<CartaCapacidadCredito>> request) throws BusinessException {

    log.log(Level.INFO, "El request general es : {0}", request.getPayload());
    Template template = request.getPayload().getTemplate();
    template.setName("/template/correo5.html");
    
    readTemplateService.execute( new Message<>(template) );

    reporteCapacidadCreditoAssembler.assemble(request.getPayload());
    
    
    request.getPayload().getCorreo().getCorreoPara().add(
      request.getPayload().getTipo().getEmail()
    );
    
    request.getPayload().getCorreo().setAsunto("Carta Capacidad de Crédito");
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
