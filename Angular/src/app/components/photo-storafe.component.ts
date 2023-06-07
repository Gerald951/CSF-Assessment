import { Component, OnInit } from '@angular/core';
import { UploadSearchService } from '../services/upload-search.service';
import { UploadedArchive } from '../model/UploadedArchive';

@Component({
  selector: 'app-photo-storafe',
  templateUrl: './photo-storafe.component.html',
  styleUrls: ['./photo-storafe.component.css']
})
export class PhotoStorafeComponent implements OnInit {
  listOfStorage : UploadedArchive[] = []
  constructor(private searchSvc : UploadSearchService) {}

  ngOnInit(): void {
      this.searchSvc.getBundles().then(data => {
                          this.listOfStorage = data
      })
  }

}
