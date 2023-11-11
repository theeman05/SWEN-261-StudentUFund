import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ErrorService, HttpErrors } from './error.service';
import { Observable, catchError } from 'rxjs';
import { Receipt } from './receipt';

@Injectable({
  providedIn: 'root'
})
export class ReceiptService {
  private receiptsUrl = 'http://localhost:8080/receipts';  // URL to web api

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient, private errorService: ErrorService) { }

  getNeedReceipt(username: String, name: String): Observable<Receipt> {
    const url = `${this.receiptsUrl}/${username}/${name}`
    return this.http.get<Receipt>(url)
  }

  getNeedReceipts(): Observable<Receipt[]> {
    return this.http.get<Receipt[]>(this.receiptsUrl)
  }

  getNeedReceiptsOfUser(username: String): Observable<Receipt[]> {
    const url = `${this.receiptsUrl}/${username}`
    return this.http.get<Receipt[]>(url)
  }

  getSortedNeedReceipts():Observable<Receipt[]> {
    const url = `${this.receiptsUrl}/sorted`
    return this.http.get<Receipt[]>(url)
  }
}
