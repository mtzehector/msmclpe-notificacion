package mx.gob.imss.dpes.notificacionback.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.PageRequestModel;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.Oferta;

import java.util.ArrayList;
import java.util.List;

public class ResumenOfertas extends BaseModel {
  @Getter @Setter private List<Oferta> ofertas = new ArrayList<>();
  @Getter @Setter PageRequestModel<OfertaRequest> requestPageRequestModel = new PageRequestModel<>();
}
