import { Component, OnDestroy, OnInit } from '@angular/core';
import { UploadSearchService } from '../services/upload-search.service';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Bundle } from '../model/bundle';

const URL = 'https://nqlcsfb2.sgp1.cdn.digitaloceanspaces.com/'

@Component({
  selector: 'app-display',
  templateUrl: './display.component.html',
  styleUrls: ['./display.component.css']
})
export class DisplayComponent implements OnInit{
 
  bundleId!:string
  receivedBundle! : Bundle
  listOfKeys: string[] = []
  constructor(private searchSvc:UploadSearchService, private activatedRoute:ActivatedRoute) {}

  ngOnInit(): void {
    const bundleId = this.activatedRoute.snapshot.params['bundleId']
    this.searchSvc.getBundle(bundleId).then((data : Bundle) => { if (data.bundleId !== null) {
                                                                    this.receivedBundle = data
                                                                    this.receivedBundle.isBundle = true
                                                                    console.info(this.receivedBundle.isBundle)
                                                                    const obj : { [key:string]: string }[] = JSON.parse(this.receivedBundle.listOfURL) 
                                                                    const keys : string[] = obj.map(o => {
                                                                                  const propertyValues = Object.keys(o)
                                                                                  return propertyValues.length > 0 ? propertyValues[0].toString() : ''
                                                                    })

                                                                    for (const element of keys) {
                                                                      this.listOfKeys.push(URL + element)
                                                                    }
                                                                                                   
                                                                  } else {
                                                                    this.receivedBundle = data
                                                                    this.receivedBundle.isBundle = false
                                                                  }
                                                                })

  }



}
