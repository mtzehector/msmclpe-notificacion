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
import mx.gob.imss.dpes.common.enums.OrigenSolicitudEnum;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenCartaInstruccion;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Adjunto;
import mx.gob.imss.dpes.notificacionback.assembler.ReporteResumenCartaInstruccionAssembler;
import mx.gob.imss.dpes.notificacionback.model.NotificacionCorreo;
import mx.gob.imss.dpes.notificacionback.model.Template;
import mx.gob.imss.dpes.notificacionback.restclient.CorreoClient;

/**
 *
 * @author osiris.hernandez
 */
@Provider
public class CreateCorreoReporteResumenCartaInstruccionService extends ServiceDefinition<NotificacionCorreo<ResumenCartaInstruccion>, NotificacionCorreo<ResumenCartaInstruccion>> {

    @Inject
    @RestClient
    private CorreoClient client;

    @Inject
    private ReadTemplateService readTemplateService;

    @Inject
    ReporteResumenCartaInstruccionAssembler reporteResumenCartaInstruccionAssembler;

    @Override
    public Message<NotificacionCorreo<ResumenCartaInstruccion>> execute(Message<NotificacionCorreo<ResumenCartaInstruccion>> request) throws BusinessException {
        
        log.log(Level.INFO, "El request general es : {0}", request.getPayload().getTipo());
        Template templateCorreoEntidad = request.getPayload().getTemplate();        
        templateCorreoEntidad.setName(request.getPayload().getTipo().getOrigen().equals(OrigenSolicitudEnum.SIMULACION.getDescripcion())?"/template/correo7.html":"/template/correo4.html");
        readTemplateService.execute(new Message<>(templateCorreoEntidad));
        reporteResumenCartaInstruccionAssembler.assemble(request.getPayload());
        request.getPayload().getCorreo().getCorreoPara().add(
                request.getPayload().getTipo().getCorreoElectronico()
        );
        log.log(Level.INFO, "El correo de la entidad financiera es: {0}", request.getPayload().getCorreo());
        
        request.getPayload().getCorreo().setAsunto(request.getPayload().getTipo().getOrigen().equals(OrigenSolicitudEnum.SIMULACION.getDescripcion())?"Carta de Libranza":"Autorización Carta de Instrucción ID " + request.getPayload().getTipo().getFolio());

        log.log(Level.INFO, "El request hacia correo electronico es : {0}", request.getPayload().getCorreo());
        Response respuestaCorreoEntidad = client.create(request.getPayload().getCorreo());

        if (respuestaCorreoEntidad.getStatus() == 200) {

            log.log(Level.INFO, "El request general es : {0}", request.getPayload());
            Template templatePensionado = request.getPayload().getTemplate();
            templatePensionado.setName(request.getPayload().getTipo().getOrigen().equals(OrigenSolicitudEnum.SIMULACION.getDescripcion())?"/template/correo6.html":"/template/correo3.html");
            request.getPayload().setAddXML(false);
            readTemplateService.execute(new Message<>(templatePensionado));
            reporteResumenCartaInstruccionAssembler.assemble(request.getPayload());
            
            request.getPayload().getCorreo().getCorreoPara().clear();
            request.getPayload().getCorreo().getCorreoPara().add(
                    request.getPayload().getTipo().getEmail()
            );
            log.log(Level.INFO, "El correo del pensionado es: {0}", request.getPayload().getCorreo());
            request.getPayload().getCorreo().setAsunto(request.getPayload().getTipo().getOrigen().equals(OrigenSolicitudEnum.SIMULACION.getDescripcion())?"Carta de Libranza":"Autorización Carta de Instrucción ID " + request.getPayload().getTipo().getFolio());
            List<Adjunto> adjunto = new ArrayList<>();
            adjunto.add(request.getPayload().getCorreo().getAdjuntos().get(0));
            
            request.getPayload().getCorreo().setAdjuntos(adjunto);
            log.log(Level.INFO,"Adjuntos {0}", request.getPayload().getCorreo().getAdjuntos().size() );
            Response respuestaCorreoPensionado = client.create(request.getPayload().getCorreo());
            if (respuestaCorreoPensionado.getStatus() == 200) {
                return new Message<>(request.getPayload());
            }
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
    }
}
