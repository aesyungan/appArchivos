import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class UploadFileService {

  constructor(private http: HttpClient) { }

 
  pushFileToStorage2(file: File): Observable<any> {
    let formdata: FormData = new FormData();
    formdata.append('file', file);
    return this.http.post('http://localhost:8080/upload', formdata, {
      reportProgress: true,
      responseType: 'text',
      observe:'events',
    });
  }

  getFiles(): Observable<string[]> {
    return this.http.get<string[]>('http://localhost:8080/getallfiles')
  }
}
