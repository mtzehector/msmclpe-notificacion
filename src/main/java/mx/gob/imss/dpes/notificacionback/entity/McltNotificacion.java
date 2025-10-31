package mx.gob.imss.dpes.notificacionback.entity;

import lombok.Data;
import mx.gob.imss.dpes.common.entity.LogicDeletedEntity;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "MCLT_NOTIFICACION")
@Data
public class McltNotificacion extends LogicDeletedEntity<Long> {

  public static final long serialVersionUID = 1L;

  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "CVE_NOTIFICACION")
  @GeneratedValue(generator = "SEQ_GEN_MCLS_NOTIFICACION")
  @GenericGenerator(
      name = "SEQ_GEN_MCLS_NOTIFICACION",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
          @Parameter(name = "sequence_name", value = "MCLS_NOTIFICACION"),
          @Parameter(name = "initial_value", value = "1"),
          @Parameter(name = "increment_size", value = "1")
      }
  )
  private Long id;
  @Basic(optional = false)
  @NotNull
  @Column(name = "CVE_SOLICITUD")
  private Integer cveSolicitud;
  @Column(name = "CVE_TIPO_NOTIFICACION")
  private Integer cveTipoNotificacion;
  @Column(name = "CVE_ESTADO_NOTIFICACION")
  private Integer cveEstadoNotificacion;
  @Size(max = 4000)
  @Column(name = "REF_NOTIFICACION")
  private String refNotificacion;
  @Column(name = "REF_FOLIO_NOTIFICACION")
  private String refFolioNotificacion;
  
 @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof McltNotificacion)) return false;
    McltNotificacion that = (McltNotificacion) o;
    return id.equals(that.id);
  }
}