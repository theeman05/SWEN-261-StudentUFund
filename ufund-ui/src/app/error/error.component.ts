// error.component.ts

import { Component, OnInit } from '@angular/core';
import { ErrorService } from '../error.service';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css'],
})
export class ErrorComponent implements OnInit {
  errorMessage: string = '';

  constructor(private errorService: ErrorService) {}

  ngOnInit() {
    this.errorService.error$.subscribe((message) => {
      this.errorMessage = message;
    });
  }

  clearError() {
    this.errorMessage = '';
    this.errorService.clearError();
  }
}
