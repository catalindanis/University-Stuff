import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';import { AuthService } from '../../services/auth.service';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink, NgIf],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  email = '';
  password = '';
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin(): void {
    this.errorMessage = '';

    if(this.email.trim() === '') {
      this.errorMessage = 'Email is required.';
      return;
    }

    if(this.password.trim() === '') {
      this.errorMessage = 'Password is required.';
      return;
    }

    this.authService.login(this.email, this.password).subscribe({
      next: (response: any) => {
        if (response?.token) {
          this.authService.saveToken(response.token);
          this.router.navigate(['/home']);
        }
      },
      error: (error) => {
        if(error?.error?.message)
          this.errorMessage = error.error.message;
        else if (Array.isArray(error?.error?.violations))
          this.errorMessage = error.error.violations.map((v: any) => v.message).join(' ');
        else
          this.errorMessage = 'Unknown error. Please try again.';
      }
    });
  }
}
