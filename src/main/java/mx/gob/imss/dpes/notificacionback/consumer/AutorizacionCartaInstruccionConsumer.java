/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.notificacionback.consumer;

import mx.gob.imss.dpes.common.consumer.BaseConsumer;
import mx.gob.imss.dpes.common.enums.EventEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenCartaInstruccion;
import mx.gob.imss.dpes.notificacionback.model.NotificacionCorreo;
import mx.gob.imss.dpes.notificacionback.service.*;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import java.util.logging.Level;

/**
 *
 * @author osiris.hernandez
 */
@MessageDriven(name = "AutorizacionCartaInstruccionConsumer", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "topic/MCLPETopic"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})

public class AutorizacionCartaInstruccionConsumer extends BaseConsumer {
  
  @Inject ReadResumenCartaInstruccionService readDocumentoService;
  @Inject ReadDocumentosAutorizacionService readDocumentosAutorizacionService;
  @Inject CreateCorreoAutorizacionService createCorreoAutorizacionService;

  @Override
  protected void proccess(Message message) {
    
    ServiceDefinition[] steps =
        { readDocumentoService,readDocumentosAutorizacionService,createCorreoAutorizacionService };
    
    if( EventEnum.CREAR_CORREO_AUTORIZACION_CARTA_INSTRUCCION.equals( message.getHeader().getEvent()) ){
      try {
        NotificacionCorreo<ResumenCartaInstruccion> notificacion = new NotificacionCorreo<>();
        notificacion.setTipo( new ResumenCartaInstruccion() );
        notificacion.setIdSolicitud( (Long) message.getPayload() );
        createCorreoAutorizacionService.executeSteps(steps, new Message<>(notificacion) );
      } catch (BusinessException ex) {
        log.log(Level.SEVERE, null, ex);
      }
    }
      
  }
}

