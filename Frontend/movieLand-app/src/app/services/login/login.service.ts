import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormGroup} from '@angular/forms';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http : HttpClient) { }

  login(loginForm : FormGroup) : Observable<any> {
    return this.http.post<any>(`${environment.UserService}/api/users/login` , loginForm.value);
  }
}
