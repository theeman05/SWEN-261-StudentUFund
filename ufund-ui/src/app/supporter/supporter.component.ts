import { Component } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { LoginComponent } from '../login/login.component';

@Component({
  selector: 'app-supporter',
  templateUrl: './supporter.component.html',
  styleUrls: ['./supporter.component.css']
})
export class SupporterComponent {
  username: string
  private userService: UserService

  constructor(userService: UserService) {
    this.userService = userService
    this.username = ''
  }
}
