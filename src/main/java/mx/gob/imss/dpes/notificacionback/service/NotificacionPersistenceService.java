package mx.gob.imss.dpes.notificacionback.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.baseback.persistence.BaseSpecification;
import mx.gob.imss.dpes.baseback.service.BaseCRUDService;
import mx.gob.imss.dpes.notificacionback.entity.McltNotificacion;
import mx.gob.imss.dpes.notificacionback.repository.NotificacionBySolicitudAndTipoSpecification;
import mx.gob.imss.dpes.notificacionback.repository.NotificacionBySolicitudSpecification;
import mx.gob.imss.dpes.notificacionback.repository.NotificacionRepository;
import mx.gob.imss.dpes.support.config.CustomSpringBeanAutowiringInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.interceptor.Interceptors;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import mx.gob.imss.dpes.notificacionback.repository.NotificacionByFolio;

@Provider
@Interceptors(CustomSpringBeanAutowiringInterceptor.class)
public class NotificacionPersistenceService
    extends BaseCRUDService<McltNotificacion, McltNotificacion, Long, Long> {

  @Autowired
  private NotificacionRepository repository;

  

  public List<McltNotificacion> load(Message<McltNotificacion> request) throws BusinessException {
    Collection<BaseSpecification> constraints = setConstraints(request);
    List<McltNotificacion> entities = load(constraints);
    return entities;
  }

  public List<McltNotificacion> loadAll(Long cveSolicitud) throws BusinessException {
    Collection<BaseSpecification> constraints = new ArrayList<>();
    constraints.add(new NotificacionBySolicitudSpecification(cveSolicitud));
    List<McltNotificacion> entities = load(constraints);
    return entities;
  }

  public Message<McltNotificacion> update (Message<McltNotificacion> request) throws BusinessException {
    Collection<BaseSpecification> constraints = setConstraints(request);
    McltNotificacion entity = findOne(constraints);
    entity.setCveEstadoNotificacion(request.getPayload().getCveEstadoNotificacion());
    return new Message<>(update(entity));
  }

  private Collection<BaseSpecification> setConstraints (Message<McltNotificacion> request) {
    Collection<BaseSpecification> constraints = new ArrayList<>();
    constraints.add(new NotificacionBySolicitudAndTipoSpecification(
        request.getPayload().getCveSolicitud(), request.getPayload().getCveTipoNotificacion()));
    return constraints;
  }
  
  public List<McltNotificacion> getAllByFolio(String folioNotificacion) 
          throws BusinessException {
    Collection<BaseSpecification> constraints = new ArrayList<>();
    constraints.add(new NotificacionByFolio(folioNotificacion));
    List<McltNotificacion> entities = load(constraints);
    return entities;
  }

  @Override
  public JpaSpecificationExecutor<McltNotificacion> getRepository() {
    return repository;
  }

  @Override
  public JpaRepository<McltNotificacion, Long> getJpaRepository() {
    return repository;
  }
}