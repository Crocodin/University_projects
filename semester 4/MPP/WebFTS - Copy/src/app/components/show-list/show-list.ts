import {Component} from '@angular/core';
import {ShowArtist} from '../../models/show-artist.model';
import {Show} from '../../models/show.model';
import {FestivalService} from '../../services/festival.service';
import {FormsModule} from '@angular/forms';
import {RouterLink} from '@angular/router';
import {BehaviorSubject, switchMap} from 'rxjs';
import {AsyncPipe} from '@angular/common';

@Component({
  selector: 'app-show-list',
  imports: [
    FormsModule,
    RouterLink,
    AsyncPipe
  ],
  templateUrl: './show-list.html',
  styleUrl: './show-list.css',
})
export class ShowList {
  // triggers a reload every time next() is called
  private refresh$ = new BehaviorSubject<void>(undefined);

  showArtists$ = this.refresh$.pipe(
    switchMap(() => this.festivalService.getAll())
  );

  filteredShowArtists: ShowArtist[] = [];
  error: string | null = null;

  filterDate: string = '';

  selectedShow: Show | null = null;
  buyerName: string = '';
  seats: number = 0;
  sellError: string | null = null;
  sellSuccess: string | null = null;

  modifyTicketId: number = 0;
  modifySeats: number = 0;
  modifyError: string | null = null;
  modifySuccess: string | null = null;

  constructor(private festivalService: FestivalService) {}

  refresh(): void {
    this.refresh$.next();
  }

  applyDateFilter(): void {
    if (!this.filterDate) {
      this.filteredShowArtists = [];
      return;
    }
    this.festivalService.getByDate(this.filterDate).subscribe({
      next: (data) => this.filteredShowArtists = data,
      error: (err) => this.error = err.message
    });
  }

  clearFilter(): void {
    this.filterDate = '';
    this.filteredShowArtists = [];
  }

  selectShow(show: Show): void {
    this.selectedShow = show;
    this.sellError = null;
    this.sellSuccess = null;
  }

  sellTicket(): void {
    if (!this.selectedShow) return;
    this.festivalService.sellTicket(this.selectedShow.id, this.buyerName, this.seats).subscribe({
      next: () => {
        this.sellSuccess = 'Ticket sold!';
        this.sellError = null;
        this.buyerName = '';
        this.seats = 0;
        this.refresh(); // ← re-fetches data
      },
      error: (err) => {
        this.sellError = err.error ?? err.message;
        this.sellSuccess = null;
      }
    });
  }

  modifyTicket(): void {
    this.festivalService.modifyTicket(this.modifyTicketId, this.modifySeats).subscribe({
      next: () => {
        this.modifySuccess = 'Ticket modified!';
        this.modifyError = null;
        this.modifyTicketId = 0;
        this.modifySeats = 0;
        this.refresh(); // ← re-fetches data
      },
      error: (err) => {
        this.modifyError = err.error ?? err.message;
        this.modifySuccess = null;
      }
    });
  }
}
