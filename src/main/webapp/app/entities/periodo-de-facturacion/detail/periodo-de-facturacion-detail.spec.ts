import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { PeriodoDeFacturacionDetail } from './periodo-de-facturacion-detail';

describe('PeriodoDeFacturacion Management Detail Component', () => {
  let comp: PeriodoDeFacturacionDetail;
  let fixture: ComponentFixture<PeriodoDeFacturacionDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./periodo-de-facturacion-detail').then(m => m.PeriodoDeFacturacionDetail),
              resolve: { periodoDeFacturacion: () => of({ id: 2236 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PeriodoDeFacturacionDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load periodoDeFacturacion on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PeriodoDeFacturacionDetail);

      // THEN
      expect(instance.periodoDeFacturacion()).toEqual(expect.objectContaining({ id: 2236 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vitest.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
