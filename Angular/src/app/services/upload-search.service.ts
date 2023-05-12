import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, lastValueFrom, map } from 'rxjs';
import { Bundle } from '../model/bundle';

const SERVER_URL = "http://localhost:8080"

@Injectable({
  providedIn: 'root'
})
export class UploadSearchService {

  getBundle$! : Promise<Bundle> | null
  bundleResult! : Bundle

  constructor(private http: HttpClient) { }

  uploadForm(formData : FormData) : Observable<any> {
    const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
   return this.http.post<any>('/upload', formData, {headers})
    
  }

  getBundle(bundleId : string) {
    this.getBundle$ = lastValueFrom(this.http.get<Bundle>('/bundle/' + bundleId)
    .pipe(
      map((a : any) => {
        if (a === null) {
          return {
            isBundle : false,
          } as Bundle
        }
        return {
          isBundle : true,
          bundleId: a['bundleId'],
          date: a['date'],
          title : a['title'],
          name: a['name'],
          comments: a['comments'],
          listOfURL : a['listOfURL']
        } as Bundle
      })
    ))

    this.getBundle$.then((data : Bundle) => this.bundleResult=data)

    return this.bundleResult


  }
}
