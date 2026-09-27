import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICostoDeIncidente, NewCostoDeIncidente } from '../costo-de-incidente.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICostoDeIncidente for edit and NewCostoDeIncidenteFormGroupInput for create.
 */
type CostoDeIncidenteFormGroupInput = ICostoDeIncidente | PartialWithRequiredKeyOf<NewCostoDeIncidente>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICostoDeIncidente | NewCostoDeIncidente> = Omit<T, 'calculadoEn'> & {
  calculadoEn?: string | null;
};

type CostoDeIncidenteFormRawValue = FormValueOf<ICostoDeIncidente>;

type NewCostoDeIncidenteFormRawValue = FormValueOf<NewCostoDeIncidente>;

type CostoDeIncidenteFormDefaults = Pick<NewCostoDeIncidente, 'id' | 'calculadoEn'>;

type CostoDeIncidenteFormGroupContent = {
  id: FormControl<CostoDeIncidenteFormRawValue['id'] | NewCostoDeIncidente['id']>;
  minutosCaidos: FormControl<CostoDeIncidenteFormRawValue['minutosCaidos']>;
  montoTotal: FormControl<CostoDeIncidenteFormRawValue['montoTotal']>;
  calculadoEn: FormControl<CostoDeIncidenteFormRawValue['calculadoEn']>;
  incidente: FormControl<CostoDeIncidenteFormRawValue['incidente']>;
  servicio: FormControl<CostoDeIncidenteFormRawValue['servicio']>;
  periodo: FormControl<CostoDeIncidenteFormRawValue['periodo']>;
};

export type CostoDeIncidenteFormGroup = FormGroup<CostoDeIncidenteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CostoDeIncidenteFormService {
  createCostoDeIncidenteFormGroup(costoDeIncidente?: CostoDeIncidenteFormGroupInput): CostoDeIncidenteFormGroup {
    const costoDeIncidenteRawValue = this.convertCostoDeIncidenteToCostoDeIncidenteRawValue({
      ...this.getFormDefaults(),
      ...(costoDeIncidente ?? { id: null }),
    });

    return new FormGroup<CostoDeIncidenteFormGroupContent>({
      id: new FormControl(
        { value: costoDeIncidenteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      minutosCaidos: new FormControl(costoDeIncidenteRawValue.minutosCaidos, {
        validators: [Validators.required, Validators.min(1)],
      }),
      montoTotal: new FormControl(costoDeIncidenteRawValue.montoTotal, {
        validators: [Validators.required, Validators.min(0)],
      }),
      calculadoEn: new FormControl(costoDeIncidenteRawValue.calculadoEn),
      incidente: new FormControl(costoDeIncidenteRawValue.incidente, {
        validators: [Validators.required],
      }),
      servicio: new FormControl(costoDeIncidenteRawValue.servicio, {
        validators: [Validators.required],
      }),
      periodo: new FormControl(costoDeIncidenteRawValue.periodo, {
        validators: [Validators.required],
      }),
    });
  }

  getCostoDeIncidente(form: CostoDeIncidenteFormGroup): ICostoDeIncidente | NewCostoDeIncidente {
    return this.convertCostoDeIncidenteRawValueToCostoDeIncidente(form.getRawValue());
  }

  resetForm(form: CostoDeIncidenteFormGroup, costoDeIncidente: CostoDeIncidenteFormGroupInput): void {
    const costoDeIncidenteRawValue = this.convertCostoDeIncidenteToCostoDeIncidenteRawValue({
      ...this.getFormDefaults(),
      ...costoDeIncidente,
    });
    form.reset({
      ...costoDeIncidenteRawValue,
      id: { value: costoDeIncidenteRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CostoDeIncidenteFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      calculadoEn: currentTime,
    };
  }

  private convertCostoDeIncidenteRawValueToCostoDeIncidente(
    rawCostoDeIncidente: CostoDeIncidenteFormRawValue | NewCostoDeIncidenteFormRawValue,
  ): ICostoDeIncidente | NewCostoDeIncidente {
    return {
      ...rawCostoDeIncidente,
      calculadoEn: dayjs(rawCostoDeIncidente.calculadoEn, DATE_TIME_FORMAT),
    };
  }

  private convertCostoDeIncidenteToCostoDeIncidenteRawValue(
    costoDeIncidente: ICostoDeIncidente | (Partial<NewCostoDeIncidente> & CostoDeIncidenteFormDefaults),
  ): CostoDeIncidenteFormRawValue | PartialWithRequiredKeyOf<NewCostoDeIncidenteFormRawValue> {
    return {
      ...costoDeIncidente,
      calculadoEn: costoDeIncidente.calculadoEn ? costoDeIncidente.calculadoEn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
