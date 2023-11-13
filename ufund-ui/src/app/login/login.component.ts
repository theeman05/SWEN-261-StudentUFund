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
  errorMessage: string = '';
  userName: string = '';

  constructor(private userService: UserService) {}

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

  signup(username: string): void {
    username = username.trim();
    if (!username) { return; }
    this.userService.signupUser({ username } as User).subscribe(user => {if (user) this.login(username)});
  }

  verifyKeyPressed(ev: KeyboardEvent, username: string): void {
    if (ev.key == "Enter")
      this.login(username)
  }
}
