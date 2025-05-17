import {Component, DestroyRef, OnInit, signal, WritableSignal} from '@angular/core';
import {SignupService} from '../../services/signup/signup.service';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {catchError, throwError} from 'rxjs';
import {HotToastService} from '@ngxpert/hot-toast';
import {CloudinaryService} from '../../services/cloudinary/cloudinary.service';
import {NgClass, NgIf} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-signup',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgIf,
    NgClass,
    RouterLink
  ],
  templateUrl: './signup.component.html',
  standalone: true,
  styleUrl: './signup.component.css'
})
export class SignupComponent implements OnInit{

    signupForm !: FormGroup;
    loading : WritableSignal<boolean> = signal(false);
    selectedImage !: File;

    ngOnInit(): void {
        this.signupForm = this.fb.group({
            username : new FormControl('' , [Validators.required]),
            password : new FormControl('',[Validators.required ]),
            email : new FormControl('',[Validators.required , Validators.email]),
            avatarUrl : new FormControl('' , [Validators.required])
        })
    }

    constructor(
                private destroyRef: DestroyRef ,
                private fb : FormBuilder ,
                private signupService : SignupService,
                private hotToast : HotToastService,
                private cloudinaryService : CloudinaryService
    ) {
    }

    handleSignup() : void  {
        this.loading.set(true);


        this.cloudinaryService.uploadImage(this.selectedImage).pipe(
            takeUntilDestroyed(this.destroyRef),
            catchError(err => {
              this.loading.set(false);
              this.hotToast.error("Error uploading your avatar");
              return throwError(() => new Error(err));
            })
        ).subscribe(cloudinaryRes => {
           const avatarCloudinaryUrl = cloudinaryRes.secure_url;
            console.log(avatarCloudinaryUrl);
           this.signupForm.patchValue({
             avatarUrl : avatarCloudinaryUrl
           });

            this.signupService.signup(this.signupForm).pipe(
                takeUntilDestroyed(this.destroyRef),
                catchError(err => {
                    this.loading.set(false);
                    this.hotToast.error("error saving user");
                    return throwError(() => new Error(err));
                })
            ).subscribe(res => {
                this.loading.set(false);
                console.log(res);
                this.hotToast.success(`${res.user.username} registered successfully`);
            });

        })

    }

  handleCoverChange(event : any) {
    let image = event.target.files[0];
    if(image){
      this.selectedImage = image;
      this.signupForm.get('avatarUrl')?.setValue(this.selectedImage.name); // Optional: Set file name
    }

  }

}
