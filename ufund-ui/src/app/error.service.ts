import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

export const HttpErrors = {
  INTERNAL_SERVER_ERROR: 500,
  NOT_FOUND: 404,
  OK: 200,
  CONFLICT: 409,
  FORBIDDEN: 403,
};

@Injectable({
  providedIn: 'root'
})
export class ErrorService {
  private errorSubject = new Subject<string>();

  error$ = this.errorSubject.asObservable();

  showError(message: string) {
    this.errorSubject.next(message);
  }

  clearError() {
    this.errorSubject.next('');
  }
}