import dayjs from 'dayjs/esm';

import { IPeriodoDeFacturacion, NewPeriodoDeFacturacion } from './periodo-de-facturacion.model';

export const sampleWithRequiredData: IPeriodoDeFacturacion = {
  id: 3520,
  fechaInicio: dayjs('2023-12-04'),
  fechaFin: dayjs('2023-12-04'),
  cerrado: true,
};

export const sampleWithPartialData: IPeriodoDeFacturacion = {
  id: 3569,
  fechaInicio: dayjs('2023-12-04'),
  fechaFin: dayjs('2023-12-04'),
  cerrado: true,
};

export const sampleWithFullData: IPeriodoDeFacturacion = {
  id: 25592,
  fechaInicio: dayjs('2023-12-04'),
  fechaFin: dayjs('2023-12-04'),
  cerrado: false,
};

export const sampleWithNewData: NewPeriodoDeFacturacion = {
  fechaInicio: dayjs('2023-12-04'),
  fechaFin: dayjs('2023-12-04'),
  cerrado: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
