import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {provideHttpClient, HTTP_INTERCEPTORS, withInterceptorsFromDi, withInterceptors} from '@angular/common/http';
import {provideHotToastConfig} from '@ngxpert/hot-toast';
import {interceptorInterceptor} from './interceptor/interceptor.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([interceptorInterceptor]),
      withInterceptorsFromDi()
    ),
    provideHotToastConfig({
      duration : 1500
    })
  ]
};
