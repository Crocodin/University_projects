import { Venue } from './venue.model';

export interface Show {
  id: number;
  date: string;
  title: string;
  soldSeats: number;
  venue: Venue;
  readonly soldOut: boolean;
}
