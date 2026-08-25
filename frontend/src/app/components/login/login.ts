import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule
  ],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  email: string = '';
  password: string = '';

  showPassword: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  login(): void {

  console.log('LOGIN BUTTON CLICKED');

  const credentials = {
    email: this.email,
    password: this.password
  };

  console.log('EMAIL:', this.email);
  console.log('PASSWORD ENTERED:', this.password);

  this.authService.login(credentials).subscribe({

    next: (response) => {

      console.log('RAW BACKEND RESPONSE:', response);
      console.log('RESPONSE TYPE:', typeof response);

      const role = response.trim().toUpperCase();

      console.log('ROLE:', role);

      if (role === 'USER') {

        console.log('Navigating to USER home...');
        this.router.navigate(['/']);

      }
      else if (role === 'BUSINESS') {

        console.log('Navigating to BUSINESS dashboard...');
        this.router.navigate(['/business-dashboard']);

      }
      else if (role === 'ADMIN') {

        console.log('Navigating to ADMIN dashboard...');
        this.router.navigate(['/admin-dashboard']);

      }
      else {

        console.error('UNKNOWN ROLE:', role);
        alert('Unknown account role');

      }

    },

    error: (error) => {

      console.error('LOGIN REQUEST FAILED:', error);

      alert('Invalid email or password');

    }

  });

}

}