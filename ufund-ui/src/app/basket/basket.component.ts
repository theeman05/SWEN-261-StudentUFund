import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { Need } from '../need';
import { Location } from '@angular/common';
import { ErrorService } from '../error.service';

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrls: ['./basket.component.css']
})
export class BasketComponent implements OnInit{
  needs: Need[] = []

  constructor(private userService: UserService, private location : Location, private errorService: ErrorService) {}

  private basketSubscribed(needs: Need[]){
    if (needs.length == 0){
      this.location.back();
      this.errorService.showError("Basket is empty");
      return;
    }
    this.needs = needs;
  }

  ngOnInit(): void {
    this.userService.getBasket().subscribe(needs => this.basketSubscribed(needs));
  }

  checkout(): void {
    this.userService.checkout().subscribe(success => {if (success) 
        window.location.href = "/supporter/basket/checkout";
      else{
        this.userService.getBasket().subscribe(needs => this.basketSubscribed(needs));
        this.errorService.showError("At least one Need in your basket has been updated. Your basket has been refreshed. Please try again.");
      }
    });
  }

  remove(need: Need): void {
    this.needs = this.needs.filter(n => n !== need);
    this.userService.removeFromBasket(need).subscribe();
    if (this.needs.length == 0) {
      this.location.back()
    }
  }
}
