package mx.gob.imss.dpes.notificacionback.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import mx.gob.imss.dpes.baseback.persistence.BaseSpecification;
import mx.gob.imss.dpes.notificacionback.entity.McltNotificacion;
import mx.gob.imss.dpes.notificacionback.entity.McltNotificacion_;

/**
 *
 * @author luisr.rodriguez
 */
@AllArgsConstructor
public class NotificacionByFolio extends BaseSpecification<McltNotificacion> {

  private final String folioNotificacion;

  @Override
  public Predicate toPredicate(Root<McltNotificacion> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
    return cb.equal(root.get(McltNotificacion_.refFolioNotificacion), this.folioNotificacion);
  }
}