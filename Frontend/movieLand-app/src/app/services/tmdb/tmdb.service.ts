import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TmdbService {

  constructor(private http : HttpClient) { }

  getMovieDetails(movieId : number) : Observable<any> {
    return this.http.get<any>(`https://api.themoviedb.org/3/movie/${movieId}?api_key=${environment.TmdbApiKey}&language=en-US`);
  }

  searchMovie(query : string) : Observable<any>  {
    return this.http.get<any>(`https://api.themoviedb.org/3/search/movie?api_key=${environment.TmdbApiKey}&query=${query}`);
  }

  getMovie(movieId : number) : Observable<any> {
    return this.http.get(`https://api.themoviedb.org/3/movie/${movieId}?api_key=${environment.TmdbApiKey}`);
  }

  searchMovieCredits(movieId : number) : Observable<any>{
    return this.http.get<any>(`https://api.themoviedb.org/3/movie/${movieId}/credits?api_key=${environment.TmdbApiKey}`);
  }

  searchMovieAlternativeTitles(movieId : number) : Observable<any>{
    return this.http.get<any>(`https://api.themoviedb.org/3/movie/${movieId}/alternative_titles?api_key=${environment.TmdbApiKey}`);
  }


}
