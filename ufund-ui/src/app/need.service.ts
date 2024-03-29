import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { Need } from './need';
import { ErrorService, HttpErrors } from './error.service';

@Injectable({
  providedIn: 'root'
})
export class NeedService {

  private needsUrl = 'http://localhost:8080/needs';  // URL to web api

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient, private errorService: ErrorService) { }

  getNeeds(): Observable<Need[]> {
    return this.http.get<Need[]>(this.needsUrl)
      .pipe(catchError(this.handleError<Need[]>('getNeeds', [])));
  }

  /** GET need by name. Will 404 if name not found */
  getNeed(name: String): Observable<Need> {
    const url = `${this.needsUrl}/${name}`;
    return this.http.get<Need>(url).pipe(catchError(this.handleError<Need>(`getNeed name=${name}`)));
  }

  /* GET needs whose name contains search term */
  searchNeeds(term: string): Observable<Need[]> {
    if (!term.trim()) {
      // if not search term, return empty array.
      return of([]);
    }
    return this.http.get<Need[]>(`${this.needsUrl}/?name=${term}`).pipe(catchError(this.handleError<Need[]>('searchNeeds', [])));
  }

  //////// Save methods //////////

  /** POST: add a new need to the server */
  addNeed(need: Need): Observable<Need> {
    return this.http.post<Need>(this.needsUrl, need, this.httpOptions).pipe(catchError(this.handleError<Need>('addNeed')));
  }

  /** DELETE: delete the need from the server */
  deleteNeed(name: String): Observable<Need> {
    const url = `${this.needsUrl}/${name}`;
    return this.http.delete<Need>(url, this.httpOptions).pipe(catchError(this.handleError<Need>('deleteNeed')));
  }

  /** PUT: update the need on the server */
  updateNeed(need: Need): Observable<any> {
    return this.http.put(this.needsUrl, need, this.httpOptions).pipe(catchError(this.handleError<any>('updateNeed')));
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

      var display_message = "";
      if (error.status != HttpErrors.INTERNAL_SERVER_ERROR) {
        switch (operation) {
          case 'getNeed':
            display_message = "Need not found!";
            break;
          case 'addNeed':
            display_message = "Need with the given name already exists!";
            break;
          case 'deleteNeed':
            display_message = "Need not found!";
            break;
          case 'updateNeed':
            display_message = "Need not found!";
            break;
        }
      } else {
        display_message = "Server storage limit exceeded!";
      }

      this.errorService.showError(display_message);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
