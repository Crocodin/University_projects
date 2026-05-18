import { Show } from './show.model';
import { Artist } from './artist.model';

export interface ShowArtist {
  id: number;
  show: Show;
  artist: Artist;
}
