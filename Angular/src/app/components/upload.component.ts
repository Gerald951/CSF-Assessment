import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UploadSearchService } from '../services/upload-search.service';
import { Router } from '@angular/router';
import { Bundle } from '../model/bundle';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit {

  uploadForm! : FormGroup
  bundle! : string
  receivedId!: string
  receivedResponse!: string

  @ViewChild('file') zip : ElementRef

  constructor(private fb : FormBuilder, private searchSvc:UploadSearchService, private router:Router) {}

  ngOnInit(): void {
      this.uploadForm = this.createForm()
  }

  private createForm() {
    return this.fb.group({
      name: this.fb.control('', Validators.required),
      title: this.fb.control('', Validators.required),
      comment: this.fb.control(''),
      zipFile: this.fb.control('', Validators.required)

    })
  }

 submit() {
    const formData = new FormData()
    formData.set('name', this.uploadForm.value['name'])
    formData.set('archive', this.zip.nativeElement[0])
    formData.set('title', this.uploadForm.value['title'])
    formData.set('comment', this.uploadForm.value['comment'])

    const bundle = {} as Bundle

    this.searchSvc.uploadForm(formData).subscribe({
      next: (data) => {
        bundle.bundleId= data['bundleId'],
        bundle.date= data['date'],
        bundle.title= data['title'],
        bundle.name= data['name'],
        bundle.comments= data['comments'],
        bundle.listOfURL = data['listOfURL']
      },
      error: (error) => { alert("Internal Server Error")}
    })
   
      console.info("uploaded")
      this.receivedId = bundle['bundleId']
      this.router.navigate(['display', this.receivedId])
    }
  //   console.info('success')
  }


