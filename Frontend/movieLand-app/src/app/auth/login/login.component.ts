import {Component, DestroyRef, OnInit, signal, WritableSignal} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {LoginService} from '../../services/login/login.service';
import {HotToastService} from '@ngxpert/hot-toast';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {catchError, throwError} from 'rxjs';
import {NgClass, NgIf} from '@angular/common';
import {AuthService} from '../../services/auth/auth.service';

@Component({
  selector: 'app-login',
  imports: [
    RouterLink,
    FormsModule,
    ReactiveFormsModule,
    NgIf,
    NgClass
  ],
  templateUrl: './login.component.html',
  standalone: true,
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{

    loginForm !: FormGroup;
    loading : WritableSignal<boolean> = signal(false);

    ngOnInit(): void {
        this.loginForm = this.fb.group({
          username : new FormControl('' , [Validators.required]),
          password : new FormControl('' , [Validators.required])
        })
    }

    constructor(private router : Router ,private authService: AuthService ,private destroyRef : DestroyRef ,private fb : FormBuilder , private loginService : LoginService , private hotToast : HotToastService) {}

    handleLogin() : void {
      this.loading.set(true);
      this.loginService.login(this.loginForm).pipe(
        takeUntilDestroyed(this.destroyRef),
        catchError(err => {
          const errorMessage = err.error.error || 'Something went wrong'
          this.hotToast.error(errorMessage);
          this.loading.set(false);
          return throwError(() => new Error(err));
        })
      ).subscribe(res => {
        this.loading.set(false);
        this.hotToast.success('Login successful');
        this.authService.loadProfile(res.token , res.user);
        this.router.navigateByUrl("/");
      })
    }

}
