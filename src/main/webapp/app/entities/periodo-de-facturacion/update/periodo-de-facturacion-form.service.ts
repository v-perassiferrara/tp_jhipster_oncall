import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPeriodoDeFacturacion, NewPeriodoDeFacturacion } from '../periodo-de-facturacion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPeriodoDeFacturacion for edit and NewPeriodoDeFacturacionFormGroupInput for create.
 */
type PeriodoDeFacturacionFormGroupInput = IPeriodoDeFacturacion | PartialWithRequiredKeyOf<NewPeriodoDeFacturacion>;

type PeriodoDeFacturacionFormDefaults = Pick<NewPeriodoDeFacturacion, 'id' | 'cerrado'>;

type PeriodoDeFacturacionFormGroupContent = {
  id: FormControl<IPeriodoDeFacturacion['id'] | NewPeriodoDeFacturacion['id']>;
  fechaInicio: FormControl<IPeriodoDeFacturacion['fechaInicio']>;
  fechaFin: FormControl<IPeriodoDeFacturacion['fechaFin']>;
  cerrado: FormControl<IPeriodoDeFacturacion['cerrado']>;
};

export type PeriodoDeFacturacionFormGroup = FormGroup<PeriodoDeFacturacionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PeriodoDeFacturacionFormService {
  createPeriodoDeFacturacionFormGroup(periodoDeFacturacion?: PeriodoDeFacturacionFormGroupInput): PeriodoDeFacturacionFormGroup {
    const periodoDeFacturacionRawValue = {
      ...this.getFormDefaults(),
      ...(periodoDeFacturacion ?? { id: null }),
    };

    return new FormGroup<PeriodoDeFacturacionFormGroupContent>({
      id: new FormControl(
        { value: periodoDeFacturacionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fechaInicio: new FormControl(periodoDeFacturacionRawValue.fechaInicio, {
        validators: [Validators.required],
      }),
      fechaFin: new FormControl(periodoDeFacturacionRawValue.fechaFin, {
        validators: [Validators.required],
      }),
      cerrado: new FormControl(periodoDeFacturacionRawValue.cerrado, {
        validators: [Validators.required],
      }),
    });
  }

  getPeriodoDeFacturacion(form: PeriodoDeFacturacionFormGroup): IPeriodoDeFacturacion | NewPeriodoDeFacturacion {
    return form.getRawValue();
  }

  resetForm(form: PeriodoDeFacturacionFormGroup, periodoDeFacturacion: PeriodoDeFacturacionFormGroupInput): void {
    const periodoDeFacturacionRawValue = { ...this.getFormDefaults(), ...periodoDeFacturacion };
    form.reset({
      ...periodoDeFacturacionRawValue,
      id: { value: periodoDeFacturacionRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PeriodoDeFacturacionFormDefaults {
    return {
      id: null,
      cerrado: false,
    };
  }
}
