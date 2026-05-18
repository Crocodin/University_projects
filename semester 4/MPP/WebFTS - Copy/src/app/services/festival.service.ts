import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { ShowArtist } from '../models/show-artist.model';
import { Show } from '../models/show.model';
import { Ticket } from '../models/ticket.model';

@Injectable({
  providedIn: 'root'
})
export class FestivalService {
  private readonly base = '/fts/show';

  constructor(private http: HttpClient) {}

  // show crud
  getAll(): Observable<ShowArtist[]> {
    return this.http.get<ShowArtist[]>(this.base).pipe(
      tap(() => console.log('GET /fts/show')),
      catchError(this.handleError('getAll'))
    );
  }

  getById(id: number): Observable<Show> {
    return this.http.get<Show>(`${this.base}/${id}`).pipe(
      tap(() => console.log(`GET /fts/show/${id}`)),
      catchError(this.handleError('getById'))
    );
  }

  getByDate(date: string): Observable<ShowArtist[]> {
    return this.http.get<ShowArtist[]>(`${this.base}/date/${date}`).pipe(
      tap(() => console.log(`GET /fts/show/date/${date}`)),
      catchError(this.handleError('getByDate'))
    );
  }

  createShow(show: Partial<Show>): Observable<Show> {
    return this.http.post<Show>(this.base, show).pipe(
      tap(() => console.log('POST /fts/show')),
      catchError(this.handleError('createShow'))
    );
  }

  updateShow(id: number, show: Partial<Show>): Observable<Show> {
    return this.http.put<Show>(`${this.base}/${id}`, show).pipe(
      tap(() => console.log(`PUT /fts/show/${id}`)),
      catchError(this.handleError('updateShow'))
    );
  }

  deleteShow(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`).pipe(
      tap(() => console.log(`DELETE /fts/show/${id}`)),
      catchError(this.handleError('deleteShow'))
    );
  }

  // ticket
  sellTicket(showId: number, buyerName: string, seats: number): Observable<Ticket> {
    const params = new HttpParams()
      .set('buyerName', buyerName)
      .set('seats', seats);
    return this.http.post<Ticket>(`${this.base}/${showId}/ticket`, null, { params }).pipe(
      tap(() => console.log(`POST /fts/show/${showId}/ticket`)),
      catchError(this.handleError('sellTicket'))
    );
  }

  modifyTicket(ticketId: number, seats: number): Observable<void> {
    const params = new HttpParams().set('seats', seats);
    return this.http.patch<void>(`${this.base}/ticket/${ticketId}`, null, { params }).pipe(
      tap(() => console.log(`PATCH /fts/show/ticket/${ticketId}`)),
      catchError(this.handleError('modifyTicket'))
    );
  }

  // errors
  private handleError(operation: string) {
    return (error: any): Observable<never> => {
      console.error(`${operation} failed:`, error);
      throw error;
    };
  }
}
