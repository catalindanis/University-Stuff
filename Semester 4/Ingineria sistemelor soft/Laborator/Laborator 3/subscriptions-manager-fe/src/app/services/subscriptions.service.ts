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

export interface Subscription {
  id: number;
  authorId: number;
  subscriptionCategoryId: number;
  startDate: string;
}

export interface SubscriptionRequest {
  subscriptionCategoryId: number;
  startDate: string;
}

export interface SubscriptionUpdateRequest {
  id: number;
  subscriptionCategoryId: number;
  startDate: string;
}

export interface PaymentAlert {
  id: number;
  userId: number;
  subscriptionDetail: string;
  date: string;
}

export interface PaymentAlertRequest {
  subscriptionId: number;
  date: string;
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

  deleteSubscriptionCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${apiRoutes.subscriptionCategories}/${id}`);
  }

  getUserSubscriptions(startDate?: string | null, subscriptionCategoryId?: number | null): Observable<Subscription[]> {
    const params: any = {};
    if (startDate) params.startDate = startDate;
    if (subscriptionCategoryId != null) params.subscriptionCategoryId = subscriptionCategoryId;
    return this.http.get<Subscription[]>(apiRoutes.subscriptions, { params });
  }

  getSubscriptionById(id: number): Observable<Subscription> {
    return this.http.get<Subscription>(`${apiRoutes.subscriptions}/${id}`);
  }

  createSubscription(data: SubscriptionRequest): Observable<Subscription> {
    return this.http.post<Subscription>(apiRoutes.subscriptions, data);
  }

  updateSubscription(data: SubscriptionUpdateRequest): Observable<Subscription> {
    return this.http.put<Subscription>(apiRoutes.subscriptions, data);
  }

  deleteSubscription(id: number): Observable<void> {
    return this.http.delete<void>(`${apiRoutes.subscriptions}/${id}`);
  }

  createPaymentAlert(data: PaymentAlertRequest): Observable<void> {
    return this.http.post<void>(apiRoutes.paymentAlerts, data);
  }

  getPaymentAlerts(): Observable<PaymentAlert[]> {
    return this.http.get<PaymentAlert[]>(apiRoutes.paymentAlerts);
  }
}
