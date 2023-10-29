import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NeedService } from '../need.service';
import { UserService } from '../user.service';
import { Need } from '../need';

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrls: ['./basket.component.css']
})
export class BasketComponent implements OnInit{
  needs: Need[] = []
  constructor(private needService: NeedService, private userService: UserService) {}
  ngOnInit(): void {
    this.userService.getBasket()
  }

  checkout(): void {

  }

  remove(need: Need): void {

  }
}
