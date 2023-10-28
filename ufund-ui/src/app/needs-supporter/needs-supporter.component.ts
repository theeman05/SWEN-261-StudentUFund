import { Component, OnInit } from '@angular/core';
import { Need } from '../need';
import { NeedService } from '../need.service';

@Component({
  selector: 'app-needs-supporter',
  templateUrl: './needs-supporter.component.html',
  styleUrls: ['./needs-supporter.component.css']
})
export class NeedsSupporterComponent implements OnInit {
  needs: Need[] = [];
  
  constructor(private needService: NeedService) { }

  ngOnInit(): void {
    this.getNeeds();
  }

  getNeeds(): void {
    this.needService.getNeeds().subscribe(needs => this.needs = needs);
  }
}
