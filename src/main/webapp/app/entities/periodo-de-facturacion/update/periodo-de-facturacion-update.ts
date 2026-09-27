import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IPeriodoDeFacturacion } from '../periodo-de-facturacion.model';
import { PeriodoDeFacturacionService } from '../service/periodo-de-facturacion.service';

import { PeriodoDeFacturacionFormGroup, PeriodoDeFacturacionFormService } from './periodo-de-facturacion-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-periodo-de-facturacion-update',
  templateUrl: './periodo-de-facturacion-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class PeriodoDeFacturacionUpdate implements OnInit {
  readonly isSaving = signal(false);
  periodoDeFacturacion: IPeriodoDeFacturacion | null = null;

  protected periodoDeFacturacionService = inject(PeriodoDeFacturacionService);
  protected periodoDeFacturacionFormService = inject(PeriodoDeFacturacionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PeriodoDeFacturacionFormGroup = this.periodoDeFacturacionFormService.createPeriodoDeFacturacionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ periodoDeFacturacion }) => {
      this.periodoDeFacturacion = periodoDeFacturacion;
      if (periodoDeFacturacion) {
        this.updateForm(periodoDeFacturacion);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const periodoDeFacturacion = this.periodoDeFacturacionFormService.getPeriodoDeFacturacion(this.editForm);
    if (periodoDeFacturacion.id === null) {
      this.subscribeToSaveResponse(this.periodoDeFacturacionService.create(periodoDeFacturacion));
    } else {
      this.subscribeToSaveResponse(this.periodoDeFacturacionService.update(periodoDeFacturacion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPeriodoDeFacturacion | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(periodoDeFacturacion: IPeriodoDeFacturacion): void {
    this.periodoDeFacturacion = periodoDeFacturacion;
    this.periodoDeFacturacionFormService.resetForm(this.editForm, periodoDeFacturacion);
  }
}
