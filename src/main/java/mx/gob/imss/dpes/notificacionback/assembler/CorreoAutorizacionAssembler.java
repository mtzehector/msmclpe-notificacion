/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.notificacionback.assembler;

import mx.gob.imss.dpes.baseback.assembler.BaseAssembler;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenCartaInstruccion;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Adjunto;
import mx.gob.imss.dpes.notificacionback.model.NotificacionCorreo;

import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.logging.Level;

/**
 *
 * @author osiris.hernandez
 */
@Provider
public class CorreoAutorizacionAssembler extends BaseAssembler<NotificacionCorreo<ResumenCartaInstruccion>
        , NotificacionCorreo<ResumenCartaInstruccion>, Long, Long> {

  public static final String BLANK = "&nbsp;";

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

    StringBuilder nombre = new StringBuilder();
    nombre.append(resumenCartaInstruccion.getNombre());
    nombre.append(BLANK);
    nombre.append(resumenCartaInstruccion.getPrimerApe());
    nombre.append(BLANK);
    nombre.append(resumenCartaInstruccion.getSegundoApe());

    template = template.replaceAll("NOMBRE_PENSIONADO", nombre.toString());
    template = template.replaceAll("NOMBRE_ENTIDAD_FINANCIERA", resumenCartaInstruccion.getNombreComercial());
    template = template.replaceAll("FOLIO_NEGOCIO", resumenCartaInstruccion.getFolio());
    template = template.replaceAll("RAZON_SOCIAL", resumenCartaInstruccion.getRazonSocial());

    source.getCorreo().setCuerpoCorreo(template);
    
    return source;
  }

  @Override
  public Long assemblePK(Long source) {
    return source;
  }
  
}
