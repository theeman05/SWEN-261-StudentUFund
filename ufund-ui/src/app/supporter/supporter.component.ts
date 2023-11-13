import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { LoginComponent } from '../login/login.component';

@Component({
  selector: 'app-supporter',
  templateUrl: './supporter.component.html',
  styleUrls: ['./supporter.component.css']
})
export class SupporterComponent {
  username: string = ''

  constructor(private userService: UserService) {}

  ngOnInit(): void {
  this.userService.getUsername().subscribe(user => this.username = user.username)
  }
}
