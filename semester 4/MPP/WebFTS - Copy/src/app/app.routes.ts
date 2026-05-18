import { Routes } from '@angular/router';
import {ShowDetail} from './components/show-details/show-details';
import {ShowList} from './components/show-list/show-list';
import {ShowCrud} from './components/show-crud/show-crud';

let ShowCrudComponent;
export const routes: Routes = [
  { path: '', component: ShowList },
  { path: 'show/:id', component: ShowDetail },
  { path: 'crud', component: ShowCrud }
];
