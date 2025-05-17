import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {jwtDecode, JwtPayload} from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  accessTokenSubject = new BehaviorSubject<string | null>(null);
  currentUserSubject = new BehaviorSubject<any>(null);

  accessToken$ = this.accessTokenSubject.asObservable();
  currentUser$ = this.currentUserSubject.asObservable();


  constructor() {
    if(this.retrieveTokenFromLocalStorage())
      this.loadProfile(this.retrieveTokenFromLocalStorage() , this.getUserDataFromLocalStorage());
  }

  injectAccessToken(accessToken : string ){
    this.accessTokenSubject.next(accessToken);
  }

   injectTokenIntoLocalStorage(token : string){
    localStorage.setItem("accessToken" , token);
  }

  injectUserDataIntoLocalStorage(userData : any) {
    localStorage.setItem("userData" , JSON.stringify(userData));
  }

  getUserDataFromLocalStorage() : any {
    const userData = localStorage.getItem("userData");
    return userData ? JSON.parse(userData) : null;
  }

  clearUserDataFromLocalStorage() {
    localStorage.removeItem("userData");
  }

   retrieveTokenFromLocalStorage() : string | null {
    return localStorage.getItem("accessToken");
  }

   clearTokensFromLocalStorage() {
    localStorage.removeItem("accessToken");
  }

  private injectUser(userData : any) {
    this.currentUserSubject.next(userData);
  }

  loadProfile(token : any , userData: any) : void {
    if(this.isTokenExpired(token)){
      this.clearTokensFromLocalStorage();
      this.accessTokenSubject.next(null);
      this.currentUserSubject.next(null);
      this.clearUserDataFromLocalStorage();
      return;
    }
    this.injectTokenIntoLocalStorage(token);
    this.injectAccessToken(token);
    this.injectUser(userData);
    this.injectUserDataIntoLocalStorage(userData);
  }


  private isTokenExpired(token: any) : boolean {
    try{
      const decoded : JwtPayload = jwtDecode(token);
      const currentTime = Math.floor(Date.now() / 1000);
      return decoded.exp ? decoded.exp < currentTime : false;
    }catch(error) {
      console.error("Invalid token", error);
      return true;
    }
  }

  isAuthenticated() : boolean {
    return this.currentUserSubject.value !== null;
  }

  getCurrentUser() : any {
    return this.currentUserSubject.value;
  }

  getToken() : any {
    return this.accessTokenSubject.value;
  }

  logout() : void {
    this.clearTokensFromLocalStorage();
    this.clearUserDataFromLocalStorage();
    this.accessTokenSubject.next(null);
    this.currentUserSubject.next(null);
  }


}
