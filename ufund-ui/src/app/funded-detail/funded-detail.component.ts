import { Component, OnInit } from '@angular/core';

import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { ErrorService } from '../error.service';
import { MessagesService } from '../messages.service';
import { NeedMessage } from '../need-message';

@Component({
  selector: 'app-funded-detail',
  templateUrl: './funded-detail.component.html',
  styleUrls: ['./funded-detail.component.css']
})
export class FundedDetailComponent {
  message: NeedMessage | undefined;
  need_name: string = '';
  receiver_username: string = '';

  constructor(
    private route: ActivatedRoute,
    private messagesService: MessagesService,
    private location: Location,
    private errorService: ErrorService
  ) {}

  ngOnInit(): void {
    this.getMessage();
  }
  
  getMessage(): void {
    this.receiver_username = String(this.route.snapshot.paramMap.get('to_username'));
    this.need_name = String(this.route.snapshot.paramMap.get('need_name'));
    this.messagesService.getMessageToUser(this.receiver_username, this.need_name)
      .subscribe(message => this.message = message || {sender_username: '', need_name: '', message: ''});
  }

  goBack(): void {
    this.location.back();
  }

  updateMessage(): void {
    if (this.message) {
      if (this.message.message)
        this.messagesService.sendMessage(this.message.message, this.need_name, this.receiver_username).subscribe(() => this.goBack());
      else
        this.errorService.showError("Please enter a message.");
    }
  }
}
