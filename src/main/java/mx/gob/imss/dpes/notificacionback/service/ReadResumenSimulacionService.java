/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.notificacionback.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenSimulacion;
import mx.gob.imss.dpes.notificacionback.model.NotificacionCorreo;
import mx.gob.imss.dpes.notificacionback.restclient.DocumentoClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class ReadResumenSimulacionService extends ServiceDefinition<NotificacionCorreo<ResumenSimulacion>, NotificacionCorreo<ResumenSimulacion>> {

    @Inject
    @RestClient
    private DocumentoClient client;

    @Override
    public Message<NotificacionCorreo<ResumenSimulacion>> execute(Message<NotificacionCorreo<ResumenSimulacion>> request) throws BusinessException {
        
        log.log( Level.INFO, "Buscando el resumen de la solicitud: {0}", request.getPayload().getIdSolicitud() );
        
        Documento documento = new Documento();
        documento.setCveSolicitud( request.getPayload().getIdSolicitud() );
        documento.setTipoDocumento( TipoDocumentoEnum.RESUMEN_SIMULACION );
        
        Response response = client.loadRefDocumento(documento);
        
        if( response.getStatus() == 200 ){
          ResumenSimulacion resumenSimulacion = response.readEntity( ResumenSimulacion.class );
          log.log( Level.INFO, "Resumen obtenido: {0}", resumenSimulacion );
          request.getPayload().setTipo(resumenSimulacion);
          return request;          
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
    }

}
