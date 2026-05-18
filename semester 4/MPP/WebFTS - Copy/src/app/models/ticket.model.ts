import { Show } from './show.model';

export interface Ticket {
  id: number;
  buyerName: string;
  numberOfSeats: number;
  purchaseDate: string;
  show: Show;
}
