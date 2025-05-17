import {Component, DestroyRef, OnInit} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {AuthService} from './services/auth/auth.service';

@Component({
  selector: 'app-root',
  standalone:true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  title = 'movieLand-app';

  constructor(private authService : AuthService , private destroyRef : DestroyRef) {}

  ngOnInit(): void {

  }
}
