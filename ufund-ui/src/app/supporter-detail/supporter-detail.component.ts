import { Component } from '@angular/core';
import { Receipt } from '../receipt';
import { ReceiptService } from '../receipt.service';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-supporter-detail',
  templateUrl: './supporter-detail.component.html',
  styleUrls: ['./supporter-detail.component.css']
})
export class SupporterDetailComponent {
  userReceipts: Receipt[] | undefined
  username: String = ''
  totalFunded: number | undefined

  constructor (private receiptService: ReceiptService, private route: ActivatedRoute, private location: Location) {}

  ngOnInit(): void {
    this.getReceipts();
  }

  goBack(): void {
    this.location.back();
  }

  getReceipts(): void {
    this.username = String(this.route.snapshot.paramMap.get('username'));
    this.receiptService.getNeedReceiptsOfUser(this.username).subscribe(userReceipts => this.userReceipts = userReceipts);
    this.receiptService.getUserFundingSum(this.username).subscribe(totalFunded => this.totalFunded = totalFunded);
  }
}
