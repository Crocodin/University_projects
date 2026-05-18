import {Component, OnInit} from '@angular/core';
import {FestivalService} from '../../services/festival.service';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {Show} from '../../models/show.model';
import {Observable} from 'rxjs';
import {AsyncPipe} from '@angular/common';

@Component({
  selector: 'app-show-details',
  imports: [
    RouterLink,
    AsyncPipe
  ],
  templateUrl: './show-details.html',
  styleUrl: './show-details.css',
})
export class ShowDetail implements OnInit {
  show$!: Observable<Show>;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private festivalService: FestivalService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.show$ = this.festivalService.getById(id);
  }
}
