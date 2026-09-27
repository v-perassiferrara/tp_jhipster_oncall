import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IPeriodoDeFacturacion, NewPeriodoDeFacturacion } from '../periodo-de-facturacion.model';

export type PartialUpdatePeriodoDeFacturacion = Partial<IPeriodoDeFacturacion> & Pick<IPeriodoDeFacturacion, 'id'>;

type RestOf<T extends IPeriodoDeFacturacion | NewPeriodoDeFacturacion> = Omit<T, 'fechaInicio' | 'fechaFin'> & {
  fechaInicio?: string | null;
  fechaFin?: string | null;
};

export type RestPeriodoDeFacturacion = RestOf<IPeriodoDeFacturacion>;

export type NewRestPeriodoDeFacturacion = RestOf<NewPeriodoDeFacturacion>;

export type PartialUpdateRestPeriodoDeFacturacion = RestOf<PartialUpdatePeriodoDeFacturacion>;

@Injectable()
export class PeriodoDeFacturacionsService {
  readonly periodoDeFacturacionsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly periodoDeFacturacionsResource = httpResource<RestPeriodoDeFacturacion[]>(() => {
    const params = this.periodoDeFacturacionsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of periodoDeFacturacion that have been fetched. It is updated when the periodoDeFacturacionsResource emits a new value.
   * In case of error while fetching the periodoDeFacturacions, the signal is set to an empty array.
   */
  readonly periodoDeFacturacions = computed(() =>
    (this.periodoDeFacturacionsResource.hasValue() ? this.periodoDeFacturacionsResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/periodo-de-facturacions');

  protected convertValueFromServer(restPeriodoDeFacturacion: RestPeriodoDeFacturacion): IPeriodoDeFacturacion {
    return {
      ...restPeriodoDeFacturacion,
      fechaInicio: restPeriodoDeFacturacion.fechaInicio ? dayjs(restPeriodoDeFacturacion.fechaInicio) : undefined,
      fechaFin: restPeriodoDeFacturacion.fechaFin ? dayjs(restPeriodoDeFacturacion.fechaFin) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class PeriodoDeFacturacionService extends PeriodoDeFacturacionsService {
  protected readonly http = inject(HttpClient);

  create(periodoDeFacturacion: NewPeriodoDeFacturacion): Observable<IPeriodoDeFacturacion> {
    const copy = this.convertValueFromClient(periodoDeFacturacion);
    return this.http.post<RestPeriodoDeFacturacion>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(periodoDeFacturacion: IPeriodoDeFacturacion): Observable<IPeriodoDeFacturacion> {
    const copy = this.convertValueFromClient(periodoDeFacturacion);
    return this.http
      .put<RestPeriodoDeFacturacion>(
        `${this.resourceUrl}/${encodeURIComponent(this.getPeriodoDeFacturacionIdentifier(periodoDeFacturacion))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(periodoDeFacturacion: PartialUpdatePeriodoDeFacturacion): Observable<IPeriodoDeFacturacion> {
    const copy = this.convertValueFromClient(periodoDeFacturacion);
    return this.http
      .patch<RestPeriodoDeFacturacion>(
        `${this.resourceUrl}/${encodeURIComponent(this.getPeriodoDeFacturacionIdentifier(periodoDeFacturacion))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IPeriodoDeFacturacion> {
    return this.http
      .get<RestPeriodoDeFacturacion>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IPeriodoDeFacturacion[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPeriodoDeFacturacion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPeriodoDeFacturacionIdentifier(periodoDeFacturacion: Pick<IPeriodoDeFacturacion, 'id'>): number {
    return periodoDeFacturacion.id;
  }

  comparePeriodoDeFacturacion(o1: Pick<IPeriodoDeFacturacion, 'id'> | null, o2: Pick<IPeriodoDeFacturacion, 'id'> | null): boolean {
    return o1 && o2 ? this.getPeriodoDeFacturacionIdentifier(o1) === this.getPeriodoDeFacturacionIdentifier(o2) : o1 === o2;
  }

  addPeriodoDeFacturacionToCollectionIfMissing<Type extends Pick<IPeriodoDeFacturacion, 'id'>>(
    periodoDeFacturacionCollection: Type[],
    ...periodoDeFacturacionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const periodoDeFacturacions: Type[] = periodoDeFacturacionsToCheck.filter(isPresent);
    if (periodoDeFacturacions.length > 0) {
      const periodoDeFacturacionCollectionIdentifiers = periodoDeFacturacionCollection.map(periodoDeFacturacionItem =>
        this.getPeriodoDeFacturacionIdentifier(periodoDeFacturacionItem),
      );
      const periodoDeFacturacionsToAdd = periodoDeFacturacions.filter(periodoDeFacturacionItem => {
        const periodoDeFacturacionIdentifier = this.getPeriodoDeFacturacionIdentifier(periodoDeFacturacionItem);
        if (periodoDeFacturacionCollectionIdentifiers.includes(periodoDeFacturacionIdentifier)) {
          return false;
        }
        periodoDeFacturacionCollectionIdentifiers.push(periodoDeFacturacionIdentifier);
        return true;
      });
      return [...periodoDeFacturacionsToAdd, ...periodoDeFacturacionCollection];
    }
    return periodoDeFacturacionCollection;
  }

  protected convertValueFromClient<T extends IPeriodoDeFacturacion | NewPeriodoDeFacturacion | PartialUpdatePeriodoDeFacturacion>(
    periodoDeFacturacion: T,
  ): RestOf<T> {
    return {
      ...periodoDeFacturacion,
      fechaInicio: periodoDeFacturacion.fechaInicio?.format(DATE_FORMAT) ?? null,
      fechaFin: periodoDeFacturacion.fechaFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestPeriodoDeFacturacion): IPeriodoDeFacturacion {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestPeriodoDeFacturacion[]): IPeriodoDeFacturacion[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
