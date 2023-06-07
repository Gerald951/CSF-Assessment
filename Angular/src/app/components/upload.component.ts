import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UploadSearchService } from '../services/upload-search.service';
import { Router } from '@angular/router';
import { Bundle } from '../model/bundle';
import { Upload } from '../model/upload';
import { Subscription } from 'rxjs';

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
  // posted$! : Subscription

  @ViewChild('file') zip : ElementRef

  constructor(private fb : FormBuilder, private searchSvc:UploadSearchService, private router:Router) {}

  ngOnInit(): void {
      this.uploadForm = this.createForm()
  }

  private createForm() {
    return this.fb.group({
      name: this.fb.control('', Validators.required),
      title: this.fb.control('', Validators.required),
      comment: this.fb.control('', Validators.required),
      zipFile: this.fb.control('', Validators.required)

    })
  }

 submit() {
    const formData = new FormData()
    formData.set('name', this.uploadForm.value['name'])
    formData.set('archive', this.zip.nativeElement.files[0])
    formData.set('title', this.uploadForm.value['title'])
    formData.set('comment', this.uploadForm.value['comment'])


   this.searchSvc.uploadForm(formData).subscribe({
      next: (data : Upload) => {
        this.receivedId= data.bundleId
        console.info('receivedId>>>' + this.receivedId)
        this.router.navigate(['display', this.receivedId])
      },
      error: (error) => { alert("Internal Server Error")}
    })
   
      
    }
  //   console.info('success')
  }


