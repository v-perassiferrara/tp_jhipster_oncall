import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import TarifaDeServicioResolve from './route/tarifa-de-servicio-routing-resolve.service';

const tarifaDeServicioRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/tarifa-de-servicio').then(m => m.TarifaDeServicio),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/tarifa-de-servicio-detail').then(m => m.TarifaDeServicioDetail),
    resolve: {
      tarifaDeServicio: TarifaDeServicioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/tarifa-de-servicio-update').then(m => m.TarifaDeServicioUpdate),
    resolve: {
      tarifaDeServicio: TarifaDeServicioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/tarifa-de-servicio-update').then(m => m.TarifaDeServicioUpdate),
    resolve: {
      tarifaDeServicio: TarifaDeServicioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tarifaDeServicioRoute;
