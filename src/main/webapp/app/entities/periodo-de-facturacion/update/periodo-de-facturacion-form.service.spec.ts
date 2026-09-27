import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../periodo-de-facturacion.test-samples';

import { PeriodoDeFacturacionFormService } from './periodo-de-facturacion-form.service';

describe('PeriodoDeFacturacion Form Service', () => {
  let service: PeriodoDeFacturacionFormService;

  beforeEach(() => {
    service = TestBed.inject(PeriodoDeFacturacionFormService);
  });

  describe('Service methods', () => {
    describe('createPeriodoDeFacturacionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPeriodoDeFacturacionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fechaInicio: expect.any(Object),
            fechaFin: expect.any(Object),
            cerrado: expect.any(Object),
          }),
        );
      });

      it('passing IPeriodoDeFacturacion should create a new form with FormGroup', () => {
        const formGroup = service.createPeriodoDeFacturacionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fechaInicio: expect.any(Object),
            fechaFin: expect.any(Object),
            cerrado: expect.any(Object),
          }),
        );
      });
    });

    describe('getPeriodoDeFacturacion', () => {
      it('should return NewPeriodoDeFacturacion for default PeriodoDeFacturacion initial value', () => {
        const formGroup = service.createPeriodoDeFacturacionFormGroup(sampleWithNewData);

        const periodoDeFacturacion = service.getPeriodoDeFacturacion(formGroup);

        expect(periodoDeFacturacion).toMatchObject(sampleWithNewData);
      });

      it('should return NewPeriodoDeFacturacion for empty PeriodoDeFacturacion initial value', () => {
        const formGroup = service.createPeriodoDeFacturacionFormGroup();

        const periodoDeFacturacion = service.getPeriodoDeFacturacion(formGroup);

        expect(periodoDeFacturacion).toMatchObject({});
      });

      it('should return IPeriodoDeFacturacion', () => {
        const formGroup = service.createPeriodoDeFacturacionFormGroup(sampleWithRequiredData);

        const periodoDeFacturacion = service.getPeriodoDeFacturacion(formGroup);

        expect(periodoDeFacturacion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPeriodoDeFacturacion should not enable id FormControl', () => {
        const formGroup = service.createPeriodoDeFacturacionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPeriodoDeFacturacion should disable id FormControl', () => {
        const formGroup = service.createPeriodoDeFacturacionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
