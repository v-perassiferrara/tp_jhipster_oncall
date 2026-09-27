import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IPeriodoDeFacturacion } from '../periodo-de-facturacion.model';
import { PeriodoDeFacturacionService } from '../service/periodo-de-facturacion.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './periodo-de-facturacion-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class PeriodoDeFacturacionDeleteDialog {
  periodoDeFacturacion?: IPeriodoDeFacturacion;

  protected readonly periodoDeFacturacionService = inject(PeriodoDeFacturacionService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.periodoDeFacturacionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
