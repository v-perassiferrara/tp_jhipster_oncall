import dayjs from 'dayjs/esm';

import { IIncidente } from 'app/entities/incidente/incidente.model';
import { IPeriodoDeFacturacion } from 'app/entities/periodo-de-facturacion/periodo-de-facturacion.model';
import { IServicio } from 'app/entities/servicio/servicio.model';

export interface ICostoDeIncidente {
  id: number;
  minutosCaidos?: number | null;
  montoTotal?: number | null;
  calculadoEn?: dayjs.Dayjs | null;
  incidente?: Pick<IIncidente, 'id' | 'titulo'> | null;
  servicio?: Pick<IServicio, 'id' | 'nombre'> | null;
  periodo?: Pick<IPeriodoDeFacturacion, 'id' | 'fechaInicio'> | null;
}

export type NewCostoDeIncidente = Omit<ICostoDeIncidente, 'id'> & { id: null };
