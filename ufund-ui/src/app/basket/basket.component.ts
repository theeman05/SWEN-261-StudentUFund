import { Component, OnInit } from '@angular/core';
import { Basket } from './basket';
import { ActivatedRoute } from '@angular/router';
import { NeedService } from '../need.service';

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrls: ['./basket.component.css']
})
export class BasketComponent{
  // basket: Basket;
  constructor(
    private route: ActivatedRoute,
    private needService: NeedService,
    private location: Location

  ) {}

  checkout(): void {

  }

  remove(): void {

  }
}
