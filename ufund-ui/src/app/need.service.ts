import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

import { Need } from './need';
import { NEEDS } from './mock-needs';

@Injectable({
  providedIn: 'root'
})
export class NeedService {
  constructor() { }

  getNeeds(): Observable<Need[]> {
    const needs = of(NEEDS);
    return needs;
  }

  /** GET: get the need from the server */
  getNeed(name: string): Observable<Need> {
    const need = NEEDS.find(n => n.name === name)!;
    return of(need);
  }

  // /** DELETE: delete the need from the server */
  // deleteNeed(id: number): Observable<Need> {
  //   const url = `${this.needsURL}/${id}`;

  //   return this.http.delete<Need>(url, this.httpOptions).pipe(
  //     tap(_ => this.log(`deleted hero id=${id}`)),
  //     catchError(this.handleError<Hero>('deleteHero'))
  //   );
  // }
}
