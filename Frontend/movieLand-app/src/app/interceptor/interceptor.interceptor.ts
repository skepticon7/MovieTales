import { HttpInterceptorFn } from '@angular/common/http';
import {inject} from '@angular/core';
import {AuthService} from '../services/auth/auth.service';

export const interceptorInterceptor: HttpInterceptorFn = (req, next) => {
  const excludedRoutes : string[] = ['/login' , '/register' , 'cloudinary']
  const authService = inject(AuthService);
  const token = authService.getToken();

  if(isExcluded(req.url , excludedRoutes)){
    return next(req);
  }

  const clonedRequest = req.clone({
    setHeaders : {
      'Authorization' : `Bearer ${token}`
    }
  });

  return next(clonedRequest);
};


const isExcluded = (route: string, excludedRoutes: string[]): boolean => {
  return excludedRoutes.some((r: string) => route.includes(r));
};
