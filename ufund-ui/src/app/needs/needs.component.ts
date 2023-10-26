import { Component } from '@angular/core';
import { NEEDS } from '../mock-needs';

@Component({
  selector: 'app-needs',
  templateUrl: './needs.component.html',
  styleUrls: ['./needs.component.css']
})
export class NeedsComponent {
  needs = NEEDS;
}
