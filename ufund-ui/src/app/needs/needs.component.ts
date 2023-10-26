import { Component, OnInit } from '@angular/core';
import { Need } from '../need';
import { NeedService } from '../need.service';

@Component({
  selector: 'app-needs',
  templateUrl: './needs.component.html',
  styleUrls: ['./needs.component.css']
})
export class NeedsComponent implements OnInit {
  needs: Need[] = [];
  
  constructor(private needService: NeedService) { }

  ngOnInit(): void {
    this.getNeeds();
  }

  getNeeds(): void {
    this.needService.getNeeds().subscribe(needs => this.needs = needs);
  }

  delete(hero: Need): void {
    this.needs = this.needs.filter(h => h !== hero);
    //this.needService.deleteNeed(hero.name).subscribe();
  }
}
