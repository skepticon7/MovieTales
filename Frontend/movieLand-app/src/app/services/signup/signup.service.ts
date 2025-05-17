import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormGroup} from '@angular/forms';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SignupService {

  constructor(private http : HttpClient) { }

  signup(signupForm : FormGroup) : Observable<any> {
    return this.http.post<any>(`${environment.UserService}/api/users/register` , signupForm.value);
  }

}
