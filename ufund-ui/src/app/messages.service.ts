import { Injectable } from '@angular/core';
import { ErrorService, HttpErrors } from './error.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NeedMessage } from './need-message';
import { Observable, catchError, of } from 'rxjs';

import { Location } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class MessagesService {
  private userUrl = 'http://localhost:8080/users';  // URL to web api
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient, private errorService: ErrorService, private location: Location) {}

  /** GET messages for the logged in user */
  getMessages(): Observable<NeedMessage[]> {
    return this.http.get<NeedMessage[]>(`${this.userUrl}/inbox`)
      .pipe(catchError(this.handleError<NeedMessage[]>('getMessages', [])));
  }

  /** POST message */
  sendMessage(messageText: string, needName: string, toUser: string): Observable<void> {
    var message: NeedMessage = {
      sender_username: toUser,
      need_name: needName,
      message: messageText
    }

    return this.http.post<void>(`${this.userUrl}/inbox`, message, this.httpOptions).pipe(catchError(this.handleError<void>('sendMessage')));
  }

  /** DELETE message */
  deleteMessage(message: NeedMessage): Observable<void> {
    const url = `${this.userUrl}/inbox/${message.need_name}`;
    return this.http.delete<void>(url, this.httpOptions).pipe(catchError(this.handleError<void>('deleteMessage')));
  }

  /** GET message to a user */
  getMessageToUser(username: string, needName: string): Observable<NeedMessage> {
    const url = `${this.userUrl}/${username}/inbox/${needName}`;
    return this.http.get<NeedMessage>(url).pipe(catchError(this.handleError<NeedMessage>(`getMessageToUser`)));
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
      }

      if (display_message)
        this.errorService.showError(display_message);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
