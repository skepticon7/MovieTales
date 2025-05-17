import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {FormGroup} from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class SettingsService {

  constructor(
    private http : HttpClient
  ) { }

  getUserSettings(userId : string) : Observable<any> {
    let params = new HttpParams().append("userId" , userId)
    return this.http.get<any>(`${environment.UserService}/api/users/getUserSettings` , {params});
  }

  updateUserSettings(userId : string , settingsForm : FormGroup) : Observable<any> {
    let params = new HttpParams().append("userId" , userId);
    return this.http.put<any>(`${environment.UserService}/api/users/updateUserSettings` , settingsForm.value , {params});
  }

  deleteUser(userId : string) : Observable<any> {
    let params = new HttpParams().append("userId" , userId);
    return this.http.delete<any>(`${environment.UserService}/api/users/deleteUser` , {params})
  }

  updateUserPassword(userId : string , passwordForm : FormGroup) : Observable<any> {
    let params = new HttpParams().append("userId" , userId);
    return this.http.patch<any>(`${environment.UserService}/api/users/updateUserPassword` , passwordForm.value ,  {params});
  }
  deleteAccount(userId: string, deleteForm: any): Observable<any> {
    return this.http.delete<any>(`${environment.UserService}/api/users/deleteUserAccount`, {
      body: deleteForm.value,
      params: new HttpParams().append('userId', userId),
    });
  }


}
