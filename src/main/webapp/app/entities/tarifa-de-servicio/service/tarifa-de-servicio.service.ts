import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ITarifaDeServicio, NewTarifaDeServicio } from '../tarifa-de-servicio.model';

export type PartialUpdateTarifaDeServicio = Partial<ITarifaDeServicio> & Pick<ITarifaDeServicio, 'id'>;

type RestOf<T extends ITarifaDeServicio | NewTarifaDeServicio> = Omit<T, 'vigenteDesde' | 'vigenteHasta'> & {
  vigenteDesde?: string | null;
  vigenteHasta?: string | null;
};

export type RestTarifaDeServicio = RestOf<ITarifaDeServicio>;

export type NewRestTarifaDeServicio = RestOf<NewTarifaDeServicio>;

export type PartialUpdateRestTarifaDeServicio = RestOf<PartialUpdateTarifaDeServicio>;

@Injectable()
export class TarifaDeServiciosService {
  readonly tarifaDeServiciosParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly tarifaDeServiciosResource = httpResource<RestTarifaDeServicio[]>(() => {
    const params = this.tarifaDeServiciosParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of tarifaDeServicio that have been fetched. It is updated when the tarifaDeServiciosResource emits a new value.
   * In case of error while fetching the tarifaDeServicios, the signal is set to an empty array.
   */
  readonly tarifaDeServicios = computed(() =>
    (this.tarifaDeServiciosResource.hasValue() ? this.tarifaDeServiciosResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/tarifa-de-servicios');

  protected convertValueFromServer(restTarifaDeServicio: RestTarifaDeServicio): ITarifaDeServicio {
    return {
      ...restTarifaDeServicio,
      vigenteDesde: restTarifaDeServicio.vigenteDesde ? dayjs(restTarifaDeServicio.vigenteDesde) : undefined,
      vigenteHasta: restTarifaDeServicio.vigenteHasta ? dayjs(restTarifaDeServicio.vigenteHasta) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class TarifaDeServicioService extends TarifaDeServiciosService {
  protected readonly http = inject(HttpClient);

  create(tarifaDeServicio: NewTarifaDeServicio): Observable<ITarifaDeServicio> {
    const copy = this.convertValueFromClient(tarifaDeServicio);
    return this.http.post<RestTarifaDeServicio>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tarifaDeServicio: ITarifaDeServicio): Observable<ITarifaDeServicio> {
    const copy = this.convertValueFromClient(tarifaDeServicio);
    return this.http
      .put<RestTarifaDeServicio>(`${this.resourceUrl}/${encodeURIComponent(this.getTarifaDeServicioIdentifier(tarifaDeServicio))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tarifaDeServicio: PartialUpdateTarifaDeServicio): Observable<ITarifaDeServicio> {
    const copy = this.convertValueFromClient(tarifaDeServicio);
    return this.http
      .patch<RestTarifaDeServicio>(`${this.resourceUrl}/${encodeURIComponent(this.getTarifaDeServicioIdentifier(tarifaDeServicio))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ITarifaDeServicio> {
    return this.http
      .get<RestTarifaDeServicio>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ITarifaDeServicio[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTarifaDeServicio[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getTarifaDeServicioIdentifier(tarifaDeServicio: Pick<ITarifaDeServicio, 'id'>): number {
    return tarifaDeServicio.id;
  }

  compareTarifaDeServicio(o1: Pick<ITarifaDeServicio, 'id'> | null, o2: Pick<ITarifaDeServicio, 'id'> | null): boolean {
    return o1 && o2 ? this.getTarifaDeServicioIdentifier(o1) === this.getTarifaDeServicioIdentifier(o2) : o1 === o2;
  }

  addTarifaDeServicioToCollectionIfMissing<Type extends Pick<ITarifaDeServicio, 'id'>>(
    tarifaDeServicioCollection: Type[],
    ...tarifaDeServiciosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tarifaDeServicios: Type[] = tarifaDeServiciosToCheck.filter(isPresent);
    if (tarifaDeServicios.length > 0) {
      const tarifaDeServicioCollectionIdentifiers = tarifaDeServicioCollection.map(tarifaDeServicioItem =>
        this.getTarifaDeServicioIdentifier(tarifaDeServicioItem),
      );
      const tarifaDeServiciosToAdd = tarifaDeServicios.filter(tarifaDeServicioItem => {
        const tarifaDeServicioIdentifier = this.getTarifaDeServicioIdentifier(tarifaDeServicioItem);
        if (tarifaDeServicioCollectionIdentifiers.includes(tarifaDeServicioIdentifier)) {
          return false;
        }
        tarifaDeServicioCollectionIdentifiers.push(tarifaDeServicioIdentifier);
        return true;
      });
      return [...tarifaDeServiciosToAdd, ...tarifaDeServicioCollection];
    }
    return tarifaDeServicioCollection;
  }

  protected convertValueFromClient<T extends ITarifaDeServicio | NewTarifaDeServicio | PartialUpdateTarifaDeServicio>(
    tarifaDeServicio: T,
  ): RestOf<T> {
    return {
      ...tarifaDeServicio,
      vigenteDesde: tarifaDeServicio.vigenteDesde?.toJSON() ?? null,
      vigenteHasta: tarifaDeServicio.vigenteHasta?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestTarifaDeServicio): ITarifaDeServicio {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestTarifaDeServicio[]): ITarifaDeServicio[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
