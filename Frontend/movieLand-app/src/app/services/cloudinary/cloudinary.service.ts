import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CloudinaryService {

  cloudName !: string;
  apiKey !: string;
  uploadPreset !: string ;

  constructor(private http : HttpClient) {
    this.cloudName = environment.cloudName;
    this.apiKey = environment.apiKey;
    this.uploadPreset = environment.uploadPreset;
  }

  uploadImage(file : File ) : Observable<any> {
    let formData : FormData = this.createFormData(file);
    const cloudinaryUrl = `https://api.cloudinary.com/v1_1/${this.cloudName}/image/upload`;
    return this.http.post(cloudinaryUrl , formData);
  }


  private createFormData(file : File) : FormData {
    const formData = new FormData();
    formData.append('file',file);
    formData.append("cloud_name",this.cloudName);
    formData.append("api_key",this.apiKey)
    formData.append("upload_preset",this.uploadPreset);
    return formData;
  }
}
