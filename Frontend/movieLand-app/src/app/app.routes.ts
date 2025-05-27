import { Routes } from '@angular/router';
import {LoginComponent} from './auth/login/login.component';
import {SignupComponent} from './auth/signup/signup.component';
import {LayoutComponent} from './core/layout/layout.component';
import {HomeComponent} from './features/home/home.component';
import {WatchedComponent} from './features/watched/watched.component';
import {WatchlistComponent} from './features/watchlist/watchlist.component';
import {SettingsComponent} from './features/settings/settings.component';
import {SearchComponent} from './features/search/search.component';
import {MovieComponent} from './component/movie/movie.component';
import {authGuard , loginGuard} from './utils/auth.guard';


export const routes: Routes = [
  {path : "login" , component : LoginComponent , canActivate : [loginGuard]},
  {path : "signup" , component : SignupComponent , canActivate : [loginGuard]},
  {
    path : "",
    canActivate : [authGuard],
    component : LayoutComponent,
    children : [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      {path : "home" , component : HomeComponent},
      {path : "watched" , component : WatchedComponent},
      {path : "watchlist" , component : WatchlistComponent},
      {path : "settings" , component : SettingsComponent},
    ],
  },
  { path : "search" , component : SearchComponent},
  { path : "movie" , component : MovieComponent},
  { path: '**', redirectTo: '/home' }
];
