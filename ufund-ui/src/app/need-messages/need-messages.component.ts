import { Component } from '@angular/core';
import { MessagesService } from '../messages.service';
import { NeedMessage } from '../need-message';

@Component({
  selector: 'app-need-messages',
  templateUrl: './need-messages.component.html',
  styleUrls: ['./need-messages.component.css']
})
export class NeedMessagesComponent {
  messages: NeedMessage[] = [];

  constructor(private messagesService: MessagesService) { }

  ngOnInit(): void {
    this.getMessages();
  } 

  getMessages(): void {
    this.messagesService.getMessages()
      .subscribe(messages => this.messages = messages);
  }

  deleteMessage(message: NeedMessage): void {
    this.messagesService.deleteMessage(message)
      .subscribe(() => this.getMessages());
  }
}
