import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ICostoDeIncidente } from '../costo-de-incidente.model';
import { CostoDeIncidenteService } from '../service/costo-de-incidente.service';

const costoDeIncidenteResolve = (route: ActivatedRouteSnapshot): Observable<null | ICostoDeIncidente> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(CostoDeIncidenteService);
    return service.find(id).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          router.navigate(['404']);
        } else {
          router.navigate(['error']);
        }
        return EMPTY;
      }),
    );
  }

  return of(null);
};

export default costoDeIncidenteResolve;
