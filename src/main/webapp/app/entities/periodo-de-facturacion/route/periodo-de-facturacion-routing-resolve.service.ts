import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPeriodoDeFacturacion } from '../periodo-de-facturacion.model';
import { PeriodoDeFacturacionService } from '../service/periodo-de-facturacion.service';

const periodoDeFacturacionResolve = (route: ActivatedRouteSnapshot): Observable<null | IPeriodoDeFacturacion> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PeriodoDeFacturacionService);
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

export default periodoDeFacturacionResolve;
