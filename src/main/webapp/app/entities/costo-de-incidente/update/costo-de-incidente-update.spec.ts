import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IIncidente } from 'app/entities/incidente/incidente.model';
import { IncidenteService } from 'app/entities/incidente/service/incidente.service';
import { IPeriodoDeFacturacion } from 'app/entities/periodo-de-facturacion/periodo-de-facturacion.model';
import { PeriodoDeFacturacionService } from 'app/entities/periodo-de-facturacion/service/periodo-de-facturacion.service';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { ICostoDeIncidente } from '../costo-de-incidente.model';
import { CostoDeIncidenteService } from '../service/costo-de-incidente.service';

import { CostoDeIncidenteFormService } from './costo-de-incidente-form.service';
import { CostoDeIncidenteUpdate } from './costo-de-incidente-update';

describe('CostoDeIncidente Management Update Component', () => {
  let comp: CostoDeIncidenteUpdate;
  let fixture: ComponentFixture<CostoDeIncidenteUpdate>;
  let activatedRoute: ActivatedRoute;
  let costoDeIncidenteFormService: CostoDeIncidenteFormService;
  let costoDeIncidenteService: CostoDeIncidenteService;
  let incidenteService: IncidenteService;
  let servicioService: ServicioService;
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

    fixture = TestBed.createComponent(CostoDeIncidenteUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    costoDeIncidenteFormService = TestBed.inject(CostoDeIncidenteFormService);
    costoDeIncidenteService = TestBed.inject(CostoDeIncidenteService);
    incidenteService = TestBed.inject(IncidenteService);
    servicioService = TestBed.inject(ServicioService);
    periodoDeFacturacionService = TestBed.inject(PeriodoDeFacturacionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Incidente query and add missing value', () => {
      const costoDeIncidente: ICostoDeIncidente = { id: 10111 };
      const incidente: IIncidente = { id: 31968 };
      costoDeIncidente.incidente = incidente;

      const incidenteCollection: IIncidente[] = [{ id: 31968 }];
      vitest.spyOn(incidenteService, 'query').mockReturnValue(of(new HttpResponse({ body: incidenteCollection })));
      const additionalIncidentes = [incidente];
      const expectedCollection: IIncidente[] = [...additionalIncidentes, ...incidenteCollection];
      vitest.spyOn(incidenteService, 'addIncidenteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ costoDeIncidente });
      comp.ngOnInit();

      expect(incidenteService.query).toHaveBeenCalled();
      expect(incidenteService.addIncidenteToCollectionIfMissing).toHaveBeenCalledWith(
        incidenteCollection,
        ...additionalIncidentes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.incidentesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Servicio query and add missing value', () => {
      const costoDeIncidente: ICostoDeIncidente = { id: 10111 };
      const servicio: IServicio = { id: 24037 };
      costoDeIncidente.servicio = servicio;

      const servicioCollection: IServicio[] = [{ id: 24037 }];
      vitest.spyOn(servicioService, 'query').mockReturnValue(of(new HttpResponse({ body: servicioCollection })));
      const additionalServicios = [servicio];
      const expectedCollection: IServicio[] = [...additionalServicios, ...servicioCollection];
      vitest.spyOn(servicioService, 'addServicioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ costoDeIncidente });
      comp.ngOnInit();

      expect(servicioService.query).toHaveBeenCalled();
      expect(servicioService.addServicioToCollectionIfMissing).toHaveBeenCalledWith(
        servicioCollection,
        ...additionalServicios.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.serviciosSharedCollection()).toEqual(expectedCollection);
    });

    it('should call PeriodoDeFacturacion query and add missing value', () => {
      const costoDeIncidente: ICostoDeIncidente = { id: 10111 };
      const periodo: IPeriodoDeFacturacion = { id: 2236 };
      costoDeIncidente.periodo = periodo;

      const periodoDeFacturacionCollection: IPeriodoDeFacturacion[] = [{ id: 2236 }];
      vitest.spyOn(periodoDeFacturacionService, 'query').mockReturnValue(of(new HttpResponse({ body: periodoDeFacturacionCollection })));
      const additionalPeriodoDeFacturacions = [periodo];
      const expectedCollection: IPeriodoDeFacturacion[] = [...additionalPeriodoDeFacturacions, ...periodoDeFacturacionCollection];
      vitest.spyOn(periodoDeFacturacionService, 'addPeriodoDeFacturacionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ costoDeIncidente });
      comp.ngOnInit();

      expect(periodoDeFacturacionService.query).toHaveBeenCalled();
      expect(periodoDeFacturacionService.addPeriodoDeFacturacionToCollectionIfMissing).toHaveBeenCalledWith(
        periodoDeFacturacionCollection,
        ...additionalPeriodoDeFacturacions.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.periodoDeFacturacionsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const costoDeIncidente: ICostoDeIncidente = { id: 10111 };
      const incidente: IIncidente = { id: 31968 };
      costoDeIncidente.incidente = incidente;
      const servicio: IServicio = { id: 24037 };
      costoDeIncidente.servicio = servicio;
      const periodo: IPeriodoDeFacturacion = { id: 2236 };
      costoDeIncidente.periodo = periodo;

      activatedRoute.data = of({ costoDeIncidente });
      comp.ngOnInit();

      expect(comp.incidentesSharedCollection()).toContainEqual(incidente);
      expect(comp.serviciosSharedCollection()).toContainEqual(servicio);
      expect(comp.periodoDeFacturacionsSharedCollection()).toContainEqual(periodo);
      expect(comp.costoDeIncidente).toEqual(costoDeIncidente);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICostoDeIncidente>();
      const costoDeIncidente = { id: 13928 };
      vitest.spyOn(costoDeIncidenteFormService, 'getCostoDeIncidente').mockReturnValue(costoDeIncidente);
      vitest.spyOn(costoDeIncidenteService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ costoDeIncidente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(costoDeIncidente);
      saveSubject.complete();

      // THEN
      expect(costoDeIncidenteFormService.getCostoDeIncidente).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(costoDeIncidenteService.update).toHaveBeenCalledWith(expect.objectContaining(costoDeIncidente));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICostoDeIncidente>();
      const costoDeIncidente = { id: 13928 };
      vitest.spyOn(costoDeIncidenteFormService, 'getCostoDeIncidente').mockReturnValue({ id: null });
      vitest.spyOn(costoDeIncidenteService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ costoDeIncidente: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(costoDeIncidente);
      saveSubject.complete();

      // THEN
      expect(costoDeIncidenteFormService.getCostoDeIncidente).toHaveBeenCalled();
      expect(costoDeIncidenteService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICostoDeIncidente>();
      const costoDeIncidente = { id: 13928 };
      vitest.spyOn(costoDeIncidenteService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ costoDeIncidente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(costoDeIncidenteService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareIncidente', () => {
      it('should forward to incidenteService', () => {
        const entity = { id: 31968 };
        const entity2 = { id: 10195 };
        vitest.spyOn(incidenteService, 'compareIncidente');
        comp.compareIncidente(entity, entity2);
        expect(incidenteService.compareIncidente).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareServicio', () => {
      it('should forward to servicioService', () => {
        const entity = { id: 24037 };
        const entity2 = { id: 644 };
        vitest.spyOn(servicioService, 'compareServicio');
        comp.compareServicio(entity, entity2);
        expect(servicioService.compareServicio).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePeriodoDeFacturacion', () => {
      it('should forward to periodoDeFacturacionService', () => {
        const entity = { id: 2236 };
        const entity2 = { id: 27160 };
        vitest.spyOn(periodoDeFacturacionService, 'comparePeriodoDeFacturacion');
        comp.comparePeriodoDeFacturacion(entity, entity2);
        expect(periodoDeFacturacionService.comparePeriodoDeFacturacion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
