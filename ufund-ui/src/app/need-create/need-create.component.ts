import { Component, OnInit } from '@angular/core';
import { Need } from '../need';
import { NeedService } from '../need.service';

@Component({
  selector: 'app-need-create',
  templateUrl: './need-create.component.html',
  styleUrls: ['./need-create.component.css']
})
export class NeedCreateComponent{
  constructor(
    private needService: NeedService,
  ) {}

  createNeed(name: string, cost: string, quantity: string): void {
    name = name.trim();
    if (name && cost && quantity) {
      this.needService.addNeed({name: name, cost: Number(cost), quantity: Number(quantity)} as Need).subscribe()
    }
  }
}
