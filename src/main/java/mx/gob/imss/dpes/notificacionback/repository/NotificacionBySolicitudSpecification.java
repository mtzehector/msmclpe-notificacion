package mx.gob.imss.dpes.notificacionback.repository;

import mx.gob.imss.dpes.baseback.persistence.BaseSpecification;
import mx.gob.imss.dpes.notificacionback.entity.McltNotificacion;
import mx.gob.imss.dpes.notificacionback.entity.McltNotificacion_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NotificacionBySolicitudSpecification extends BaseSpecification<McltNotificacion> {

  private final long cveSolicitud;

  public NotificacionBySolicitudSpecification(long cveSolicitud) {
    this.cveSolicitud = cveSolicitud;
  }

  @Override
  public Predicate toPredicate(Root<McltNotificacion> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
    return cb.equal(root.get(McltNotificacion_.cveSolicitud), this.cveSolicitud);
  }
}
