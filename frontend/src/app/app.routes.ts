import { Routes } from '@angular/router';

import { Home } from './components/home/home';
import { Login } from './components/login/login';
import { Register } from './components/register/register';
import { BusinessDashboard } from './components/business-dashboard/business-dashboard';
import { AdminDashboard } from './components/admin-dashboard/admin-dashboard';

export const routes: Routes = [

  {
    path: '',
    component: Home
  },

  {
    path: 'login',
    component: Login
  },

  {
    path: 'register',
    component: Register
  },

  {
    path: 'business-dashboard',
    component: BusinessDashboard
  },

  {
    path: 'admin-dashboard',
    component: AdminDashboard
  },

  {
    path: '**',
    redirectTo: ''
  }

];