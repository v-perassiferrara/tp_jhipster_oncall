import dayjs from 'dayjs/esm';

import { ITarifaDeServicio, NewTarifaDeServicio } from './tarifa-de-servicio.model';

export const sampleWithRequiredData: ITarifaDeServicio = {
  id: 27314,
  costoPorMinuto: 24075.33,
  vigenteDesde: dayjs('2023-12-04T08:57'),
};

export const sampleWithPartialData: ITarifaDeServicio = {
  id: 4559,
  costoPorMinuto: 23897.28,
  vigenteDesde: dayjs('2023-12-04T21:37'),
};

export const sampleWithFullData: ITarifaDeServicio = {
  id: 6007,
  costoPorMinuto: 18071.67,
  vigenteDesde: dayjs('2023-12-04T02:50'),
  vigenteHasta: dayjs('2023-12-04T04:08'),
};

export const sampleWithNewData: NewTarifaDeServicio = {
  costoPorMinuto: 21157.83,
  vigenteDesde: dayjs('2023-12-04T21:09'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
