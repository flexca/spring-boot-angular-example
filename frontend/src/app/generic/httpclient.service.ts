import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {

    baseUrl: string = "http://localhost:8080"

    constructor(private http: HttpClient, private authenticationService: AuthenticationService) {
    }

    post<T>(uri: string, body: string) : Observable<T> {
        let headers: HttpHeaders = new HttpHeaders();
        headers = headers.set('content-type', 'application/json');
        const token = this.authenticationService.getToken();
        if (token && token.length > 0) {
          headers = headers.set('Authorization', 'Bearer ' + token);   
        }
        return this.http.post<T>(this.baseUrl + uri, body,{'headers':headers});
    }

    put<T>(uri: string, body: string) : Observable<T> {
      let headers: HttpHeaders = new HttpHeaders();
      headers = headers.set('content-type', 'application/json');
      const token = this.authenticationService.getToken();
      if (token && token.length > 0) {
        headers = headers.set('Authorization', 'Bearer ' + token);   
      }
      return this.http.put<T>(this.baseUrl + uri, body,{'headers':headers});
    }

    get<T>(uri: string, params: HttpParams) : Observable<T> {
        let headers: HttpHeaders = new HttpHeaders();
        headers = headers.set('content-type', 'application/json');
        const token = this.authenticationService.getToken();
        if (token && token.length > 0) {
          headers = headers.set('Authorization', 'Bearer ' + token);   
        }
        return this.http.get<T>(this.baseUrl + uri, {headers: headers, params: params});
    } 
}
