import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { apiRoutes } from '../constants/routes';
import { Observable } from 'rxjs';

export interface SubscriptionsCostResponse {
  value: number;
}

@Injectable({ providedIn: 'root' })
export class ReportsService {
  constructor(private http: HttpClient) {}

  getSubscriptionsCost(): Observable<SubscriptionsCostResponse> {
    return this.http.get<SubscriptionsCostResponse>(`${apiRoutes.reports}/costs`);
  }

  getPdfExport(): Observable<Blob> {
    // request PDF as a binary blob so the client can open it in a new tab or save it
    return this.http.get(`${apiRoutes.reports}/pdf`, { responseType: 'blob' as 'blob' });
  }
}
