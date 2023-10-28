import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { Need } from './need';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userUrl = 'http://localhost:8080/users';  // URL to web api

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  /** Login a user */
  loginUser(user: User): Observable<User> {
    return this.http.get<User>(`${this.userUrl}/${user.username}`)
      .pipe(catchError(this.handleError<User>('loginUser')));
  }

  /** Logout current user */
  logout(): Observable<Boolean>{
    return this.http.get<Boolean>("/logout");
  }

  /** GET basket of logged in user */
  getBasket(): Observable<String[]> {
    return this.http.get<String[]>("/basket")
      .pipe(catchError(this.handleError<String[]>('getBasket', [])));
  }

  //////// Save methods //////////

  /** POST: add a new need to the basket */
  addToBasket(need: Need): Observable<Need> {
    return this.http.post<Need>("/basket", need.name, this.httpOptions).pipe(catchError(this.handleError<Need>('addToBasket')));
  }

  /** DELETE: delete the need from the basket */
  removeFromBasket(need: Need): Observable<void> {
    return this.http.delete<void>(`${this.userUrl}/${need.name}`, this.httpOptions).pipe(catchError(this.handleError<void>('removeFromBasket')));
  }

  /**
 * Handle Http operation that failed.
 * Let the app continue.
 *
 * @param operation - name of the operation that failed
 * @param result - optional value to return as the observable result
 */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      console.error(error); // log to console

      // TODO: better job of transforming error for user consumption

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
