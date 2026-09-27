import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../tarifa-de-servicio.test-samples';

import { TarifaDeServicioFormService } from './tarifa-de-servicio-form.service';

describe('TarifaDeServicio Form Service', () => {
  let service: TarifaDeServicioFormService;

  beforeEach(() => {
    service = TestBed.inject(TarifaDeServicioFormService);
  });

  describe('Service methods', () => {
    describe('createTarifaDeServicioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTarifaDeServicioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            costoPorMinuto: expect.any(Object),
            vigenteDesde: expect.any(Object),
            vigenteHasta: expect.any(Object),
            servicio: expect.any(Object),
          }),
        );
      });

      it('passing ITarifaDeServicio should create a new form with FormGroup', () => {
        const formGroup = service.createTarifaDeServicioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            costoPorMinuto: expect.any(Object),
            vigenteDesde: expect.any(Object),
            vigenteHasta: expect.any(Object),
            servicio: expect.any(Object),
          }),
        );
      });
    });

    describe('getTarifaDeServicio', () => {
      it('should return NewTarifaDeServicio for default TarifaDeServicio initial value', () => {
        const formGroup = service.createTarifaDeServicioFormGroup(sampleWithNewData);

        const tarifaDeServicio = service.getTarifaDeServicio(formGroup);

        expect(tarifaDeServicio).toMatchObject(sampleWithNewData);
      });

      it('should return NewTarifaDeServicio for empty TarifaDeServicio initial value', () => {
        const formGroup = service.createTarifaDeServicioFormGroup();

        const tarifaDeServicio = service.getTarifaDeServicio(formGroup);

        expect(tarifaDeServicio).toMatchObject({});
      });

      it('should return ITarifaDeServicio', () => {
        const formGroup = service.createTarifaDeServicioFormGroup(sampleWithRequiredData);

        const tarifaDeServicio = service.getTarifaDeServicio(formGroup);

        expect(tarifaDeServicio).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITarifaDeServicio should not enable id FormControl', () => {
        const formGroup = service.createTarifaDeServicioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTarifaDeServicio should disable id FormControl', () => {
        const formGroup = service.createTarifaDeServicioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
