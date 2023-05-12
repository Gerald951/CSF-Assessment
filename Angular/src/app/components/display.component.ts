import { Component, OnInit } from '@angular/core';
import { UploadSearchService } from '../services/upload-search.service';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Bundle } from '../model/bundle';
import { URL } from '../model/Url';

@Component({
  selector: 'app-display',
  templateUrl: './display.component.html',
  styleUrls: ['./display.component.css']
})
export class DisplayComponent implements OnInit {
  params$!:Subscription
  bundleId!:string
  receivedBundle! : Bundle
  obj! : URL[]
  constructor(private searchSvc:UploadSearchService, private activatedRoute:ActivatedRoute) {}

  ngOnInit(): void {
    this.params$ = this.activatedRoute.params.subscribe(
      async (params) => {
        this.bundleId = params['bundleId']
      }
    )

    this.receivedBundle = this.searchSvc.getBundle(this.bundleId);

    this.obj = JSON.parse(this.receivedBundle.listOfURL)

}

}
