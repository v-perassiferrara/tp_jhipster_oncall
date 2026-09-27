import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { TarifaDeServicioService } from '../service/tarifa-de-servicio.service';
import { ITarifaDeServicio } from '../tarifa-de-servicio.model';

import { TarifaDeServicioFormService } from './tarifa-de-servicio-form.service';
import { TarifaDeServicioUpdate } from './tarifa-de-servicio-update';

describe('TarifaDeServicio Management Update Component', () => {
  let comp: TarifaDeServicioUpdate;
  let fixture: ComponentFixture<TarifaDeServicioUpdate>;
  let activatedRoute: ActivatedRoute;
  let tarifaDeServicioFormService: TarifaDeServicioFormService;
  let tarifaDeServicioService: TarifaDeServicioService;
  let servicioService: ServicioService;

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

    fixture = TestBed.createComponent(TarifaDeServicioUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tarifaDeServicioFormService = TestBed.inject(TarifaDeServicioFormService);
    tarifaDeServicioService = TestBed.inject(TarifaDeServicioService);
    servicioService = TestBed.inject(ServicioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Servicio query and add missing value', () => {
      const tarifaDeServicio: ITarifaDeServicio = { id: 8566 };
      const servicio: IServicio = { id: 24037 };
      tarifaDeServicio.servicio = servicio;

      const servicioCollection: IServicio[] = [{ id: 24037 }];
      vitest.spyOn(servicioService, 'query').mockReturnValue(of(new HttpResponse({ body: servicioCollection })));
      const additionalServicios = [servicio];
      const expectedCollection: IServicio[] = [...additionalServicios, ...servicioCollection];
      vitest.spyOn(servicioService, 'addServicioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tarifaDeServicio });
      comp.ngOnInit();

      expect(servicioService.query).toHaveBeenCalled();
      expect(servicioService.addServicioToCollectionIfMissing).toHaveBeenCalledWith(
        servicioCollection,
        ...additionalServicios.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.serviciosSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const tarifaDeServicio: ITarifaDeServicio = { id: 8566 };
      const servicio: IServicio = { id: 24037 };
      tarifaDeServicio.servicio = servicio;

      activatedRoute.data = of({ tarifaDeServicio });
      comp.ngOnInit();

      expect(comp.serviciosSharedCollection()).toContainEqual(servicio);
      expect(comp.tarifaDeServicio).toEqual(tarifaDeServicio);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITarifaDeServicio>();
      const tarifaDeServicio = { id: 19133 };
      vitest.spyOn(tarifaDeServicioFormService, 'getTarifaDeServicio').mockReturnValue(tarifaDeServicio);
      vitest.spyOn(tarifaDeServicioService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tarifaDeServicio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(tarifaDeServicio);
      saveSubject.complete();

      // THEN
      expect(tarifaDeServicioFormService.getTarifaDeServicio).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tarifaDeServicioService.update).toHaveBeenCalledWith(expect.objectContaining(tarifaDeServicio));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITarifaDeServicio>();
      const tarifaDeServicio = { id: 19133 };
      vitest.spyOn(tarifaDeServicioFormService, 'getTarifaDeServicio').mockReturnValue({ id: null });
      vitest.spyOn(tarifaDeServicioService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tarifaDeServicio: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(tarifaDeServicio);
      saveSubject.complete();

      // THEN
      expect(tarifaDeServicioFormService.getTarifaDeServicio).toHaveBeenCalled();
      expect(tarifaDeServicioService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ITarifaDeServicio>();
      const tarifaDeServicio = { id: 19133 };
      vitest.spyOn(tarifaDeServicioService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tarifaDeServicio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tarifaDeServicioService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareServicio', () => {
      it('should forward to servicioService', () => {
        const entity = { id: 24037 };
        const entity2 = { id: 644 };
        vitest.spyOn(servicioService, 'compareServicio');
        comp.compareServicio(entity, entity2);
        expect(servicioService.compareServicio).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
