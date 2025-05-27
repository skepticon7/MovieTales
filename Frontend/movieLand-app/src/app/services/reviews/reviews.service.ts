import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {environment} from '../../../environments/environment';
import {FormGroup} from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class ReviewsService {

  constructor(private http : HttpClient) { }

  addReview(reviewData : FormGroup , userId : string , movieId : number) : Observable<any> {
    let params = new HttpParams().append("userId" , userId).append("movieId" , movieId);
    return this.http.post<any>(`${environment.MovieService}/addNewReview` , reviewData.value , {params});
  }

  getUserReviews(userId : string) : Observable<any> {
    let params = new HttpParams().append("userId" , userId);
    return this.http.get<any>(`${environment.MovieService}/getAllUserReviews` , {params});
  }

  updateReview(reviewData : FormGroup , reviewId : number , movieId : number , userId : string) : Observable<any> {
    let params = new HttpParams().append("reviewId" , reviewId).append("movieId" , movieId).append("userId" , userId);
    return this.http.patch<any>(`${environment.MovieService}/updateReview` , reviewData.value , {params});
  }

  deleteReview(reviewId : number , movieId : number , userId : string) : Observable<any> {
    let params = new HttpParams().append("reviewId" , reviewId).append("movieId" , movieId).append("userId" , userId);
    return this.http.delete<any>(`${environment.MovieService}/deleteReview` , {params});
  }

  getMovieReviews(movieId: number) : Observable<any> {
    let params = new HttpParams().append("movieId" , movieId);
    return this.http.get<any>(`${environment.MovieService}/getMovieReviews` , {params});
  }
}
