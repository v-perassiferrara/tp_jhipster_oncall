import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { CostoDeIncidenteDetail } from './costo-de-incidente-detail';

describe('CostoDeIncidente Management Detail Component', () => {
  let comp: CostoDeIncidenteDetail;
  let fixture: ComponentFixture<CostoDeIncidenteDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./costo-de-incidente-detail').then(m => m.CostoDeIncidenteDetail),
              resolve: { costoDeIncidente: () => of({ id: 13928 }) },
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
    fixture = TestBed.createComponent(CostoDeIncidenteDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load costoDeIncidente on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CostoDeIncidenteDetail);

      // THEN
      expect(instance.costoDeIncidente()).toEqual(expect.objectContaining({ id: 13928 }));
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
