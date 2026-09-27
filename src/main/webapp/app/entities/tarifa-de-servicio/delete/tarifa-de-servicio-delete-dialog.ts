import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { TarifaDeServicioService } from '../service/tarifa-de-servicio.service';
import { ITarifaDeServicio } from '../tarifa-de-servicio.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './tarifa-de-servicio-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class TarifaDeServicioDeleteDialog {
  tarifaDeServicio?: ITarifaDeServicio;

  protected readonly tarifaDeServicioService = inject(TarifaDeServicioService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tarifaDeServicioService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
