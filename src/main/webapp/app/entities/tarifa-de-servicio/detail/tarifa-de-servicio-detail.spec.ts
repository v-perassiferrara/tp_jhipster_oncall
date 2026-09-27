import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { TarifaDeServicioDetail } from './tarifa-de-servicio-detail';

describe('TarifaDeServicio Management Detail Component', () => {
  let comp: TarifaDeServicioDetail;
  let fixture: ComponentFixture<TarifaDeServicioDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./tarifa-de-servicio-detail').then(m => m.TarifaDeServicioDetail),
              resolve: { tarifaDeServicio: () => of({ id: 19133 }) },
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
    fixture = TestBed.createComponent(TarifaDeServicioDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load tarifaDeServicio on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TarifaDeServicioDetail);

      // THEN
      expect(instance.tarifaDeServicio()).toEqual(expect.objectContaining({ id: 19133 }));
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
