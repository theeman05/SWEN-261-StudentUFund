import { Component, OnInit } from '@angular/core';
import { Receipt } from '../receipt';
import { UserService } from '../user.service';
import { ErrorService } from '../error.service';
import { ReceiptService } from '../receipt.service';
import { map } from 'rxjs';

@Component({
  selector: 'app-funding-leaderboard',
  templateUrl: './funding-leaderboard.component.html',
  styleUrls: ['./funding-leaderboard.component.css']
})
export class FundingLeaderboardComponent implements OnInit{
  funding: String[] = []

  constructor(private receiptService: ReceiptService) {}
  
  ngOnInit(): void {
    this.getFunding()
  }

  getFunding() {
    return this.receiptService.getAllUserFunding().subscribe(funding => this.funding = funding)
  }

  getFundedName(stringEntry: String): String {
    return stringEntry.split("=")[0]
  }

  getFundedSum(stringEntry: String): String {
    return stringEntry.split("=")[1]
  }
}
