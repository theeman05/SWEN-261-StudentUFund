import { Component, OnInit } from '@angular/core';

import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Need } from '../need';
import { NeedService } from '../need.service';
import { UserService } from '../user.service';
import { ErrorService } from '../error.service';

@Component({
  selector: 'app-need-detail-supporter',
  templateUrl: './need-detail-supporter.component.html',
  styleUrls: ['./need-detail-supporter.component.css']
})
export class NeedDetailSupporterComponent {
  need: Need | undefined;

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private userService: UserService,
    private errorService: ErrorService
  ) {}

  ngOnInit(): void {
    this.getNeed();
  }

  getNeed(): void {
    const name = String(this.route.snapshot.paramMap.get('name'));
    this.userService.getNeedInCart(name).subscribe(need => this.need = need);
  }

  goBack(): void {
    this.location.back();
  }

  updateBasket(need: Need): void {
    if (need.quantity > 0 && need.quantity <= need.stock && need.quantity % 1 == 0) {
      need.quantity = Number(need.quantity);
      this.userService.updateBasket(need).subscribe(_ => this.goBack());
    }else{
      this.errorService.showError("Please enter a valid quantity.");
    }
  }
}
