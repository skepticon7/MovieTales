import {Component, OnInit, signal, WritableSignal} from '@angular/core';
import {
  LucideAngularModule,
  LucideHome,
  LucideStar,
  LucideClock4,
  LucideEye,
  LucideSettings,
  Signal
} from 'lucide-angular';
import {ActivatedRoute, NavigationEnd, Router, RouterLink} from '@angular/router';
import {NgClass} from '@angular/common';
import {filter} from 'rxjs';

@Component({
  selector: 'app-sidebar',
  imports: [LucideAngularModule, RouterLink, NgClass],
  templateUrl: './sidebar.component.html',
  standalone: true,
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit{

  selectedOption : WritableSignal<string> = signal("Home")

  protected readonly LucideHome = LucideHome;
  protected readonly LucideStar = LucideStar;
  protected readonly LucideClock4 = LucideClock4;
  protected readonly LucideEye = LucideEye;
  protected readonly LucideSettings = LucideSettings;

  constructor(private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.setSelectedOptionFromUrl();

    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.setSelectedOptionFromUrl();
    });
  }

  private setSelectedOptionFromUrl(): void {
    const currentRoute = this.router.url.split('/')[1];

    switch (currentRoute) {
      case 'watched':
        this.selectedOption.set('Watched');
        break;
      case 'home':
        this.selectedOption.set('Home');
        break;
      case 'settings':
        this.selectedOption.set('Settings');
        break;
      case 'watchlist':
        this.selectedOption.set('Watchlist');
        break;
      default:
        this.selectedOption.set('Home'); // Default option
    }
  }

  selectOption(option: string): void {
    this.selectedOption.set(option);
  }

  isActive(option: string): boolean {
    return this.selectedOption() === option;
  }

}
