/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.notificacionback.service;

import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.capacidadcredito.model.CartaCapacidadCredito;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.notificacionback.model.NotificacionCorreo;
import mx.gob.imss.dpes.notificacionback.restclient.DocumentoClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class ReadResumenCapacidadService extends ServiceDefinition<NotificacionCorreo<CartaCapacidadCredito>, NotificacionCorreo<CartaCapacidadCredito>> {

    @Inject
    @RestClient
    private DocumentoClient client;

    @Override
    public Message<NotificacionCorreo<CartaCapacidadCredito>> execute(Message<NotificacionCorreo<CartaCapacidadCredito>> request) throws BusinessException {
        
        log.log( Level.INFO, "Buscando el resumen de la solicitud: {0}", request.getPayload().getIdSolicitud() );
        
        Documento documento = new Documento();
        documento.setCveSolicitud( request.getPayload().getIdSolicitud() );
        documento.setTipoDocumento( TipoDocumentoEnum.CARTA_CAPACIDAD_CREDITO);
        
        Response response = client.loadRefDocumento(documento);
        
        if( response.getStatus() == 200 ){
          CartaCapacidadCredito cartaCapacidadCredito = response.readEntity( CartaCapacidadCredito.class );
          log.log( Level.INFO, "Resumen obtenido: {0}", cartaCapacidadCredito);
          request.getPayload().setTipo(cartaCapacidadCredito);
          return request;          
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
    }

}
