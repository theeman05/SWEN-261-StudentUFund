import { Component } from '@angular/core';
import { ReceiptService } from '../receipt.service';
import { Receipt } from '../receipt';

@Component({
  selector: 'app-all-funded',
  templateUrl: './all-funded.component.html',
  styleUrls: ['./all-funded.component.css']
})
export class AllFundedComponent {
  receipts: Receipt[] = [];

  constructor (private receiptService: ReceiptService) { }
  
  ngOnInit(): void {
    this.getReceipts();
  }

  getReceipts(): void {
    this.receiptService.getNeedReceipts().subscribe(receipts => this.receipts = receipts);
  }
}
