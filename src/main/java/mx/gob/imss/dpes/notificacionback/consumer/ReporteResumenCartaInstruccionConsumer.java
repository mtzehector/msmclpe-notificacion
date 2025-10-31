/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.notificacionback.consumer;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import mx.gob.imss.dpes.common.consumer.BaseConsumer;
import mx.gob.imss.dpes.common.enums.EventEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenCartaInstruccion;
import mx.gob.imss.dpes.notificacionback.model.NotificacionCorreo;
import mx.gob.imss.dpes.notificacionback.service.CreateCorreoReporteResumenCartaInstruccionService;
import mx.gob.imss.dpes.notificacionback.service.ReadResumenCartaInstruccionService;
import mx.gob.imss.dpes.notificacionback.service.ReadReporteResumenCartaInstruccionService;
/**
 *
 * @author osiris.hernandez
 */
@MessageDriven(name = "ReporteResumenCartaInstruccionConsumer", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "topic/MCLPETopic"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})

public class ReporteResumenCartaInstruccionConsumer extends BaseConsumer {
  
  @Inject ReadResumenCartaInstruccionService readDocumentoService;
  @Inject ReadReporteResumenCartaInstruccionService readReporteResumenCartaInstruccionService;
  @Inject CreateCorreoReporteResumenCartaInstruccionService createCorreoReporteResumenCartaInstruccionService;

  @Override
  protected void proccess(Message message) {
    
    ServiceDefinition[] steps = { readDocumentoService, readReporteResumenCartaInstruccionService,createCorreoReporteResumenCartaInstruccionService };
    
    if( EventEnum.CREAR_REPORTE_CARTA_INSTRUCCION.equals( message.getHeader().getEvent()) ){
      try {
        NotificacionCorreo<ResumenCartaInstruccion> notificacion = new NotificacionCorreo<>();
        notificacion.setTipo( new ResumenCartaInstruccion() );
        notificacion.setIdSolicitud( (Long) message.getPayload() );
        createCorreoReporteResumenCartaInstruccionService.executeSteps(steps, new Message<>(notificacion) );
      } catch (BusinessException ex) {
        log.log(Level.SEVERE, null, ex);
      }
    }
      
  }
}

