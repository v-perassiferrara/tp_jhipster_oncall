import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import PeriodoDeFacturacionResolve from './route/periodo-de-facturacion-routing-resolve.service';

const periodoDeFacturacionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/periodo-de-facturacion').then(m => m.PeriodoDeFacturacion),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/periodo-de-facturacion-detail').then(m => m.PeriodoDeFacturacionDetail),
    resolve: {
      periodoDeFacturacion: PeriodoDeFacturacionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/periodo-de-facturacion-update').then(m => m.PeriodoDeFacturacionUpdate),
    resolve: {
      periodoDeFacturacion: PeriodoDeFacturacionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/periodo-de-facturacion-update').then(m => m.PeriodoDeFacturacionUpdate),
    resolve: {
      periodoDeFacturacion: PeriodoDeFacturacionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default periodoDeFacturacionRoute;
