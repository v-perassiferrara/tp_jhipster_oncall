import dayjs from 'dayjs/esm';

import { IServicio } from 'app/entities/servicio/servicio.model';

export interface ITarifaDeServicio {
  id: number;
  costoPorMinuto?: number | null;
  vigenteDesde?: dayjs.Dayjs | null;
  vigenteHasta?: dayjs.Dayjs | null;
  servicio?: Pick<IServicio, 'id' | 'nombre'> | null;
}

export type NewTarifaDeServicio = Omit<ITarifaDeServicio, 'id'> & { id: null };
