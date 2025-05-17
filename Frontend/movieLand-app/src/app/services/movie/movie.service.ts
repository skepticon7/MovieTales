import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MovieService {

  constructor(private http : HttpClient) { }

  getTenWatchedMovies(userId : string) : Observable<any> {
    let params = new HttpParams().append("userId" , userId);
    return this.http.get<any>(`${environment.MovieService}/getTenMoviesInWatchedList` , {params})
  }

  addMovie(movieData : any) : Observable<any> {
    return this.http.post<any>(`${environment.MovieService}/addMovie` , movieData);
  }

  getMovieById(movieId : string) : Observable<any> {
    return this.http.get<any>(`${environment.MovieService}/getMovie/${movieId}`);
  }

  addMovieToWatchedOrWatchlist(movieId : number , userId : string , status : string) : Observable<any> {
    let params = new HttpParams().append("movieId" , movieId).append("userId" , userId).append("status" , status);
    return this.http.post<any>(`${environment.MovieService}/addToWatchListOrWatchedList` , {} , {params});
  }

  getWatchedOrWatchlist(userId : string , status : string) : Observable<any> {
    let params = new HttpParams().append("userId" , userId).append("status" , status);
    return this.http.get<any>(`${environment.MovieService}/getWatchlistOrWatchedlist` , {params});
  }

  removeFromWatchedOrWatchlist(movieId : number , userId : string) : Observable<any> {
    let params = new HttpParams().append("movieId" , movieId).append("userId" , userId);
    return this.http.delete<any>(`${environment.MovieService}/removeFromWatchListOrWatchedList` , {params});
  }


  getMovieStats(movieId: number) : Observable<any> {
    let params = new HttpParams().append("movieId" , movieId);
    return this.http.get<any>(`${environment.MovieService}/getMovieStats` , {params});
  }

  getMovieTrackerStatus(movieId: number , userId : string) {
    let params = new HttpParams().append("movieId" , movieId).append("userId" , userId);
    return this.http.get<any>(`${environment.MovieService}/getMovieTrackerStatus` , {params});
  }
}
