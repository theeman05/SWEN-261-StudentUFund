import { Component, OnInit } from '@angular/core';
import { Need } from '../need';
import { UserService } from '../user.service';

@Component({
  selector: 'app-needs-supporter',
  templateUrl: './needs-supporter.component.html',
  styleUrls: ['./needs-supporter.component.css']
})
export class NeedsSupporterComponent implements OnInit {
  needs: Need[] = [];
  
  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.getNeeds();
  }

  getNeeds(): void {
    this.userService.getBasketableNeeds().subscribe(needs => this.needs = needs);
  }
}
