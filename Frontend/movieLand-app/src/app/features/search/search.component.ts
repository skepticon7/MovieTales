import {Component, DestroyRef, OnInit} from '@angular/core';
import {SidebarComponent} from '../../core/sidebar/sidebar.component';
import {NavbarComponent} from '../../core/navbar/navbar.component';
import {AsyncPipe, NgForOf, NgIf} from '@angular/common';
import {TmdbService} from '../../services/tmdb/tmdb.service';
import {HotToastService} from '@ngxpert/hot-toast';
import {catchError, forkJoin, from, map, mergeMap, Observable, of, toArray} from 'rxjs';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {ActivatedRoute, RouterLink} from '@angular/router';

@Component({
  selector: 'app-search',
  imports: [SidebarComponent, NavbarComponent, NgForOf, NgIf, AsyncPipe, RouterLink],
  templateUrl: './search.component.html',
  standalone: true,
  styleUrl: './search.component.css'
})
export class SearchComponent implements OnInit{
  descriptionMock : string = "Dom Cobb (DiCaprio) steals secrets from dreams. To clear his past, he attempts inception—planting an idea in a target’s mind. As dreams layer deeper, Cobb battles guilt over his wife (Cotillard). Ends ambiguously: is he still dreaming?"
  mockData = [
    {id : 1 , title : "Inception" , year : "2010" , description :this.descriptionMock , director : "Christopher Nolan" , poster : "inception.png"},
    {id : 2 , title : "Inception" , year : "2010" , description :this.descriptionMock , director : "Christopher Nolan" , poster : "inception.png"},
    {id : 3 ,title : "Inception" , year : "2010" , description :this.descriptionMock , director : "Christopher Nolan" , poster : "inception.png"},
    {id : 4 ,title : "Inception" , year : "2010" , description :this.descriptionMock , director : "Christopher Nolan" , poster : "inception.png"}
  ]

  movies$ !: Observable<any>
  errorMessage !: string;
  keyword !: string;

  ngOnInit(): void {
    this.activatedRoute.queryParams.pipe(
      takeUntilDestroyed(this.destroyRef),
    ).subscribe(params => {
      this.keyword = params['q'];
      this.movies$ = this.getMoviesWithDirector(this.keyword);
      this.movies$.subscribe(res => {
        console.log(res);
      });
    })
  }

  constructor(
    private destroyRef : DestroyRef,
    private tmdbService : TmdbService ,
    private hotToast : HotToastService,
    private activatedRoute : ActivatedRoute) {
  }

  searchMovie(keyword : string) {
    return this.tmdbService.searchMovie(keyword).pipe(
      map((response) => response.results.slice(0 , 10).filter((movieMapped : any) => movieMapped.poster_path !== null)),
      catchError(err => {
        console.error(`Error searching for movie named ${keyword}`);
        return of([]);
      })
    )
  }

  searchDirector(movieId : number) {
    return this.tmdbService.searchMovieCredits(movieId).pipe(
      map((response) => {
        const crew = response.crew;
        const director = crew.find((person: any) => person.job === 'Director');
        return director ? director.name : 'Unknown';
      }),
      catchError(err => {
        console.error(`Error searching for credits of movie ${movieId}`);
        return of('Unknown');
      })
    )
  }

  searchMovieAlternativeTitles(movieId : number) {
    return this.tmdbService.searchMovieAlternativeTitles(movieId).pipe(
      map((response) => {
        return response.titles.slice(0,10).map((titleMapped : any) => {
          return titleMapped.title;
        })
      }),
      catchError(err => {
        console.error(`Error fetching alternative titles for movie ${movieId}`);
        return of([]);
      })
    )
  }

  getMoviesWithDirector(keyword: string): Observable<any[]> {
    return this.searchMovie(keyword).pipe(
      takeUntilDestroyed(this.destroyRef),
      mergeMap((movies: any[]) => {
        if (!movies || movies.length === 0) {
          return of([]);
        }

        return forkJoin(
          movies.map(movie =>
            forkJoin({
              director: this.searchDirector(movie.id),
              alternativeTitles: this.searchMovieAlternativeTitles(movie.id)
            }).pipe(
              map(({ director, alternativeTitles }) => ({
                ...movie,
                director,
                alternativeTitles
              }))
            )
          )
        );
      }),
      catchError(error => {
        console.error('Error in getMoviesWithDirector:', error);
        return of([]);
      })
    );
  }


  private mapMovie(observable: Observable<any>) : Observable<any> {
    return of(null);
  }
}
