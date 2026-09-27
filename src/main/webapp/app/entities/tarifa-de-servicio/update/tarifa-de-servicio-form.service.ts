import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITarifaDeServicio, NewTarifaDeServicio } from '../tarifa-de-servicio.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITarifaDeServicio for edit and NewTarifaDeServicioFormGroupInput for create.
 */
type TarifaDeServicioFormGroupInput = ITarifaDeServicio | PartialWithRequiredKeyOf<NewTarifaDeServicio>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITarifaDeServicio | NewTarifaDeServicio> = Omit<T, 'vigenteDesde' | 'vigenteHasta'> & {
  vigenteDesde?: string | null;
  vigenteHasta?: string | null;
};

type TarifaDeServicioFormRawValue = FormValueOf<ITarifaDeServicio>;

type NewTarifaDeServicioFormRawValue = FormValueOf<NewTarifaDeServicio>;

type TarifaDeServicioFormDefaults = Pick<NewTarifaDeServicio, 'id' | 'vigenteDesde' | 'vigenteHasta'>;

type TarifaDeServicioFormGroupContent = {
  id: FormControl<TarifaDeServicioFormRawValue['id'] | NewTarifaDeServicio['id']>;
  costoPorMinuto: FormControl<TarifaDeServicioFormRawValue['costoPorMinuto']>;
  vigenteDesde: FormControl<TarifaDeServicioFormRawValue['vigenteDesde']>;
  vigenteHasta: FormControl<TarifaDeServicioFormRawValue['vigenteHasta']>;
  servicio: FormControl<TarifaDeServicioFormRawValue['servicio']>;
};

export type TarifaDeServicioFormGroup = FormGroup<TarifaDeServicioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TarifaDeServicioFormService {
  createTarifaDeServicioFormGroup(tarifaDeServicio?: TarifaDeServicioFormGroupInput): TarifaDeServicioFormGroup {
    const tarifaDeServicioRawValue = this.convertTarifaDeServicioToTarifaDeServicioRawValue({
      ...this.getFormDefaults(),
      ...(tarifaDeServicio ?? { id: null }),
    });

    return new FormGroup<TarifaDeServicioFormGroupContent>({
      id: new FormControl(
        { value: tarifaDeServicioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      costoPorMinuto: new FormControl(tarifaDeServicioRawValue.costoPorMinuto, {
        validators: [Validators.required, Validators.min(0)],
      }),
      vigenteDesde: new FormControl(tarifaDeServicioRawValue.vigenteDesde, {
        validators: [Validators.required],
      }),
      vigenteHasta: new FormControl(tarifaDeServicioRawValue.vigenteHasta),
      servicio: new FormControl(tarifaDeServicioRawValue.servicio, {
        validators: [Validators.required],
      }),
    });
  }

  getTarifaDeServicio(form: TarifaDeServicioFormGroup): ITarifaDeServicio | NewTarifaDeServicio {
    return this.convertTarifaDeServicioRawValueToTarifaDeServicio(form.getRawValue());
  }

  resetForm(form: TarifaDeServicioFormGroup, tarifaDeServicio: TarifaDeServicioFormGroupInput): void {
    const tarifaDeServicioRawValue = this.convertTarifaDeServicioToTarifaDeServicioRawValue({
      ...this.getFormDefaults(),
      ...tarifaDeServicio,
    });
    form.reset({
      ...tarifaDeServicioRawValue,
      id: { value: tarifaDeServicioRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TarifaDeServicioFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      vigenteDesde: currentTime,
      vigenteHasta: currentTime,
    };
  }

  private convertTarifaDeServicioRawValueToTarifaDeServicio(
    rawTarifaDeServicio: TarifaDeServicioFormRawValue | NewTarifaDeServicioFormRawValue,
  ): ITarifaDeServicio | NewTarifaDeServicio {
    return {
      ...rawTarifaDeServicio,
      vigenteDesde: dayjs(rawTarifaDeServicio.vigenteDesde, DATE_TIME_FORMAT),
      vigenteHasta: dayjs(rawTarifaDeServicio.vigenteHasta, DATE_TIME_FORMAT),
    };
  }

  private convertTarifaDeServicioToTarifaDeServicioRawValue(
    tarifaDeServicio: ITarifaDeServicio | (Partial<NewTarifaDeServicio> & TarifaDeServicioFormDefaults),
  ): TarifaDeServicioFormRawValue | PartialWithRequiredKeyOf<NewTarifaDeServicioFormRawValue> {
    return {
      ...tarifaDeServicio,
      vigenteDesde: tarifaDeServicio.vigenteDesde ? tarifaDeServicio.vigenteDesde.format(DATE_TIME_FORMAT) : undefined,
      vigenteHasta: tarifaDeServicio.vigenteHasta ? tarifaDeServicio.vigenteHasta.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
