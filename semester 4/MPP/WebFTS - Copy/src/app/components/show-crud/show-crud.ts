import { Component } from '@angular/core';
import {Show} from '../../models/show.model';
import {FestivalService} from '../../services/festival.service';
import {Venue} from '../../models/venue.model';
import {FormsModule} from '@angular/forms';
import {Observable} from 'rxjs';
import {AsyncPipe} from '@angular/common';

@Component({
  selector: 'app-show-crud',
  imports: [
    FormsModule,
    AsyncPipe
  ],
  templateUrl: './show-crud.html',
  styleUrl: './show-crud.css',
})
export class ShowCrud {
  error: string | null = null;
  success: string | null = null;

  // create
  newShow: ShowForm = { date: '', title: '', soldSeats: 0, venue: { id: 0 } };

  // update
  updateId: number = 0;
  updateShow: ShowForm = { date: '', title: '', soldSeats: 0, venue: { id: 0 } };

  // delete
  deleteId: number = 0;

  // get by id result
  foundShow$: Observable<Show> | null = null;
  findId: number = 0;


  constructor(private festivalService: FestivalService) {}

  private clear(): void {
    this.error = null;
    this.success = null;
  }

  create(): void {
    this.clear();
    this.festivalService.createShow(this.newShow as Partial<Show>).subscribe({
      next: (show) => {
        this.success = `Show created with id: ${show.id}`;
        this.newShow = { date: '', title: '', soldSeats: 0, venue: { id: 0 } as Venue };
      },
      error: (err) => this.error = err.error ?? err.message
    });
  }

  find(): void {
    this.clear();
    this.foundShow$ = this.festivalService.getById(this.findId);
  }

  update(): void {
    this.clear();
    this.festivalService.updateShow(this.updateId, this.updateShow as Partial<Show>).subscribe({
      next: (show) => {
        this.success = `Show ${show.id} updated`;
        this.updateShow = { date: '', title: '', soldSeats: 0, venue: { id: 0 } as Venue };
        this.updateId = 0;
      },
      error: (err) => this.error = err.error ?? err.message
    });
  }

  delete(): void {
    this.clear();
    this.festivalService.deleteShow(this.deleteId).subscribe({
      next: () => {
        this.success = `Show ${this.deleteId} deleted`;
        this.deleteId = 0;
      },
      error: (err) => this.error = err.error ?? err.message
    });
  }
}
