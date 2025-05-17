import {Component, DestroyRef} from '@angular/core';
import {BehaviorSubject, catchError, combineLatest, forkJoin, map, mergeMap, Observable, of} from 'rxjs';
import {LucideAngularModule, LucideChevronDown , LucideX} from 'lucide-angular';
import {Router, RouterLink} from '@angular/router';
import {HotToastService} from '@ngxpert/hot-toast';
import {MovieService} from '../../services/movie/movie.service';
import {AuthService} from '../../services/auth/auth.service';
import {TmdbService} from '../../services/tmdb/tmdb.service';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {AsyncPipe, NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-watchlist',
  imports: [
    LucideAngularModule,
    NgIf,
    AsyncPipe,
    RouterLink,
    NgForOf
  ],
  templateUrl: './watchlist.component.html',
  standalone: true,
  styleUrl: './watchlist.component.css'
})
export class WatchlistComponent {

  protected readonly LucideX = LucideX;
  protected readonly LucideChevronDown = LucideChevronDown;

  isDecadeDropDownOpen: boolean = false;
  isGenreDropDownOpen: boolean = false;
  isSortDropDownOpen: boolean = false;

  watchedMovies$ !: Observable<any[]>
  filteredMovies$ !: Observable<any>

  private selectedDecade$ = new BehaviorSubject<string | null>(null);
  private selectedGenre$ = new BehaviorSubject<string | null>(null);
  private selectedSort$ = new BehaviorSubject<string | null>('Added First');

  errorMessage !: string;

  constructor(
    private router : Router,
    private hotToast : HotToastService,
    private movieService : MovieService,
    private authService : AuthService,
    private destroyRef : DestroyRef,
    private tmdbService : TmdbService
  ) {}

  ngOnInit(): void {
    this.watchedMovies$ = this.getWatchedMoviesPosters();
    this.watchedMovies$.subscribe(res => {
      console.log(res);
    })

    this.filteredMovies$ = combineLatest([
      this.watchedMovies$,
      this.selectedDecade$,
      this.selectedGenre$,
      this.selectedSort$
    ]).pipe(
      map(([movies, selectedDecade, selectedGenre, selectedSort]) => {
        let filteredMovies = movies;

        // Filter by Decade
        if (selectedDecade) {
          const startYear = parseInt(selectedDecade);
          const startDate = new Date(startYear, 0, 1); // January 1st of the decade
          const endDate = new Date(startYear + 10, 0, 1); // January 1st of the next decade
          filteredMovies = filteredMovies.filter((movie: any) => {
            const releaseDate = new Date(movie.details.release_date);
            return releaseDate >= startDate && releaseDate < endDate;
          });
        }

        // Filter by Genre
        if (selectedGenre) {
          filteredMovies = filteredMovies.filter((movie: any) => {
            return movie.details.genres.some((genre: any) => genre.name === selectedGenre);
          });
        }

        // Sort by Selected Option
        if (selectedSort === 'Added First') {
          filteredMovies = filteredMovies.sort((a, b) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime());
        } else if (selectedSort === 'Added Last') {
          filteredMovies = filteredMovies.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
        } else if (selectedSort === 'Highest First') {
          filteredMovies = filteredMovies.sort((a, b) => b.details.vote_average - a.details.vote_average);
        } else if (selectedSort === 'Lowest First') {
          filteredMovies = filteredMovies.sort((a, b) => a.details.vote_average - b.details.vote_average);
        } else if (selectedSort === 'Shortest First') {
          filteredMovies = filteredMovies.sort((a, b) => a.details.runtime - b.details.runtime);
        } else if (selectedSort === 'Longest First') {
          filteredMovies = filteredMovies.sort((a, b) => b.details.runtime - a.details.runtime);
        }

        this.isSortDropDownOpen = false;
        this.isDecadeDropDownOpen = false;
        this.isGenreDropDownOpen = false;

        return filteredMovies;
      }),
      catchError(err => {
        console.error('Error filtering movies', err);
        this.errorMessage = 'Error filtering movies';
        return of([]);
      })
    );

  }


  selectDecade(decade: string | null): void {
    this.selectedDecade$.next(decade);
  }

  selectGenre(genre: string | null): void {
    this.selectedGenre$.next(genre);
  }

  selectSort(sort: string): void {
    this.selectedSort$.next(sort);
  }

  resetFilters(): void {
    this.selectedDecade$.next(null);
    this.selectedGenre$.next(null);
    this.selectedSort$.next('Added First');
  }

  filtersCheck() : boolean {
    return this.selectedDecade$.getValue() !== null || this.selectedGenre$.getValue() !== null || this.selectedSort$.getValue() !== 'Added First';
  }

  toggleDropDown(type: string) {
    switch (type){
      case 'decade':
        this.isDecadeDropDownOpen = !this.isDecadeDropDownOpen;
        this.isGenreDropDownOpen = false;
        this.isSortDropDownOpen = false;
        break;
      case 'genre':
        this.isGenreDropDownOpen = !this.isGenreDropDownOpen;
        this.isDecadeDropDownOpen = false;
        this.isSortDropDownOpen = false;
        break;
      case 'sort':
        this.isSortDropDownOpen = !this.isSortDropDownOpen;
        this.isDecadeDropDownOpen = false;
        this.isGenreDropDownOpen = false;
        break;
    }
  }

  getWatchedMoviesPosters(): Observable<any> {
    return this.movieService.getWatchedOrWatchlist(this.authService.getCurrentUser()?._id, 'WATCHLIST').pipe(
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
