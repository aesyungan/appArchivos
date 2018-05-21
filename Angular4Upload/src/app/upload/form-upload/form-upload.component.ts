import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpResponse, HttpEventType } from '@angular/common/http';
import { UploadFileService } from '../upload-file.service';

@Component({
  selector: 'form-upload',
  templateUrl: './form-upload.component.html',
  styleUrls: ['./form-upload.component.css']
})
export class FormUploadComponent implements OnInit {

  selectedFiles: FileList
  currentFileUpload: File
  progress: { percentage: number } = { percentage: 0 }

  constructor(private uploadService: UploadFileService) { }

  ngOnInit() {
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
    this.progress.percentage=0;
  }


  upload2() {

    this.currentFileUpload = this.selectedFiles.item(0)
    this.uploadService.pushFileToStorage2(this.currentFileUpload).subscribe(event => {
      if (event.type === HttpEventType.UploadProgress) {
        this.progress.percentage = Math.round(100 * event.loaded / event.total);
      } else if (event.type === HttpEventType.Response) {
        console.log('File is completely uploaded!');
        console.log(event.body);//viene los datos que estan bien 
       
      }
    }, error => {
      
      console.log(error.error);
    });

    this.selectedFiles = undefined
  }
}
