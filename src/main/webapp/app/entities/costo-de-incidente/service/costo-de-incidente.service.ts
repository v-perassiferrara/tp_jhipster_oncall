import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ICostoDeIncidente, NewCostoDeIncidente } from '../costo-de-incidente.model';

export type PartialUpdateCostoDeIncidente = Partial<ICostoDeIncidente> & Pick<ICostoDeIncidente, 'id'>;

type RestOf<T extends ICostoDeIncidente | NewCostoDeIncidente> = Omit<T, 'calculadoEn'> & {
  calculadoEn?: string | null;
};

export type RestCostoDeIncidente = RestOf<ICostoDeIncidente>;

export type NewRestCostoDeIncidente = RestOf<NewCostoDeIncidente>;

export type PartialUpdateRestCostoDeIncidente = RestOf<PartialUpdateCostoDeIncidente>;

@Injectable()
export class CostoDeIncidentesService {
  readonly costoDeIncidentesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly costoDeIncidentesResource = httpResource<RestCostoDeIncidente[]>(() => {
    const params = this.costoDeIncidentesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of costoDeIncidente that have been fetched. It is updated when the costoDeIncidentesResource emits a new value.
   * In case of error while fetching the costoDeIncidentes, the signal is set to an empty array.
   */
  readonly costoDeIncidentes = computed(() =>
    (this.costoDeIncidentesResource.hasValue() ? this.costoDeIncidentesResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/costo-de-incidentes');

  protected convertValueFromServer(restCostoDeIncidente: RestCostoDeIncidente): ICostoDeIncidente {
    return {
      ...restCostoDeIncidente,
      calculadoEn: restCostoDeIncidente.calculadoEn ? dayjs(restCostoDeIncidente.calculadoEn) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class CostoDeIncidenteService extends CostoDeIncidentesService {
  protected readonly http = inject(HttpClient);

  create(costoDeIncidente: NewCostoDeIncidente): Observable<ICostoDeIncidente> {
    const copy = this.convertValueFromClient(costoDeIncidente);
    return this.http.post<RestCostoDeIncidente>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(costoDeIncidente: ICostoDeIncidente): Observable<ICostoDeIncidente> {
    const copy = this.convertValueFromClient(costoDeIncidente);
    return this.http
      .put<RestCostoDeIncidente>(`${this.resourceUrl}/${encodeURIComponent(this.getCostoDeIncidenteIdentifier(costoDeIncidente))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(costoDeIncidente: PartialUpdateCostoDeIncidente): Observable<ICostoDeIncidente> {
    const copy = this.convertValueFromClient(costoDeIncidente);
    return this.http
      .patch<RestCostoDeIncidente>(`${this.resourceUrl}/${encodeURIComponent(this.getCostoDeIncidenteIdentifier(costoDeIncidente))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ICostoDeIncidente> {
    return this.http
      .get<RestCostoDeIncidente>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ICostoDeIncidente[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCostoDeIncidente[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCostoDeIncidenteIdentifier(costoDeIncidente: Pick<ICostoDeIncidente, 'id'>): number {
    return costoDeIncidente.id;
  }

  compareCostoDeIncidente(o1: Pick<ICostoDeIncidente, 'id'> | null, o2: Pick<ICostoDeIncidente, 'id'> | null): boolean {
    return o1 && o2 ? this.getCostoDeIncidenteIdentifier(o1) === this.getCostoDeIncidenteIdentifier(o2) : o1 === o2;
  }

  addCostoDeIncidenteToCollectionIfMissing<Type extends Pick<ICostoDeIncidente, 'id'>>(
    costoDeIncidenteCollection: Type[],
    ...costoDeIncidentesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const costoDeIncidentes: Type[] = costoDeIncidentesToCheck.filter(isPresent);
    if (costoDeIncidentes.length > 0) {
      const costoDeIncidenteCollectionIdentifiers = costoDeIncidenteCollection.map(costoDeIncidenteItem =>
        this.getCostoDeIncidenteIdentifier(costoDeIncidenteItem),
      );
      const costoDeIncidentesToAdd = costoDeIncidentes.filter(costoDeIncidenteItem => {
        const costoDeIncidenteIdentifier = this.getCostoDeIncidenteIdentifier(costoDeIncidenteItem);
        if (costoDeIncidenteCollectionIdentifiers.includes(costoDeIncidenteIdentifier)) {
          return false;
        }
        costoDeIncidenteCollectionIdentifiers.push(costoDeIncidenteIdentifier);
        return true;
      });
      return [...costoDeIncidentesToAdd, ...costoDeIncidenteCollection];
    }
    return costoDeIncidenteCollection;
  }

  protected convertValueFromClient<T extends ICostoDeIncidente | NewCostoDeIncidente | PartialUpdateCostoDeIncidente>(
    costoDeIncidente: T,
  ): RestOf<T> {
    return {
      ...costoDeIncidente,
      calculadoEn: costoDeIncidente.calculadoEn?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestCostoDeIncidente): ICostoDeIncidente {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestCostoDeIncidente[]): ICostoDeIncidente[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
