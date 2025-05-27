import {Component, DestroyRef, OnInit, signal, WritableSignal} from '@angular/core';
import {NavbarComponent} from '../../core/navbar/navbar.component';
import {SidebarComponent} from '../../core/sidebar/sidebar.component';
import {
  LucideAngularModule,
  LucideX,
  LucideEye,
  LucideClock,
  LucideMessageCircleMore,
  LucideFlame,
  LucideStar,
  LucideEllipsisVertical, LucideLogOut, LucideEdit, LucideTrash2, LucideMoreVertical
} from "lucide-angular"
import {AsyncPipe, DecimalPipe, NgClass, NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {HttpClient} from '@angular/common/http';
import {HotToastService} from '@ngxpert/hot-toast';
import {TmdbService} from '../../services/tmdb/tmdb.service';
import {MovieService} from '../../services/movie/movie.service';
import {ActivatedRoute, Router} from '@angular/router';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {catchError, forkJoin, map, Observable, of, switchMap, take, throwError} from 'rxjs';
import {ReviewsService} from '../../services/reviews/reviews.service';
import {AuthService} from '../../services/auth/auth.service';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'app-movie',
  imports: [NavbarComponent, SidebarComponent, LucideAngularModule, NgIf, AsyncPipe, NgForOf, NgClass, ReactiveFormsModule, DecimalPipe],
  templateUrl: './movie.component.html',
  standalone: true,
  styleUrl: './movie.component.css'
})
export class MovieComponent implements OnInit{

  protected readonly LucideX = LucideX;
  protected readonly LucideEye = LucideEye;
  protected readonly LucideStar = LucideStar;
  protected readonly LucideFlame = LucideFlame;
  protected readonly LucideMessageCircleMore = LucideMessageCircleMore;
  protected readonly LucideClock = LucideClock;
  protected readonly LucideEllipsisVertical = LucideEllipsisVertical;
  protected readonly LucideLogOut = LucideLogOut;
  protected readonly LucideEdit = LucideEdit;
  protected readonly LucideTrash2 = LucideTrash2;
  protected readonly LucideMoreVertical = LucideMoreVertical;
  protected readonly Math = Math;

  hoveredIcon: string | null = null;
  showModal: boolean = false;
  movieId !: number;
  errorMessage !: string;
  isReviewDropdownOpen: number | null = null;
  reviewId : number | null = null;

  movieDetails$ !: Observable<any>;

  movieWatched : WritableSignal<boolean> = signal(false);
  movieInWatchlist : WritableSignal<boolean> = signal(false);
  reviewLoading : WritableSignal<boolean> = signal(false);

  reviewGroup !: FormGroup;

  constructor(
    private hotToast : HotToastService ,
    protected authService : AuthService,
    private tmdbService : TmdbService,
    private movieService : MovieService,
    private reviewService : ReviewsService,
    private activatedRoute : ActivatedRoute,
    private destroyRef : DestroyRef,
    private fb : FormBuilder
  ) {

  }

  ngOnInit(): void {
    this.reviewGroup = this.fb.group({
      rating : new FormControl(2 , [Validators.required]),
      review : new FormControl('' , [Validators.required , Validators.maxLength(200)])
    })

    this.activatedRoute.queryParams.pipe(
      takeUntilDestroyed(this.destroyRef),
      catchError(err => {
        return throwError(() => new Error(err));
      })
    ).subscribe(params => {
      this.movieId = params['id'];
      this.movieDetails$ = this.getAllMovieDetails(this.movieId);
      this.movieDetails$.subscribe(res => {
        console.log(res);
        this.movieInWatchlist.set(res.movieTrackerStatus.status === 'WATCHLIST');
        this.movieWatched.set(res.movieTrackerStatus.status === 'WATCHED');
      })
    })
  }

  toggleReviewDropdown(index: number): void {
    this.isReviewDropdownOpen = this.isReviewDropdownOpen === index ? null : index;
  }

  private getAllMovieDetails(movieId: number) : Observable<any> {
    return forkJoin({
      movieTrackerStatus : this.movieService.getMovieTrackerStatus(movieId , this.authService.getCurrentUser()?._id),
      movieDetails : this.tmdbService.getMovie(movieId).pipe(
        catchError(err => {
          console.error('Error fetching movie details' , err);
          return of(null);
        })
      ),
      movieCredits : this.tmdbService.searchMovieCredits(movieId).pipe(
        map(response => {
          const crew = response.crew;
          const director = crew.find((person: any) => person.job === 'Director');
          return {
            director: director ? director.name : 'Unknown',
            cast: response.cast.slice(0, 10).map((person: any) => ({
              name: person.name,
              profile_path: person.profile_path
            }))
          }
        }),
        catchError(err => {
          console.error('Error fetching movie credits' , err);
          return of([])
        })
      ),
      movieReviews : this.reviewService.getMovieReviews(movieId).pipe(
        catchError(err => {
          console.error('Error fetching movie reviews' , err);
          return of([])
        })
      ),
      movieStats : this.movieService.getMovieStats(movieId).pipe(
        catchError(err => {
          console.error('Error fetching movie stats' , err);
          return of([])
        })
      )
    }).pipe(
      takeUntilDestroyed(this.destroyRef),
      catchError(err => {
        this.errorMessage = "Error fetching movie details";
        console.error(err);
        return of(null);
      })
    )
  }

  addMovieToWatchedOrWatchlist(status : string) : void {
    this.movieService.addMovieToWatchedOrWatchlist(this.movieId , this.authService.getCurrentUser()?._id , status).pipe(
      takeUntilDestroyed(this.destroyRef),
      catchError(err => {
        console.error('Error adding movie to watched or watchlist' , err);
        this.hotToast.error(`Error adding movie to ${status}`);
        return throwError(() => new Error(err));
      })
    ).subscribe(res => {
      this.hotToast.success(`Movie added to ${status}`);
      if(status === 'WATCHLIST') {
        this.movieInWatchlist.set(true);
        this.movieWatched.set(false);
      } else {
        this.movieWatched.set(true);
        this.movieInWatchlist.set(false);
      }
      this.movieDetails$ = this.getAllMovieDetails(this.movieId);
    })
  }

  handleWatchlistActions() : void {
    if(this.movieInWatchlist()) {
      this.removeFromWatchedOrWatchList(this.authService.getCurrentUser()?._id , 'WATCHLIST');
    }else{
      this.addMovieToWatchedOrWatchlist('WATCHLIST');
    }
  }

  handleWatchedActions() : void {
    if(this.movieWatched()) {
      this.removeFromWatchedOrWatchList(this.authService.getCurrentUser()?._id , 'WATCHED');
    }else{
      this.addMovieToWatchedOrWatchlist('WATCHED');
    }
  }


  private removeFromWatchedOrWatchList( userId: string , status : string) {
    this.movieService.removeFromWatchedOrWatchlist(this.movieId , userId).pipe(
      takeUntilDestroyed(this.destroyRef),
      catchError(err => {
        console.error(`error removing movie from ${status}` , err);
        this.hotToast.error(`Error removing movie from ${status}`);
        return throwError(() => new Error(err));
      })
    ).subscribe(res => {
      this.hotToast.success(`Movie removed from ${status}`);
      if(status === 'WATCHLIST') {
        this.movieInWatchlist.set(false);
      }
      if(status === 'WATCHED') {
        this.movieWatched.set(false);
      }
      this.movieDetails$ = this.getAllMovieDetails(this.movieId);
    })
  }


  handleReview() {
    console.log(this.reviewGroup.value);
    if(this.reviewId){
        this.EditReview(this.reviewId);
     }else{
       this.AddReview();
     }
  }

  handleModal(reviewId : string | null) : void {
    if(!reviewId){
      this.reviewGroup.reset();
    }else{
      this.isReviewDropdownOpen = null;

      this.movieDetails$.pipe(take(1)).subscribe((res) => {
        const review = res.movieReviews.find((review: any) => review.id === reviewId);
        this.reviewGroup.patchValue({
          rating: review.rating,
          review: review.review
        });
      });
    }
    this.showModal = true;
  }

  handleDeleteReview(reviewId : number) : void {
    this.reviewService.deleteReview(reviewId , this.movieId  ,this.authService.getCurrentUser()?._id).pipe(
      takeUntilDestroyed(this.destroyRef),
      catchError(err => {
        this.hotToast.error('Error deleting review');
        return throwError(() => new Error(err));
      })
    ).subscribe((res : any) => {
      this.hotToast.success('Review deleted successfully');
      this.movieDetails$ = this.getAllMovieDetails(this.movieId);
    })
  }

  EditReview(reviewId: number): void {
    this.reviewLoading.set(true);
    this.reviewService.updateReview(this.reviewGroup ,  reviewId ,this.movieId , this.authService.getCurrentUser()?._id).pipe(
      takeUntilDestroyed(this.destroyRef),
      catchError(err => {
        this.hotToast.error('Error updating review');
        this.reviewLoading.set(false);
        return throwError(() => new Error(err));
      })
    ).subscribe(res => {
      this.hotToast.success('Review updated successfully');
      this.reviewLoading.set(false);
      this.showModal = false;
      this.movieDetails$ = this.getAllMovieDetails(this.movieId);
    })

  }

   AddReview() : void {
     this.reviewLoading.set(true);
     this.reviewService.addReview(this.reviewGroup ,  this.authService.getCurrentUser()?._id ,this.movieId).pipe(
       takeUntilDestroyed(this.destroyRef),
       catchError(err => {
         console.error('Error adding review' , err);
         this.hotToast.error('Error adding review');
         this.reviewLoading.set(false);
         return throwError(() => new Error(err));
       })
     ).subscribe(res => {
       this.hotToast.success('Review added successfully');
       this.reviewLoading.set(false);
       this.reviewGroup.reset();
       this.showModal = false;
       this.movieDetails$ = this.getAllMovieDetails(this.movieId);
     })
   }




}
