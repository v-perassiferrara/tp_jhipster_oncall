import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { TarifaDeServicioService } from '../service/tarifa-de-servicio.service';
import { ITarifaDeServicio } from '../tarifa-de-servicio.model';

const tarifaDeServicioResolve = (route: ActivatedRouteSnapshot): Observable<null | ITarifaDeServicio> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(TarifaDeServicioService);
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

export default tarifaDeServicioResolve;
