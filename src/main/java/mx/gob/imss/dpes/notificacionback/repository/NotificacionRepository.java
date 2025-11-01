package mx.gob.imss.dpes.notificacionback.repository;

import mx.gob.imss.dpes.notificacionback.entity.McltNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NotificacionRepository extends JpaRepository<McltNotificacion, Long>,
    JpaSpecificationExecutor<McltNotificacion> {
}
