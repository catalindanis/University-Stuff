import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, RouterLink, NgIf],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  firstName = '';
  lastName = '';
  email = '';
  password = '';
  confirmPassword = '';
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  onRegister(): void {
    this.errorMessage = '';

    if (this.firstName.trim() === '') {
      this.errorMessage = 'First name is required.';
      return;
    }

    if (this.lastName.trim() === '') {
      this.errorMessage = 'Last name is required.';
      return;
    }

    if (this.email.trim() === '') {
      this.errorMessage = 'Email is required.';
      return;
    }

    if (this.password.trim() === '') {
      this.errorMessage = 'Password is required.';
      return;
    }

    if (this.confirmPassword.trim() === '') {
      this.errorMessage = 'Confirm password is required.';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match.';
      return;
    }

    this.authService
      .register(this.firstName, this.lastName, this.email, this.password, this.confirmPassword)
      .subscribe({
        next: (response: any) => {
          this.router.navigate(['/login']);
        },
        error: (error) => {
          if (error?.error?.message) {
            this.errorMessage = error.error.message;
          } else if (Array.isArray(error?.error?.violations)) {
            this.errorMessage = error.error.violations.map((v: any) => v.message).join(' ');
          } else {
            this.errorMessage = 'Unknown error. Please try again.';
          }
        }
      });
  }

}
