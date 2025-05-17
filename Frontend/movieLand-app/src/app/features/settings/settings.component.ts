import { Component, DestroyRef, OnInit, signal, WritableSignal } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { AuthService } from '../../services/auth/auth.service';
import { SettingsService } from '../../services/settings/settings.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {
  LucideAngularModule,
  LucideLockKeyhole,
  LucideMail, LucidePen,
  LucideShieldPlus, LucideTrash,
  LucideUser,
  LucideUserRound
} from 'lucide-angular';
import { NgClass, NgIf } from '@angular/common';
import { Router } from '@angular/router';
import {CloudinaryService} from '../../services/cloudinary/cloudinary.service';

@Component({
  selector: 'app-settings',
  imports: [
    LucideAngularModule,
    NgIf,
    NgClass,
    ReactiveFormsModule
  ],
  templateUrl: './settings.component.html',
  standalone: true,
  styleUrl: './settings.component.css'
})
export class SettingsComponent implements OnInit {

  userSettings!: Observable<any>;
  passwordForm!: FormGroup;
  selectedSection: WritableSignal<string> = signal('personalInfo');
  loading: WritableSignal<boolean> = signal(false);
  errorMessage!: string;
  settingsForm!: FormGroup;
  deleteForm!: FormGroup;
  changingImageLocker : WritableSignal<boolean> = signal(false);
  selectedImage!: any;
  profileImage!: any;

  constructor(
    protected authService: AuthService,
    private router: Router,
    private settingsService: SettingsService,
    private hotToaster: HotToastService,
    private cloudinaryService : CloudinaryService,
    private destroyRef: DestroyRef,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.profileImage = this.authService.getCurrentUser()?.avatarUrl;

    this.settingsForm = this.fb.group({
      username: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required]),
      avatarUrl: new FormControl('', [Validators.required]),
    });

    this.passwordForm = this.fb.group({
      oldPassword: new FormControl('', [Validators.required]),
      newPassword: new FormControl('', [Validators.required]),
      confirmNewPassword: new FormControl('', [Validators.required])
    });

    this.deleteForm = this.fb.group({
      password: new FormControl('', [Validators.required])
    });

    this.userSettings = this.getUserSettings();
    this.userSettings.subscribe(res => {
      console.log(res);
      this.settingsForm.patchValue({
        username: res.UserDTO.username,
        email: res.UserDTO.email,
        avatarUrl: res.UserDTO.avatarUrl
      });

    });

  }

  toggleSelectedSection(section: string) {
    this.selectedSection.set(section);
  }

  isSectionActive(section: string): boolean {
    return this.selectedSection() === section;
  }

  private getUserSettings(): Observable<any> {
    return this.settingsService.getUserSettings(this.authService.getCurrentUser()?._id).pipe(
      takeUntilDestroyed(this.destroyRef),
      catchError(err => {
        this.errorMessage = 'Error fetching user settings';
        console.error("Error fetching user settings", err);
        return throwError(() => new Error(err));
      })
    );
  }

  handleNewUserSettings(): void {
    this.loading.set(true);
    if (this.selectedImage) {
      this.cloudinaryService.uploadImage(this.selectedImage).pipe(
        takeUntilDestroyed(this.destroyRef),
        catchError(err => {
          this.loading.set(false);
          this.hotToaster.error("Error uploading your avatar");
          return throwError(() => new Error(err));
        })
      ).subscribe(cloudinaryResponse => {
        const cloudinaryAvatarUrl: string = cloudinaryResponse.secure_url;

        this.settingsForm.patchValue({
          avatarUrl: cloudinaryAvatarUrl
        });

        this.updateUserSettings();
      });

    } else {
      this.updateUserSettings();
    }
  }


  private updateUserSettings(): void {
    this.settingsService.updateUserSettings(this.authService.getCurrentUser()?._id, this.settingsForm).pipe(
      takeUntilDestroyed(this.destroyRef),
      catchError(err => {
        this.loading.set(false);
        this.hotToaster.error("Error updating user settings");
        console.error("Error updating user settings", err);
        return throwError(() => new Error(err));
      })
    ).subscribe((res) => {
      this.loading.set(false);
      this.hotToaster.success("User settings updated successfully");
      this.authService.injectUserDataIntoLocalStorage(res.user);

      this.router.navigateByUrl('/settings', { skipLocationChange: true }).then(() => {
        this.router.navigate([this.router.url]);
      });
    });
  }

  handleNewPassword() {
    this.loading.set(true);
    const formData = this.passwordForm.value;
    if (formData.newPassword !== formData.confirmNewPassword) {
      this.loading.set(false);
      this.passwordForm.reset();
      this.hotToaster.error("Passwords do not match");
      return;
    }

    this.settingsService.updateUserPassword(this.authService.getCurrentUser()?._id, this.passwordForm).pipe(
      takeUntilDestroyed(this.destroyRef),
      catchError(err => {
        this.loading.set(false);
        this.hotToaster.error(err.error.error);
        this.passwordForm.reset();
        return throwError(() => new Error(err));
      })
    ).subscribe((res) => {
      this.loading.set(false);
      this.hotToaster.success(res.message);
      this.passwordForm.reset();
    });
  }

  handleDeleteAccount() {
    this.loading.set(true);
    this.settingsService.deleteAccount(this.authService.getCurrentUser()?._id, this.deleteForm).pipe(
      takeUntilDestroyed(this.destroyRef),
      catchError(err => {
        this.deleteForm.reset();
        this.loading.set(false);
        this.hotToaster.error(err.error.error);
        return throwError(() => new Error(err));
      })
    ).subscribe(() => {
      this.loading.set(false);
      this.hotToaster.success('Account successfully deleted');
      this.authService.logout();
      this.router.navigateByUrl('/login');
    });
  }

  changeImage(event: any) {
    this.changingImageLocker.set(true);
    const image = event.target.files[0];
    if (image) {
      this.selectedImage = image;
      this.profileImage = URL.createObjectURL(image);
      this.settingsForm.get('avatarUrl')?.setValue(this.selectedImage.name);
    }
    this.changingImageLocker.set(false);
  }

  resetImage() {
    this.selectedImage = null;
    this.profileImage = this.authService.getCurrentUser()?.avatarUrl;
  }

  protected readonly LucideUser = LucideUser;
  protected readonly LucideShieldPlus = LucideShieldPlus;
  protected readonly LucideUserRound = LucideUserRound;
  protected readonly LucideMail = LucideMail;
  protected readonly LucidePen = LucidePen;
  protected readonly LucideTrash = LucideTrash;
  protected readonly LucideLockKeyhole = LucideLockKeyhole;
}
