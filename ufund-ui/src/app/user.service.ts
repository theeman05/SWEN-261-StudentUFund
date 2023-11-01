import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { Need } from './need';
import { User } from './user';
import { ErrorService, HttpErrors } from './error.service';
import { Location } from '@angular/common';
import { NeedService } from './need.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userUrl = 'http://localhost:8080/users';  // URL to web api

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient, private errorService: ErrorService, private location: Location) { }

  /** Login a user */
  loginUser(user: User): Observable<User> {
    return this.http.get<User>(`${this.userUrl}/${user.username}`)
      .pipe(catchError(this.handleError<User>('loginUser')));
  }

  /** Logout current user */
  logout(): Observable<Boolean>{
    return this.http.get<Boolean>(`${this.userUrl}/logout`);
  }

  /** GET basket of logged in user */
  getBasket(): Observable<Need[]> {
    return this.http.get<Need[]>(`${this.userUrl}/basket`)
      .pipe(catchError(this.handleError<Need[]>('getBasket', [])));
  }

  /** GET needs for the logged in user, excluding basket needs */
  getBasketableNeeds(): Observable<Need[]> {
    return this.http.get<Need[]>(`${this.userUrl}/basketable`)
      .pipe(catchError(this.handleError<Need[]>('getBasketableNeeds', [])));
  }

  /** GET checkout the current user */
  checkout(): Observable<void> {
    return this.http.get<void>(`${this.userUrl}/checkout`)
      .pipe(catchError(this.handleError<void>('checkout')));
  }

  /** try to GET need in cart by name */
  getNeedInCart(name: String): Observable<Need> {
    const url = `${this.userUrl}/basket/${name}`;
    return this.http.get<Need>(url).pipe(catchError(this.handleError<Need>(`getBasketNeed name=${name}`)));
  }

  //////// Save methods //////////

  /** PUT: update a need in the basket */
  updateBasket(need: Need): Observable<any> {
    return this.http.put(`${this.userUrl}/basket`, need, this.httpOptions).pipe(catchError(this.handleError<any>('updateBasket')));
  }

  /** DELETE: delete the need from the basket */
  removeFromBasket(need: Need): Observable<void> {
    return this.http.delete<void>(`${this.userUrl}/basket/${need.name}`, this.httpOptions).pipe(catchError(this.handleError<void>('removeFromBasket')));
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

      var display_message;
      if (error.status == HttpErrors.INTERNAL_SERVER_ERROR) {
        display_message = "Server storage limit exceeded!";
      } else if (error.status == HttpErrors.FORBIDDEN){
        this.location.back();
      } else if (error.status == HttpErrors.NOT_FOUND){
        if (operation == 'loginUser'){
          display_message = "Username not found!";
        }else{
          display_message = "Need not found!"; // Idk how this would happen
        }
      } else {
        switch (operation) {
          case 'addToBasket':
            display_message = "Need already in basket!";
            break;
          case 'removeFromBasket':
            display_message = "Need not in basket!";
            break;
          case 'checkout':
            display_message = "Checkout failed!";
            break;
          default:
            display_message = "Unknown error!";
            break;
        }
      }

      if (display_message)
        this.errorService.showError(display_message);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
