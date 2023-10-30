import { Component, OnInit } from '@angular/core';

import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Need } from '../need';
import { NeedService } from '../need.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-need-detail-supporter',
  templateUrl: './need-detail-supporter.component.html',
  styleUrls: ['./need-detail-supporter.component.css']
})
export class NeedDetailSupporterComponent {
  need: Need | undefined;

  constructor(
    private route: ActivatedRoute,
    private needService: NeedService,
    private location: Location,
    private userService: UserService
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

  addToBasket(need: Need): void {
    this.userService.addToBasket(need).subscribe(response => {
      if (response) {
        this.goBack();
      }
    });
  }
}
