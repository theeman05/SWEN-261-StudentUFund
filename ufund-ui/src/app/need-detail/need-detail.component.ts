import { Component, OnInit } from '@angular/core';

import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Need } from '../need';
import { NeedService } from '../need.service';
import { ErrorService } from '../error.service';

@Component({
  selector: 'app-need-detail',
  templateUrl: './need-detail.component.html',
  styleUrls: ['./need-detail.component.css']
})
export class NeedDetailComponent {
  need: Need | undefined;

  constructor(
    private route: ActivatedRoute,
    private needService: NeedService,
    private location: Location,
    private errorService: ErrorService
  ) {}

  ngOnInit(): void {
    this.getNeed();
  }

  getNeed(): void {
    const name = String(this.route.snapshot.paramMap.get('name'));
    this.needService.getNeed(name)
      .subscribe(need => this.need = need);
  }

  goBack(): void {
    this.location.back();
  }

  updateNeed(): void {
    if (this.need) {
      var cost_num = Number(this.need.cost) || -1;
      var quantity_num = Number(this.need.quantity) || -1;
      var error_message = "";
      quantity_num = Math.floor(quantity_num) == quantity_num ? quantity_num : -1;
      if (cost_num < 0 || quantity_num < 0) {
        if (cost_num < 0) {
          error_message += "A valid cost is required. ";
        }
        if (quantity_num < 0) {
          error_message += "A valid quantity is required. ";
        }
        this.errorService.showError(error_message);
      }else{
        this.needService.updateNeed(this.need).subscribe(() => this.goBack());
      }
    }
  }
}
