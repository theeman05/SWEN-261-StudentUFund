import { Component, OnInit } from '@angular/core';
import { Receipt } from '../receipt';
import { UserService } from '../user.service';
import { ErrorService } from '../error.service';
import { ReceiptService } from '../receipt.service';

@Component({
  selector: 'app-funding-leaderboard',
  templateUrl: './funding-leaderboard.component.html',
  styleUrls: ['./funding-leaderboard.component.css']
})
export class FundingLeaderboardComponent implements OnInit{
  receipts: Receipt[] = []

  constructor(private receiptService: ReceiptService) {}
  
  ngOnInit(): void {
    this.receiptService.getSortedNeedReceipts()
  }

}
