import { Component } from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {NavbarComponent} from '../navbar/navbar.component';
import {SidebarComponent} from '../sidebar/sidebar.component';

@Component({
  selector: 'app-layout',
  imports: [
    RouterOutlet , NavbarComponent , SidebarComponent
  ],
  templateUrl: './layout.component.html',
  standalone: true,
  styleUrl: './layout.component.css'
})
export class LayoutComponent {

}
