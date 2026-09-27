import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import CostoDeIncidenteResolve from './route/costo-de-incidente-routing-resolve.service';

const costoDeIncidenteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/costo-de-incidente').then(m => m.CostoDeIncidente),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/costo-de-incidente-detail').then(m => m.CostoDeIncidenteDetail),
    resolve: {
      costoDeIncidente: CostoDeIncidenteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/costo-de-incidente-update').then(m => m.CostoDeIncidenteUpdate),
    resolve: {
      costoDeIncidente: CostoDeIncidenteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/costo-de-incidente-update').then(m => m.CostoDeIncidenteUpdate),
    resolve: {
      costoDeIncidente: CostoDeIncidenteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default costoDeIncidenteRoute;
