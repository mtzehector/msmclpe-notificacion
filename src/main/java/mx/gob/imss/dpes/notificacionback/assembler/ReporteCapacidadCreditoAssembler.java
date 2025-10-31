/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.notificacionback.assembler;

import java.io.StringWriter;
import java.util.logging.Level;
import mx.gob.imss.dpes.baseback.assembler.BaseAssembler;
import mx.gob.imss.dpes.interfaces.capacidadcredito.model.CartaCapacidadCredito;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenSimulacion;
import mx.gob.imss.dpes.notificacionback.model.NotificacionCorreo;

import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import mx.gob.imss.dpes.interfaces.capacidadcredito.model.CapacidadCredito;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Adjunto;

/**
 *
 * @author Diego Velazquez
 */
@Provider
public class ReporteCapacidadCreditoAssembler extends BaseAssembler<NotificacionCorreo<CartaCapacidadCredito>
        , NotificacionCorreo<CartaCapacidadCredito>, Long, Long> {
  public static final String BLANK = "&nbsp;";

  @Override
  public NotificacionCorreo<CartaCapacidadCredito> toEntity(
          NotificacionCorreo<CartaCapacidadCredito> source) {
    return source;
  }

  @Override
  public Long toPKEntity(Long source) {
    return source;
  }

  @Override
  public NotificacionCorreo<CartaCapacidadCredito> assemble(
          NotificacionCorreo<CartaCapacidadCredito> source) {

    String template = source.getTemplate().getContent();
    CartaCapacidadCredito cartaCapacidadCredito = source.getTipo();

    StringBuilder nombre = new StringBuilder();
    nombre.append(cartaCapacidadCredito.getNombre());
    nombre.append(BLANK);
    nombre.append(cartaCapacidadCredito.getPrimerApe());
    nombre.append(BLANK);
    nombre.append(cartaCapacidadCredito.getSegundoApe());
    
    template = template.replaceAll("NOMBRE_PENSIONADO", nombre.toString());
    template = template.replaceAll("NUMERO_SEGURIDAD_SOCIAL", cartaCapacidadCredito.getNss());
    template = template.replaceAll("CLAVE_UNICA", cartaCapacidadCredito.getCurp());
    template = template.replaceAll("FOLIO_NEGOCIO", cartaCapacidadCredito.getFolio());
    template = template.replaceAll("CONTRASENA",  cartaCapacidadCredito.getCurp().substring(4, 10));

    source.getCorreo().setCuerpoCorreo(template);
    
    String xml =  obtenerXML(cartaCapacidadCredito);
    
    Adjunto adjunto = new Adjunto();
    adjunto.setNombreAdjunto("ReporteCapacidadCredito.xml");
    adjunto.setAdjuntoBase64( xml.getBytes() );
    source.getCorreo().getAdjuntos().add(adjunto);

    return source;
  }

  @Override
  public Long assemblePK(Long source) {
    return source;
  }

  
  private String obtenerXML(CartaCapacidadCredito cartaCapacidadCredito){
    String xml = "";
    try {
        JAXBContext jaxbContext = JAXBContext.newInstance(CartaCapacidadCredito.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(cartaCapacidadCredito, sw);        
        xml = sw.toString() ;            
        
      } catch (JAXBException ex) {
        log.log(Level.SEVERE, null, ex);
      }
    return xml;
  }
   
}
