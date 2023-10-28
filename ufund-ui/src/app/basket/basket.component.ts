import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NeedService } from '../need.service';

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrls: ['./basket.component.css']
})
export class BasketComponent{

  constructor(private needService: NeedService) {}

  checkout(): void {

  }

  remove(): void {

  }
}
