import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPeriodoDeFacturacion } from '../periodo-de-facturacion.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../periodo-de-facturacion.test-samples';

import { PeriodoDeFacturacionService, RestPeriodoDeFacturacion } from './periodo-de-facturacion.service';

const requireRestSample: RestPeriodoDeFacturacion = {
  ...sampleWithRequiredData,
  fechaInicio: sampleWithRequiredData.fechaInicio?.format(DATE_FORMAT),
  fechaFin: sampleWithRequiredData.fechaFin?.format(DATE_FORMAT),
};

describe('PeriodoDeFacturacion Service', () => {
  let service: PeriodoDeFacturacionService;
  let httpMock: HttpTestingController;
  let expectedResult: IPeriodoDeFacturacion | IPeriodoDeFacturacion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PeriodoDeFacturacionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PeriodoDeFacturacion', () => {
      const periodoDeFacturacion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(periodoDeFacturacion).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PeriodoDeFacturacion', () => {
      const periodoDeFacturacion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(periodoDeFacturacion).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PeriodoDeFacturacion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PeriodoDeFacturacion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PeriodoDeFacturacion', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPeriodoDeFacturacionToCollectionIfMissing', () => {
      it('should add a PeriodoDeFacturacion to an empty array', () => {
        const periodoDeFacturacion: IPeriodoDeFacturacion = sampleWithRequiredData;
        expectedResult = service.addPeriodoDeFacturacionToCollectionIfMissing([], periodoDeFacturacion);
        expect(expectedResult).toEqual([periodoDeFacturacion]);
      });

      it('should not add a PeriodoDeFacturacion to an array that contains it', () => {
        const periodoDeFacturacion: IPeriodoDeFacturacion = sampleWithRequiredData;
        const periodoDeFacturacionCollection: IPeriodoDeFacturacion[] = [
          {
            ...periodoDeFacturacion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPeriodoDeFacturacionToCollectionIfMissing(periodoDeFacturacionCollection, periodoDeFacturacion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PeriodoDeFacturacion to an array that doesn't contain it", () => {
        const periodoDeFacturacion: IPeriodoDeFacturacion = sampleWithRequiredData;
        const periodoDeFacturacionCollection: IPeriodoDeFacturacion[] = [sampleWithPartialData];
        expectedResult = service.addPeriodoDeFacturacionToCollectionIfMissing(periodoDeFacturacionCollection, periodoDeFacturacion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(periodoDeFacturacion);
      });

      it('should add only unique PeriodoDeFacturacion to an array', () => {
        const periodoDeFacturacionArray: IPeriodoDeFacturacion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const periodoDeFacturacionCollection: IPeriodoDeFacturacion[] = [sampleWithRequiredData];
        expectedResult = service.addPeriodoDeFacturacionToCollectionIfMissing(periodoDeFacturacionCollection, ...periodoDeFacturacionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const periodoDeFacturacion: IPeriodoDeFacturacion = sampleWithRequiredData;
        const periodoDeFacturacion2: IPeriodoDeFacturacion = sampleWithPartialData;
        expectedResult = service.addPeriodoDeFacturacionToCollectionIfMissing([], periodoDeFacturacion, periodoDeFacturacion2);
        expect(expectedResult).toEqual([periodoDeFacturacion, periodoDeFacturacion2]);
      });

      it('should accept null and undefined values', () => {
        const periodoDeFacturacion: IPeriodoDeFacturacion = sampleWithRequiredData;
        expectedResult = service.addPeriodoDeFacturacionToCollectionIfMissing([], null, periodoDeFacturacion, undefined);
        expect(expectedResult).toEqual([periodoDeFacturacion]);
      });

      it('should return initial array if no PeriodoDeFacturacion is added', () => {
        const periodoDeFacturacionCollection: IPeriodoDeFacturacion[] = [sampleWithRequiredData];
        expectedResult = service.addPeriodoDeFacturacionToCollectionIfMissing(periodoDeFacturacionCollection, undefined, null);
        expect(expectedResult).toEqual(periodoDeFacturacionCollection);
      });
    });

    describe('comparePeriodoDeFacturacion', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePeriodoDeFacturacion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 2236 };
        const entity2 = null;

        const compareResult1 = service.comparePeriodoDeFacturacion(entity1, entity2);
        const compareResult2 = service.comparePeriodoDeFacturacion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 2236 };
        const entity2 = { id: 27160 };

        const compareResult1 = service.comparePeriodoDeFacturacion(entity1, entity2);
        const compareResult2 = service.comparePeriodoDeFacturacion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 2236 };
        const entity2 = { id: 2236 };

        const compareResult1 = service.comparePeriodoDeFacturacion(entity1, entity2);
        const compareResult2 = service.comparePeriodoDeFacturacion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
