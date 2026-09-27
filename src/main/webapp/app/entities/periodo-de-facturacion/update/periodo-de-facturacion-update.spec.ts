import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPeriodoDeFacturacion } from '../periodo-de-facturacion.model';
import { PeriodoDeFacturacionService } from '../service/periodo-de-facturacion.service';

import { PeriodoDeFacturacionFormService } from './periodo-de-facturacion-form.service';
import { PeriodoDeFacturacionUpdate } from './periodo-de-facturacion-update';

describe('PeriodoDeFacturacion Management Update Component', () => {
  let comp: PeriodoDeFacturacionUpdate;
  let fixture: ComponentFixture<PeriodoDeFacturacionUpdate>;
  let activatedRoute: ActivatedRoute;
  let periodoDeFacturacionFormService: PeriodoDeFacturacionFormService;
  let periodoDeFacturacionService: PeriodoDeFacturacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(PeriodoDeFacturacionUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    periodoDeFacturacionFormService = TestBed.inject(PeriodoDeFacturacionFormService);
    periodoDeFacturacionService = TestBed.inject(PeriodoDeFacturacionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const periodoDeFacturacion: IPeriodoDeFacturacion = { id: 27160 };

      activatedRoute.data = of({ periodoDeFacturacion });
      comp.ngOnInit();

      expect(comp.periodoDeFacturacion).toEqual(periodoDeFacturacion);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPeriodoDeFacturacion>();
      const periodoDeFacturacion = { id: 2236 };
      vitest.spyOn(periodoDeFacturacionFormService, 'getPeriodoDeFacturacion').mockReturnValue(periodoDeFacturacion);
      vitest.spyOn(periodoDeFacturacionService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ periodoDeFacturacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(periodoDeFacturacion);
      saveSubject.complete();

      // THEN
      expect(periodoDeFacturacionFormService.getPeriodoDeFacturacion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(periodoDeFacturacionService.update).toHaveBeenCalledWith(expect.objectContaining(periodoDeFacturacion));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPeriodoDeFacturacion>();
      const periodoDeFacturacion = { id: 2236 };
      vitest.spyOn(periodoDeFacturacionFormService, 'getPeriodoDeFacturacion').mockReturnValue({ id: null });
      vitest.spyOn(periodoDeFacturacionService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ periodoDeFacturacion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(periodoDeFacturacion);
      saveSubject.complete();

      // THEN
      expect(periodoDeFacturacionFormService.getPeriodoDeFacturacion).toHaveBeenCalled();
      expect(periodoDeFacturacionService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPeriodoDeFacturacion>();
      const periodoDeFacturacion = { id: 2236 };
      vitest.spyOn(periodoDeFacturacionService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ periodoDeFacturacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(periodoDeFacturacionService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
