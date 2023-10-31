import { Component } from '@angular/core';
import { Need } from '../need';
import { NeedService } from '../need.service';
import { ErrorService } from '../error.service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-need-create',
  templateUrl: './need-create.component.html',
  styleUrls: ['./need-create.component.css']
})
export class NeedCreateComponent {
  constructor(
    private needService: NeedService,
    private errorService: ErrorService
  ) {}

  createNeed(name: string, cost: string, quantity: string): void {
    name = name.trim();
    var error_message = "";
    var cost_num = Number(cost) || -1;
    var quantity_num = Number(quantity) || -1;
    quantity_num = Math.floor(quantity_num) == quantity_num ? quantity_num : -1;
    if (name && cost_num > 0 && quantity_num > 0) {
      this.needService.addNeed({name: name, cost: cost_num, quantity: quantity_num} as Need).subscribe(need => {if (need) window.location.href = "admin/needs";})
    }else{
      if (!name) {
        error_message += "Name is required. ";
      }
      if (cost_num < 0) {
        error_message += "A valid cost is required. ";
      }
      if (quantity_num < 0) {
        error_message += "A valid quantity is required. ";
      }
      this.errorService.showError(error_message);
    }
  }
}
