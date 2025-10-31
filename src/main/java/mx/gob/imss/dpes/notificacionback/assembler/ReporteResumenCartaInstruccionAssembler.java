/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.notificacionback.assembler;

import java.io.StringWriter;
import java.util.logging.Level;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import mx.gob.imss.dpes.baseback.assembler.BaseAssembler;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenCartaInstruccion;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenSimulacion;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Adjunto;
import mx.gob.imss.dpes.notificacionback.model.NotificacionCorreo;

/**
 *
 * @author osiris.hernandez
 */
@Provider
public class ReporteResumenCartaInstruccionAssembler extends BaseAssembler<NotificacionCorreo<ResumenCartaInstruccion>
        , NotificacionCorreo<ResumenCartaInstruccion>, Long, Long> {

  @Override
  public NotificacionCorreo<ResumenCartaInstruccion> toEntity(
          NotificacionCorreo<ResumenCartaInstruccion> source) {
    return source;
  }

  @Override
  public Long toPKEntity(Long source) {
    return source;
  }

  @Override
  public NotificacionCorreo<ResumenCartaInstruccion> assemble(
          NotificacionCorreo<ResumenCartaInstruccion> source) {
    
    String template = source.getTemplate().getContent();
    ResumenCartaInstruccion resumenCartaInstruccion = source.getTipo();
    
    template = template.replaceAll("NOMBRE_ENTIDAD_FINANCIERA", resumenCartaInstruccion.getNombreComercial());
    template = template.replaceAll("FOLIO_NEGOCIO", resumenCartaInstruccion.getFolio());
    StringBuilder nombre = new StringBuilder();
    nombre.append(resumenCartaInstruccion.getNombre());
    nombre.append(" ");
    nombre.append(resumenCartaInstruccion.getPrimerApe());
    nombre.append(" ");
    nombre.append(resumenCartaInstruccion.getSegundoApe());
    template = template.replaceAll("NOMBRE_PENSIONADO", nombre.toString() );
    template = template.replaceAll("NUMERO_SEGURIDAD_SOCIAL", resumenCartaInstruccion.getNss());

    
    source.getCorreo().setCuerpoCorreo(template);
    
    String xml =  obtenerXML(resumenCartaInstruccion);
    if( source.isAddXML() ){
      Adjunto adjunto = new Adjunto();
      adjunto.setNombreAdjunto("ReporteResumenCartaInstruccion.xml");
      adjunto.setAdjuntoBase64( xml.getBytes() );
      source.getCorreo().getAdjuntos().add(adjunto);
    }
    return source;
  }

  @Override
  public Long assemblePK(Long source) {
    return source;
  }
  
  private String obtenerXML(ResumenCartaInstruccion resumenCartaInstruccion){
    String xml = "";
    try {
        JAXBContext jaxbContext = JAXBContext.newInstance(ResumenCartaInstruccion.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(resumenCartaInstruccion, sw);        
        xml = sw.toString() ;            
        
      } catch (JAXBException ex) {
        log.log(Level.SEVERE, null, ex);
      }
    return xml;
  }

   
}
