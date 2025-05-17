import {Component, DestroyRef, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth/auth.service';
import {MovieService} from '../../services/movie/movie.service';
import {catchError, forkJoin, map, mergeMap, Observable, of, throwError} from 'rxjs';
import {AsyncPipe, NgForOf, NgIf} from '@angular/common';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {TmdbService} from '../../services/tmdb/tmdb.service';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [
    AsyncPipe,
    NgIf,
    NgForOf,
    RouterLink
  ],
  templateUrl: './home.component.html',
  standalone: true,
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit{

  tenWatchedMovies$ !: Observable<any>;
  errorMessage !: string;

  constructor(
    protected authService : AuthService ,
    private movieService : MovieService,
    private destroyRef : DestroyRef,
    private tmdbService : TmdbService
  ) {}

  ngOnInit(): void {
    this.tenWatchedMovies$ = this.getWatchedMoviesPosters();
  }

  getWatchedMoviesPosters(): Observable<any> {
    return this.movieService.getTenWatchedMovies(this.authService.getCurrentUser()?._id).pipe(
      takeUntilDestroyed(this.destroyRef),
      mergeMap((movies: any) => {
        if (!movies || movies.length === 0) {
          return of([]);
        }
        return forkJoin(
          movies.map((movieTracker: any) =>
            this.tmdbService.getMovie(movieTracker.movieDTO.id).pipe(
              map(movieDetails => ({
                ...movieTracker,
                details: movieDetails
              })),
              catchError(err => {
                this.errorMessage = 'Error fetching movie details';
                console.error('Error fetching watched movies details', err);
                return of(null);
              })
            )
          )
        );
      }),
      catchError(err => {
        console.error('Error fetching watched movies', err);
        this.errorMessage = 'Error fetching watched movies';
        return of([]);
      })
    );
  }



}
