import { Component } from '@angular/core';
import { UserService } from '../user.service';
import { User } from '../user';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  private static ADMIN_USERNAME = "admin";
  userName: string
  constructor(private userService: UserService) { this.userName = "test" }

  login(username: string): void {
    this.userName = username
    username = username.trim();
    if (!username) { return; }

    this.userService.loginUser({ username } as User).subscribe(user => {
      if (user){
        if (username == LoginComponent.ADMIN_USERNAME)
          window.location.href = "/admin/needs";
        else
          window.location.href = "/supporter/needs";
      }
    });
  }
}
