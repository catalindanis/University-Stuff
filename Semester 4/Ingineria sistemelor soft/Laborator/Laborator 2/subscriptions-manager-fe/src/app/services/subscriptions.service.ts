import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { apiRoutes } from '../constants/routes';
import { Observable } from 'rxjs';

export interface SubscriptionCategory {
  id: number;
  companyName: string;
  billingType: string;
  price: number;
}

export interface CreateSubscriptionCategoryRequest {
  companyName: string;
  billingType: string;
  price: number;
}

export interface UpdateSubscriptionCategoryRequest {
  id: number;
  companyName: string;
  billingType: string;
  price: number;
}

@Injectable({ providedIn: 'root' })
export class SubscriptionsService {
  constructor(private http: HttpClient) {}

  getSubscriptionCategories(): Observable<SubscriptionCategory[]> {
    return this.http.get<SubscriptionCategory[]>(apiRoutes.subscriptionCategories);
  }

  getBillingTypes(): Observable<string[]> {
    return this.http.get<string[]>(apiRoutes.billingTypes);
  }

  createSubscriptionCategory(data: CreateSubscriptionCategoryRequest): Observable<SubscriptionCategory> {
    return this.http.post<SubscriptionCategory>(apiRoutes.subscriptionCategories, data);
  }

  updateSubscriptionCategory(data: UpdateSubscriptionCategoryRequest): Observable<SubscriptionCategory> {
    return this.http.put<SubscriptionCategory>(apiRoutes.subscriptionCategories, data);
  }
}
