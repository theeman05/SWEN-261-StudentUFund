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

  constructor(private userService: UserService) { }

  login(username: string): void {
    username = username.trim();
    if (!username) { return; }
    if (this.userService.loginUser({ username } as User).subscribe())
      if (username == LoginComponent.ADMIN_USERNAME)
        window.location.href = "/admin/needs";
      else
        window.location.href = "/supporter/needs";
  }
}
