import dayjs from 'dayjs/esm';

import { ICostoDeIncidente, NewCostoDeIncidente } from './costo-de-incidente.model';

export const sampleWithRequiredData: ICostoDeIncidente = {
  id: 32188,
  minutosCaidos: 16148,
  montoTotal: 10440.21,
};

export const sampleWithPartialData: ICostoDeIncidente = {
  id: 11850,
  minutosCaidos: 16243,
  montoTotal: 17851.45,
};

export const sampleWithFullData: ICostoDeIncidente = {
  id: 7365,
  minutosCaidos: 626,
  montoTotal: 16748.87,
  calculadoEn: dayjs('2023-12-04T22:17'),
};

export const sampleWithNewData: NewCostoDeIncidente = {
  minutosCaidos: 5874,
  montoTotal: 30529.27,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
