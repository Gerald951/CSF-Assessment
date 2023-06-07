import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, lastValueFrom, map, pipe } from 'rxjs';
import { Bundle } from '../model/bundle';
import { Upload } from '../model/upload';
import { UploadedArchive } from '../model/UploadedArchive';

const SERVER_URL = "http://localhost:8080"

@Injectable({
  providedIn: 'root'
})
export class UploadSearchService {

  getBundle$! : Promise<Bundle> | null
  getBundles$: Promise<UploadedArchive[]>
  bundleResult! : Bundle

  constructor(private http: HttpClient) { }

  uploadForm(formData : FormData) : Observable<Upload> {
    // const headers = new HttpHeaders().set('Content-Type', 'multipart/form-data')
   return this.http.post<Upload>(SERVER_URL +'/upload', formData)
    
  }

  getBundle(bundleId : string) {
    this.getBundle$ = lastValueFrom(this.http.get<Bundle>(SERVER_URL+'/bundle/' + bundleId)
    .pipe(
      map((a : any) => {
        if (a === null) {
          return {
            bundleId: null,
          } as Bundle
        }
        return {
          bundleId: a['bundleId'],
          date: a['date'],
          title : a['title'],
          name: a['name'],
          comments: a['comments'],
          listOfURL : a['urls']
        } as Bundle
      })
    ))

    // this.getBundle$.then((data : Bundle) => this.bundleResult=data)

    return this.getBundle$

  }

  getBundles() {
    this.getBundles$ = lastValueFrom(this.http.get<UploadedArchive[]>(SERVER_URL+'/bundles')
    .pipe(
      map((ar:any[]) => {
        return ar.map((a:any) => {
          return {
            bundleId: a['bundleId'],
            title : a['title'],
            date: a['date']
          } as UploadedArchive
        })
      } )
    ))
  return this.getBundles$
  }
}
