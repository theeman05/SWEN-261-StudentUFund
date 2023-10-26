import { Component, OnInit } from '@angular/core';
import { Need } from '../need';
import { NeedService } from '../need.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.css' ]
})
export class DashboardComponent implements OnInit {
  needs: Need[] = [];

  constructor(private needService: NeedService) { }

  ngOnInit(): void {
    this.getNeeds();
  }

  getNeeds(): void {
    this.needService.getNeeds()
      .subscribe(needs => this.needs = needs.slice(1, 5));
  }
}