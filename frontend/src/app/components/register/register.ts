import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { AuthService } from '../../services/auth.service';
@Component({
  selector: 'app-register',
  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    RouterModule
  ],

  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {

  name: string = '';
  email: string = '';
  password: string = '';
  role: string = 'USER';

  // Password visibility
  showPassword: boolean = false;

  constructor(private authService: AuthService) {}

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  register() {

    const user = {

      name: this.name,

      email: this.email,

      password: this.password,

      role: this.role

    };

    this.authService.register(user).subscribe({

      next: (response) => {

        alert(response);

        console.log(response);

      },

      error: (error) => {

        console.log(error);

        alert("Registration Failed");

      }

    });

  }

}