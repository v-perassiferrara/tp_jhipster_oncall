import dayjs from 'dayjs/esm';

export interface IPeriodoDeFacturacion {
  id: number;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  cerrado?: boolean | null;
}

export type NewPeriodoDeFacturacion = Omit<IPeriodoDeFacturacion, 'id'> & { id: null };
