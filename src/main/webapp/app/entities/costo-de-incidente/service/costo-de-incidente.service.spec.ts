import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICostoDeIncidente } from '../costo-de-incidente.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../costo-de-incidente.test-samples';

import { CostoDeIncidenteService, RestCostoDeIncidente } from './costo-de-incidente.service';

const requireRestSample: RestCostoDeIncidente = {
  ...sampleWithRequiredData,
  calculadoEn: sampleWithRequiredData.calculadoEn?.toJSON(),
};

describe('CostoDeIncidente Service', () => {
  let service: CostoDeIncidenteService;
  let httpMock: HttpTestingController;
  let expectedResult: ICostoDeIncidente | ICostoDeIncidente[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CostoDeIncidenteService);
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

    it('should create a CostoDeIncidente', () => {
      const costoDeIncidente = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(costoDeIncidente).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CostoDeIncidente', () => {
      const costoDeIncidente = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(costoDeIncidente).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CostoDeIncidente', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CostoDeIncidente', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CostoDeIncidente', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCostoDeIncidenteToCollectionIfMissing', () => {
      it('should add a CostoDeIncidente to an empty array', () => {
        const costoDeIncidente: ICostoDeIncidente = sampleWithRequiredData;
        expectedResult = service.addCostoDeIncidenteToCollectionIfMissing([], costoDeIncidente);
        expect(expectedResult).toEqual([costoDeIncidente]);
      });

      it('should not add a CostoDeIncidente to an array that contains it', () => {
        const costoDeIncidente: ICostoDeIncidente = sampleWithRequiredData;
        const costoDeIncidenteCollection: ICostoDeIncidente[] = [
          {
            ...costoDeIncidente,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCostoDeIncidenteToCollectionIfMissing(costoDeIncidenteCollection, costoDeIncidente);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CostoDeIncidente to an array that doesn't contain it", () => {
        const costoDeIncidente: ICostoDeIncidente = sampleWithRequiredData;
        const costoDeIncidenteCollection: ICostoDeIncidente[] = [sampleWithPartialData];
        expectedResult = service.addCostoDeIncidenteToCollectionIfMissing(costoDeIncidenteCollection, costoDeIncidente);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(costoDeIncidente);
      });

      it('should add only unique CostoDeIncidente to an array', () => {
        const costoDeIncidenteArray: ICostoDeIncidente[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const costoDeIncidenteCollection: ICostoDeIncidente[] = [sampleWithRequiredData];
        expectedResult = service.addCostoDeIncidenteToCollectionIfMissing(costoDeIncidenteCollection, ...costoDeIncidenteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const costoDeIncidente: ICostoDeIncidente = sampleWithRequiredData;
        const costoDeIncidente2: ICostoDeIncidente = sampleWithPartialData;
        expectedResult = service.addCostoDeIncidenteToCollectionIfMissing([], costoDeIncidente, costoDeIncidente2);
        expect(expectedResult).toEqual([costoDeIncidente, costoDeIncidente2]);
      });

      it('should accept null and undefined values', () => {
        const costoDeIncidente: ICostoDeIncidente = sampleWithRequiredData;
        expectedResult = service.addCostoDeIncidenteToCollectionIfMissing([], null, costoDeIncidente, undefined);
        expect(expectedResult).toEqual([costoDeIncidente]);
      });

      it('should return initial array if no CostoDeIncidente is added', () => {
        const costoDeIncidenteCollection: ICostoDeIncidente[] = [sampleWithRequiredData];
        expectedResult = service.addCostoDeIncidenteToCollectionIfMissing(costoDeIncidenteCollection, undefined, null);
        expect(expectedResult).toEqual(costoDeIncidenteCollection);
      });
    });

    describe('compareCostoDeIncidente', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCostoDeIncidente(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 13928 };
        const entity2 = null;

        const compareResult1 = service.compareCostoDeIncidente(entity1, entity2);
        const compareResult2 = service.compareCostoDeIncidente(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 13928 };
        const entity2 = { id: 10111 };

        const compareResult1 = service.compareCostoDeIncidente(entity1, entity2);
        const compareResult2 = service.compareCostoDeIncidente(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 13928 };
        const entity2 = { id: 13928 };

        const compareResult1 = service.compareCostoDeIncidente(entity1, entity2);
        const compareResult2 = service.compareCostoDeIncidente(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
