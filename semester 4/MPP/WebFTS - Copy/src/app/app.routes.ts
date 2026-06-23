import { Routes } from '@angular/router';
import {ShowDetail} from './components/show-details/show-details';
import {ShowList} from './components/show-list/show-list';
import {ShowCrud} from './components/show-crud/show-crud';
import {LoginComponent} from './components/login/login';
import {AuthGuard} from './interceptors/auth.guard';

export const routes: Routes = [
  { path: '', component: ShowList, canActivate: [AuthGuard]},
  { path: 'show/:id', component: ShowDetail, canActivate: [AuthGuard] },
  { path: 'crud', component: ShowCrud, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
];
