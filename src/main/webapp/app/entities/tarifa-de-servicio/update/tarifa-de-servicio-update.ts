import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { TarifaDeServicioService } from '../service/tarifa-de-servicio.service';
import { ITarifaDeServicio } from '../tarifa-de-servicio.model';

import { TarifaDeServicioFormGroup, TarifaDeServicioFormService } from './tarifa-de-servicio-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-tarifa-de-servicio-update',
  templateUrl: './tarifa-de-servicio-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class TarifaDeServicioUpdate implements OnInit {
  readonly isSaving = signal(false);
  tarifaDeServicio: ITarifaDeServicio | null = null;

  serviciosSharedCollection = signal<IServicio[]>([]);

  protected tarifaDeServicioService = inject(TarifaDeServicioService);
  protected tarifaDeServicioFormService = inject(TarifaDeServicioFormService);
  protected servicioService = inject(ServicioService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TarifaDeServicioFormGroup = this.tarifaDeServicioFormService.createTarifaDeServicioFormGroup();

  compareServicio = (o1: IServicio | null, o2: IServicio | null): boolean => this.servicioService.compareServicio(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tarifaDeServicio }) => {
      this.tarifaDeServicio = tarifaDeServicio;
      if (tarifaDeServicio) {
        this.updateForm(tarifaDeServicio);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const tarifaDeServicio = this.tarifaDeServicioFormService.getTarifaDeServicio(this.editForm);
    if (tarifaDeServicio.id === null) {
      this.subscribeToSaveResponse(this.tarifaDeServicioService.create(tarifaDeServicio));
    } else {
      this.subscribeToSaveResponse(this.tarifaDeServicioService.update(tarifaDeServicio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ITarifaDeServicio | null>): void {
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

  protected updateForm(tarifaDeServicio: ITarifaDeServicio): void {
    this.tarifaDeServicio = tarifaDeServicio;
    this.tarifaDeServicioFormService.resetForm(this.editForm, tarifaDeServicio);

    this.serviciosSharedCollection.update(servicios =>
      this.servicioService.addServicioToCollectionIfMissing<IServicio>(servicios, tarifaDeServicio.servicio),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.servicioService
      .query()
      .pipe(map((res: HttpResponse<IServicio[]>) => res.body ?? []))
      .pipe(
        map((servicios: IServicio[]) =>
          this.servicioService.addServicioToCollectionIfMissing<IServicio>(servicios, this.tarifaDeServicio?.servicio),
        ),
      )
      .subscribe((servicios: IServicio[]) => this.serviciosSharedCollection.set(servicios));
  }
}
