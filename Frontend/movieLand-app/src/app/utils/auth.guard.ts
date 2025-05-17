import { inject } from '@angular/core';
import {CanActivateFn, Router} from '@angular/router';
import {AuthService} from '../services/auth/auth.service';

export const authGuard: CanActivateFn = () : boolean => {
  const route : Router = inject(Router);
  const authService : AuthService = inject(AuthService);
  if(!authService.isAuthenticated()){
    route.navigateByUrl("/login");
    return false;
  }
  return true;
};

export const loginGuard: CanActivateFn = () => {
  const router: Router = inject(Router);
  const authService: AuthService = inject(AuthService);

  if (authService.isAuthenticated()) {
    router.navigateByUrl("/")
    return false;
  }

  return true;
};
