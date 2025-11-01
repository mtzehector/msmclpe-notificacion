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
import mx.gob.imss.dpes.interfaces.capacidadcredito.model.CartaCapacidadCredito;
import mx.gob.imss.dpes.notificacionback.model.NotificacionCorreo;
import mx.gob.imss.dpes.notificacionback.service.CreateCorreoReporteCapacidadCreditoService;
import mx.gob.imss.dpes.notificacionback.service.ReadReporteCartaCapacidadService;
import mx.gob.imss.dpes.notificacionback.service.ReadResumenCapacidadService;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import java.util.logging.Level;

/**
 *
 * @author antonio
 */
@MessageDriven(name = "ReporteCapacidadCreditoConsumer", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "topic/MCLPETopic"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})

public class ReporteCapacidadCreditoConsumer extends BaseConsumer {
  
  @Inject ReadResumenCapacidadService readDocumentoService;
  @Inject ReadReporteCartaCapacidadService readReporteCartaCapacidadService;
  @Inject CreateCorreoReporteCapacidadCreditoService createCorreoReporteCapacidadCreditoService;

  @Override
  protected void proccess(Message message) {
    
    ServiceDefinition[] steps = {
        readDocumentoService,
        readReporteCartaCapacidadService,
        createCorreoReporteCapacidadCreditoService
    };
    
    if( EventEnum.CREAR_REPORTE_CAPACIDAD_CREDITO.equals( message.getHeader().getEvent()) ){
      try {
        NotificacionCorreo<CartaCapacidadCredito> notificacion = new NotificacionCorreo<>();
        notificacion.setTipo( new CartaCapacidadCredito() );
        notificacion.setIdSolicitud( (Long) message.getPayload() );
        createCorreoReporteCapacidadCreditoService.executeSteps(steps, new Message<>(notificacion) );
      } catch (BusinessException ex) {
        log.log(Level.SEVERE, null, ex);
      }
    }
      
  }
}
