package mx.gob.imss.dpes.notificacionback.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.enums.TipoSimulacionEnum;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;

public class OfertaRequest extends BaseModel {

  @Getter @Setter Pensionado pensionado;
  @Getter @Setter double monto;
  @Getter @Setter double capacidadCredito;
  @Getter @Setter Long plazo;
  @Getter @Setter TipoSimulacionEnum tipoSimulacion;
  @Getter @Setter Double descuentoMensual;
  @Getter @Setter String email;
}
