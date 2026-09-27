import { ChangeDetectionStrategy, Component, OnInit, effect, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { TranslatePipe } from '@ngx-translate/core';
import { Subscription, combineLatest, filter, tap } from 'rxjs';

import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { PeriodoDeFacturacionDeleteDialog } from '../delete/periodo-de-facturacion-delete-dialog';
import { IPeriodoDeFacturacion } from '../periodo-de-facturacion.model';
import { PeriodoDeFacturacionService } from '../service/periodo-de-facturacion.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-periodo-de-facturacion',
  templateUrl: './periodo-de-facturacion.html',
  imports: [
    RouterLink,
    FormsModule,
    FontAwesomeModule,
    AlertError,
    Alert,
    SortDirective,
    SortByDirective,
    TranslateDirective,
    TranslatePipe,
    FormatMediumDatePipe,
  ],
})
export class PeriodoDeFacturacion implements OnInit {
  subscription: Subscription | null = null;
  readonly periodoDeFacturacions = signal<IPeriodoDeFacturacion[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly periodoDeFacturacionService = inject(PeriodoDeFacturacionService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.periodoDeFacturacionService.periodoDeFacturacionsResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      this.periodoDeFacturacions.set(
        this.fillComponentAttributesFromResponseBody([...this.periodoDeFacturacionService.periodoDeFacturacions()]),
      );
    });
  }

  trackId = (item: IPeriodoDeFacturacion): number => this.periodoDeFacturacionService.getPeriodoDeFacturacionIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (this.periodoDeFacturacions().length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  delete(periodoDeFacturacion: IPeriodoDeFacturacion): void {
    const modalRef = this.modalService.open(PeriodoDeFacturacionDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.periodoDeFacturacion = periodoDeFacturacion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend();
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected refineData(data: IPeriodoDeFacturacion[]): IPeriodoDeFacturacion[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IPeriodoDeFacturacion[]): IPeriodoDeFacturacion[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.periodoDeFacturacionService.periodoDeFacturacionsParams.set(queryObject);
  }

  protected handleNavigation(sortState: SortState): void {
    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
}
