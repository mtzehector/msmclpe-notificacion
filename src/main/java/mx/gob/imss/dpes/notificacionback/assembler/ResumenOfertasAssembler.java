/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.notificacionback.assembler;

import java.io.StringWriter;
import java.util.List;

import mx.gob.imss.dpes.baseback.assembler.BaseAssembler;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.Beneficio;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.Oferta;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Adjunto;
import mx.gob.imss.dpes.notificacionback.model.NotificacionCorreo;
import mx.gob.imss.dpes.notificacionback.model.ResumenOfertas;

import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author Diego Velazquez
 */
@Provider
public class  ResumenOfertasAssembler extends BaseAssembler<NotificacionCorreo<ResumenOfertas>
        , NotificacionCorreo<ResumenOfertas>, Long, Long> {

  public static final String TRI = "<tr>";
  public static final String TRF = "</tr>";
  public static final String TDI ="<td>";
  public static final String TDF ="</td>";
  public static final String BR = "</br>";
  public static final String TDB = "<td style=\"text-align: justify\">";
  public static final String MXN = " MXN";
  public static final String SM = "&#36;";
  public static final String SP = "&#37;";
  public static final String MESES = " meses";

  @Override
  public NotificacionCorreo<ResumenOfertas> toEntity(
          NotificacionCorreo<ResumenOfertas> source) {
    return source;
  }

  @Override
  public Long toPKEntity(Long source) {
    return source;
  }

  @Override
  public NotificacionCorreo<ResumenOfertas> assemble(
          NotificacionCorreo<ResumenOfertas> source) {

    String template = source.getTemplate().getContent();
    StringBuilder tabla = new StringBuilder();
    ResumenOfertas resumenOfertas = source.getTipo();
    List<Oferta> ofertas = resumenOfertas.getOfertas();

    for( Oferta oferta : ofertas ){
      tabla.append(TRI);
      tabla.append(TDI).append(oferta.getEntidadFinanciera().getNombreComercial()!=null?oferta.getEntidadFinanciera().getNombreComercial():"").append(TDF);
      tabla.append(TDI).append(SM).append(oferta.getMonto()).append(MXN).append(TDF);
      tabla.append(TDI).append(oferta.getCat()).append(SP).append(TDF);
      tabla.append(TDI).append(SM).append(oferta.getDescuentoMensual()).append(MXN).append(TDF);
      tabla.append(TDI).append(oferta.getPlazo().getNumPlazo()).append(MESES).append(TDF);
      tabla.append(TDI).append(SM).append(oferta.getImporteTotal()).append(MXN).append(TDF);
      /*
      tabla.append(TDB);
      List<Beneficio> beneficios = oferta.getBeneficios();
      for( Beneficio beneficio : beneficios ){
        tabla.append(beneficio.getDesBeneficio()).append(BR);
      }
      tabla.append(TDF);
       */
      tabla.append(TRF);
    }

    template = template.replaceAll("TABLA_OFERTAS", tabla.toString());
    source.getCorreo().setCuerpoCorreo(template);

    return source;
  }

  @Override
  public Long assemblePK(Long source) {
    return source;
  }
}
