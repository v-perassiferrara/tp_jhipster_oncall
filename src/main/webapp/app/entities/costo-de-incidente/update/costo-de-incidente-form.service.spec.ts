import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../costo-de-incidente.test-samples';

import { CostoDeIncidenteFormService } from './costo-de-incidente-form.service';

describe('CostoDeIncidente Form Service', () => {
  let service: CostoDeIncidenteFormService;

  beforeEach(() => {
    service = TestBed.inject(CostoDeIncidenteFormService);
  });

  describe('Service methods', () => {
    describe('createCostoDeIncidenteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCostoDeIncidenteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            minutosCaidos: expect.any(Object),
            montoTotal: expect.any(Object),
            calculadoEn: expect.any(Object),
            incidente: expect.any(Object),
            servicio: expect.any(Object),
            periodo: expect.any(Object),
          }),
        );
      });

      it('passing ICostoDeIncidente should create a new form with FormGroup', () => {
        const formGroup = service.createCostoDeIncidenteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            minutosCaidos: expect.any(Object),
            montoTotal: expect.any(Object),
            calculadoEn: expect.any(Object),
            incidente: expect.any(Object),
            servicio: expect.any(Object),
            periodo: expect.any(Object),
          }),
        );
      });
    });

    describe('getCostoDeIncidente', () => {
      it('should return NewCostoDeIncidente for default CostoDeIncidente initial value', () => {
        const formGroup = service.createCostoDeIncidenteFormGroup(sampleWithNewData);

        const costoDeIncidente = service.getCostoDeIncidente(formGroup);

        expect(costoDeIncidente).toMatchObject(sampleWithNewData);
      });

      it('should return NewCostoDeIncidente for empty CostoDeIncidente initial value', () => {
        const formGroup = service.createCostoDeIncidenteFormGroup();

        const costoDeIncidente = service.getCostoDeIncidente(formGroup);

        expect(costoDeIncidente).toMatchObject({});
      });

      it('should return ICostoDeIncidente', () => {
        const formGroup = service.createCostoDeIncidenteFormGroup(sampleWithRequiredData);

        const costoDeIncidente = service.getCostoDeIncidente(formGroup);

        expect(costoDeIncidente).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICostoDeIncidente should not enable id FormControl', () => {
        const formGroup = service.createCostoDeIncidenteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCostoDeIncidente should disable id FormControl', () => {
        const formGroup = service.createCostoDeIncidenteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
