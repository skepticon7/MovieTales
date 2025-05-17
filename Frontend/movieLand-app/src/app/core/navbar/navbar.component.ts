import { Component } from '@angular/core';
import { LucideAngularModule , LucideChevronDown , LucideSearch , LucideLogOut , LucideChevronUp} from 'lucide-angular';
import { NgIf} from '@angular/common';
import {AuthService} from '../../services/auth/auth.service';
import {Router} from '@angular/router';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';




@Component({
  selector: 'app-navbar',
  imports: [LucideAngularModule, NgIf, FormsModule, ReactiveFormsModule],
  templateUrl: './navbar.component.html',
  standalone: true,
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  protected readonly LucideChevronDown = LucideChevronDown;
  protected readonly LucideSearch = LucideSearch;

  isDropdownOpen : boolean = false;

  constructor(protected authService : AuthService , private router : Router) {}

  toggleDropdown(): void {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  protected readonly LucideLogOut = LucideLogOut;

  protected readonly LucideChevronUp = LucideChevronUp;

  handleLogout() {
    this.authService.logout();
    this.router.navigateByUrl("/login");
  }

  protected readonly event = event;

  handleSearch(event: any ) {
    event.preventDefault();
    const inputElement = (event.target as HTMLFormElement).querySelector('input');
    const keyword = inputElement?.value.trim();
    if (keyword) {
      this.router.navigate(['/search'], { queryParams: { q: keyword } });
    }
  }
}
