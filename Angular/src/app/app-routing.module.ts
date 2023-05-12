import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UploadComponent } from './components/upload.component';
import { StorageComponent } from './components/storage.component';
import { DisplayComponent } from './components/display.component';
import { PhotoStorafeComponent } from './components/photo-storafe.component';

const routes: Routes = [
  {path:'', component:PhotoStorafeComponent},
  {path:'upload', component:UploadComponent},
  {path:'display/:bundleId', component:DisplayComponent},
  {path: "**", redirectTo:'/', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
