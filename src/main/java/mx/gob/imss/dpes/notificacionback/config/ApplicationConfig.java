/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.notificacionback.config;


import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * @author antonio
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> resources = new java.util.HashSet<>();
    addRestResourceClasses(resources);
    return resources;
  }

    /**
   * Do not modify addRestResourceClasses() method.
   * It is automatically populated with
   * all resources defined in the project.
   * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(mx.gob.imss.dpes.common.exception.AlternateFlowMapper.class);
        resources.add(mx.gob.imss.dpes.common.exception.BusinessMapper.class);
        resources.add(mx.gob.imss.dpes.common.rule.MontoTotalRule.class);
        resources.add(mx.gob.imss.dpes.common.rule.PagoMensualRule.class);
        resources.add(mx.gob.imss.dpes.notificacionback.assembler.CorreoAutorizacionAssembler.class);
        resources.add(mx.gob.imss.dpes.notificacionback.assembler.ReporteCapacidadCreditoAssembler.class);
        resources.add(mx.gob.imss.dpes.notificacionback.assembler.ReporteResumenCartaInstruccionAssembler.class);
        resources.add(mx.gob.imss.dpes.notificacionback.assembler.ReporteResumenSimulacionAssembler.class);
        resources.add(mx.gob.imss.dpes.notificacionback.assembler.ResumenOfertasAssembler.class);
        resources.add(mx.gob.imss.dpes.notificacionback.endpoint.NotificacionEndPoint.class);
        resources.add(mx.gob.imss.dpes.notificacionback.service.CreateCorreoAutorizacionService.class);
        resources.add(mx.gob.imss.dpes.notificacionback.service.CreateCorreoOfertasService.class);
        resources.add(mx.gob.imss.dpes.notificacionback.service.CreateCorreoReporteCapacidadCreditoService.class);
        resources.add(mx.gob.imss.dpes.notificacionback.service.CreateCorreoReporteResumenCartaInstruccionService.class);
        resources.add(mx.gob.imss.dpes.notificacionback.service.CreateCorreoReporteResumenSimulacionService.class);
        resources.add(mx.gob.imss.dpes.notificacionback.service.EnviaOfertasService.class);
        resources.add(mx.gob.imss.dpes.notificacionback.service.NotificacionPersistenceService.class);
        resources.add(mx.gob.imss.dpes.notificacionback.service.ReadDocumentosAutorizacionService.class);
        resources.add(mx.gob.imss.dpes.notificacionback.service.ReadReporteCartaCapacidadService.class);
        resources.add(mx.gob.imss.dpes.notificacionback.service.ReadReporteResumenCartaInstruccionService.class);
        resources.add(mx.gob.imss.dpes.notificacionback.service.ReadReporteResumenSimulacionService.class);
        resources.add(mx.gob.imss.dpes.notificacionback.service.ReadResumenCapacidadService.class);
        resources.add(mx.gob.imss.dpes.notificacionback.service.ReadResumenCartaInstruccionService.class);
        resources.add(mx.gob.imss.dpes.notificacionback.service.ReadResumenSimulacionService.class);
        resources.add(mx.gob.imss.dpes.notificacionback.service.ReadTemplateService.class);
    }
}
