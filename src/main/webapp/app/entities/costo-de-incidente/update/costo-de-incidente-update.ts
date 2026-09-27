import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { IIncidente } from 'app/entities/incidente/incidente.model';
import { IncidenteService } from 'app/entities/incidente/service/incidente.service';
import { IPeriodoDeFacturacion } from 'app/entities/periodo-de-facturacion/periodo-de-facturacion.model';
import { PeriodoDeFacturacionService } from 'app/entities/periodo-de-facturacion/service/periodo-de-facturacion.service';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { ICostoDeIncidente } from '../costo-de-incidente.model';
import { CostoDeIncidenteService } from '../service/costo-de-incidente.service';

import { CostoDeIncidenteFormGroup, CostoDeIncidenteFormService } from './costo-de-incidente-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-costo-de-incidente-update',
  templateUrl: './costo-de-incidente-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CostoDeIncidenteUpdate implements OnInit {
  readonly isSaving = signal(false);
  costoDeIncidente: ICostoDeIncidente | null = null;

  incidentesSharedCollection = signal<IIncidente[]>([]);
  serviciosSharedCollection = signal<IServicio[]>([]);
  periodoDeFacturacionsSharedCollection = signal<IPeriodoDeFacturacion[]>([]);

  protected costoDeIncidenteService = inject(CostoDeIncidenteService);
  protected costoDeIncidenteFormService = inject(CostoDeIncidenteFormService);
  protected incidenteService = inject(IncidenteService);
  protected servicioService = inject(ServicioService);
  protected periodoDeFacturacionService = inject(PeriodoDeFacturacionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CostoDeIncidenteFormGroup = this.costoDeIncidenteFormService.createCostoDeIncidenteFormGroup();

  compareIncidente = (o1: IIncidente | null, o2: IIncidente | null): boolean => this.incidenteService.compareIncidente(o1, o2);

  compareServicio = (o1: IServicio | null, o2: IServicio | null): boolean => this.servicioService.compareServicio(o1, o2);

  comparePeriodoDeFacturacion = (o1: IPeriodoDeFacturacion | null, o2: IPeriodoDeFacturacion | null): boolean =>
    this.periodoDeFacturacionService.comparePeriodoDeFacturacion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ costoDeIncidente }) => {
      this.costoDeIncidente = costoDeIncidente;
      if (costoDeIncidente) {
        this.updateForm(costoDeIncidente);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const costoDeIncidente = this.costoDeIncidenteFormService.getCostoDeIncidente(this.editForm);
    if (costoDeIncidente.id === null) {
      this.subscribeToSaveResponse(this.costoDeIncidenteService.create(costoDeIncidente));
    } else {
      this.subscribeToSaveResponse(this.costoDeIncidenteService.update(costoDeIncidente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICostoDeIncidente | null>): void {
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

  protected updateForm(costoDeIncidente: ICostoDeIncidente): void {
    this.costoDeIncidente = costoDeIncidente;
    this.costoDeIncidenteFormService.resetForm(this.editForm, costoDeIncidente);

    this.incidentesSharedCollection.update(incidentes =>
      this.incidenteService.addIncidenteToCollectionIfMissing<IIncidente>(incidentes, costoDeIncidente.incidente),
    );
    this.serviciosSharedCollection.update(servicios =>
      this.servicioService.addServicioToCollectionIfMissing<IServicio>(servicios, costoDeIncidente.servicio),
    );
    this.periodoDeFacturacionsSharedCollection.update(periodoDeFacturacions =>
      this.periodoDeFacturacionService.addPeriodoDeFacturacionToCollectionIfMissing<IPeriodoDeFacturacion>(
        periodoDeFacturacions,
        costoDeIncidente.periodo,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.incidenteService
      .query()
      .pipe(map((res: HttpResponse<IIncidente[]>) => res.body ?? []))
      .pipe(
        map((incidentes: IIncidente[]) =>
          this.incidenteService.addIncidenteToCollectionIfMissing<IIncidente>(incidentes, this.costoDeIncidente?.incidente),
        ),
      )
      .subscribe((incidentes: IIncidente[]) => this.incidentesSharedCollection.set(incidentes));

    this.servicioService
      .query()
      .pipe(map((res: HttpResponse<IServicio[]>) => res.body ?? []))
      .pipe(
        map((servicios: IServicio[]) =>
          this.servicioService.addServicioToCollectionIfMissing<IServicio>(servicios, this.costoDeIncidente?.servicio),
        ),
      )
      .subscribe((servicios: IServicio[]) => this.serviciosSharedCollection.set(servicios));

    this.periodoDeFacturacionService
      .query()
      .pipe(map((res: HttpResponse<IPeriodoDeFacturacion[]>) => res.body ?? []))
      .pipe(
        map((periodoDeFacturacions: IPeriodoDeFacturacion[]) =>
          this.periodoDeFacturacionService.addPeriodoDeFacturacionToCollectionIfMissing<IPeriodoDeFacturacion>(
            periodoDeFacturacions,
            this.costoDeIncidente?.periodo,
          ),
        ),
      )
      .subscribe((periodoDeFacturacions: IPeriodoDeFacturacion[]) => this.periodoDeFacturacionsSharedCollection.set(periodoDeFacturacions));
  }
}
