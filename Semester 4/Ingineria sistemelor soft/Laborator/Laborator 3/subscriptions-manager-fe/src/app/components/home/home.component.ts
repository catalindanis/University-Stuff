import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import {
  SubscriptionsService,
  SubscriptionCategory,
  Subscription,
  PaymentAlert
} from '../../services/subscriptions.service';
import {ReportsService} from '../../services/reports.service';
import {ModalmessageComponent} from '../utils/modalmessage/modalmessage.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, ModalmessageComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  isAdmin = false;
  categories: SubscriptionCategory[] = [];
  billingTypes: string[] = [];
  loading = false;
  errorMessage = '';
  successMessage = '';

  formData = {
    companyName: '',
    billingType: '',
    price: ''
  };
  formLoading = false;
  formError = '';

  isModalOpen = false;
  editingCategory: SubscriptionCategory | null = null;
  modalFormData = {
    companyName: '',
    billingType: '',
    price: ''
  };
  modalFormLoading = false;
  modalFormError = '';
  modalSuccessMessage = '';

  subscriptions: Subscription[] = [];
  subsLoading = false;
  subsError = '';

  subscriptionForm = {
    subscriptionCategoryId: null as number | null,
    startDate: ''
  };
  subscriptionFormLoading = false;
  subscriptionFormError = '';
  subscriptionSuccess = '';

  editingSubscription: Subscription | null = null;
  isSubscriptionModalOpen = false;
  modalSubscriptionData = {
    subscriptionCategoryId: null as number | null,
    startDate: ''
  };
  modalSubscriptionLoading = false;
  modalSubscriptionError = '';
  modalSubscriptionSuccess = '';

  filterStartDate = '';
  filterCategoryId: number | null = null;

  constructor(
    private authService: AuthService,
    private subscriptionsService: SubscriptionsService,
    private reportsService: ReportsService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  private checkAdminRole(): void {
    this.isAdmin = this.authService.hasRole('ADMIN');
  }

  private loadCategories(): void {
    this.loading = true;
    this.errorMessage = '';

    this.subscriptionsService.getSubscriptionCategories().subscribe({
      next: (data) => {
        this.categories = data;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage =
          error?.error?.message || 'Failed to load subscription categories.';
        this.loading = false;
      }
    });
  }

  private loadBillingTypes(): void {
    this.subscriptionsService.getBillingTypes().subscribe({
      next: (data) => {
        this.billingTypes = data;
        if (this.billingTypes.length > 0) {
          this.formData.billingType = this.billingTypes[0];
        }
      },
      error: (error) => {
        console.error('Failed to load billing types:', error);
      }
    });
  }

  loadUserSubscriptions(startDate?: string | null, subscriptionCategoryId?: number | null): void {
    this.subsLoading = true;
    this.subsError = '';

    this.subscriptionsService.getUserSubscriptions(startDate ?? null, subscriptionCategoryId ?? null).subscribe({
      next: (data) => {
        this.subscriptions = data;
        this.subsLoading = false;
      },
      error: (error) => {
        this.subsError = error?.error?.message || 'Failed to load your subscriptions.';
        this.subsLoading = false;
      }
    });
  }

  onAddSubscription(): void {
    this.subscriptionFormError = '';

    if (!this.subscriptionForm.subscriptionCategoryId) {
      this.subscriptionFormError = 'Please select a subscription category.';
      return;
    }

    if (!this.subscriptionForm.startDate || !this.subscriptionForm.startDate.trim()) {
      this.subscriptionFormError = 'Start date is required.';
      return;
    }

    this.subscriptionFormLoading = true;

    const request = {
      subscriptionCategoryId: Number(this.subscriptionForm.subscriptionCategoryId),
      startDate: this.subscriptionForm.startDate
    };

    this.subscriptionsService.createSubscription(request).subscribe({
      next: () => {
        this.subscriptionSuccess = 'Subscription created successfully!';
        this.loadUserSubscriptions(this.filterStartDate || null, this.filterCategoryId);
        this.resetSubscriptionForm();
        this.subscriptionFormLoading = false;

        setTimeout(() => (this.subscriptionSuccess = ''), 3000);
      },
      error: (error) => {
        this.subscriptionFormError = error?.error?.message || 'Failed to create subscription.';
        this.subscriptionFormLoading = false;
      }
    });
  }

  openEditSubscriptionModal(subscription: Subscription): void {
    this.editingSubscription = subscription;
    this.modalSubscriptionData.subscriptionCategoryId = subscription.subscriptionCategoryId;
    this.modalSubscriptionData.startDate = subscription.startDate;
    this.modalSubscriptionError = '';
    this.modalSubscriptionSuccess = '';
    this.isSubscriptionModalOpen = true;
  }

  closeSubscriptionModal(): void {
    this.isSubscriptionModalOpen = false;
    this.editingSubscription = null;
    this.resetModalSubscriptionForm();
  }

  onUpdateSubscription(): void {
    this.modalSubscriptionError = '';

    if (!this.modalSubscriptionData.subscriptionCategoryId) {
      this.modalSubscriptionError = 'Please select a subscription category.';
      return;
    }

    if (!this.modalSubscriptionData.startDate || !this.modalSubscriptionData.startDate.trim()) {
      this.modalSubscriptionError = 'Start date is required.';
      return;
    }

    if (!this.editingSubscription) {
      this.modalSubscriptionError = 'No subscription selected for editing.';
      return;
    }

    this.modalSubscriptionLoading = true;

    const request = {
      id: this.editingSubscription.id,
      subscriptionCategoryId: Number(this.modalSubscriptionData.subscriptionCategoryId),
      startDate: this.modalSubscriptionData.startDate
    };

    this.subscriptionsService.updateSubscription(request).subscribe({
      next: () => {
        this.modalSubscriptionSuccess = 'Subscription updated successfully!';
        this.loadUserSubscriptions(this.filterStartDate || null, this.filterCategoryId);
        this.modalSubscriptionLoading = false;

        setTimeout(() => this.closeSubscriptionModal(), 1200);
      },
      error: (error) => {
        this.modalSubscriptionError = error?.error?.message || 'Failed to update subscription.';
        this.modalSubscriptionLoading = false;
      }
    });
  }

  onDeleteSubscription(subscription: Subscription): void {
    if (!confirm('Are you sure you want to delete this subscription?')) return;

    this.subscriptionsService.deleteSubscription(subscription.id).subscribe({
      next: () => {
        this.subscriptionSuccess = 'Subscription deleted successfully!';
        this.loadUserSubscriptions(this.filterStartDate || null, this.filterCategoryId);
        setTimeout(() => (this.subscriptionSuccess = ''), 3000);
      },
      error: (error) => {
        this.subsError = error?.error?.message || 'Failed to delete subscription.';
      }
    });
  }

  applyFilters(): void {
    const start = this.filterStartDate && this.filterStartDate.trim() ? this.filterStartDate : null;
    this.loadUserSubscriptions(start, this.filterCategoryId ?? null);
  }

  private resetSubscriptionForm(): void {
    this.subscriptionForm.subscriptionCategoryId = this.categories.length > 0 ? this.categories[0].id : null;
    this.subscriptionForm.startDate = '';
    this.subscriptionFormError = '';
  }

  private resetModalSubscriptionForm(): void {
    this.modalSubscriptionData.subscriptionCategoryId = null;
    this.modalSubscriptionData.startDate = '';
    this.modalSubscriptionError = '';
    this.modalSubscriptionSuccess = '';
  }

  getCategoryById(id: number | undefined | null): SubscriptionCategory | undefined {
    if (id == null) return undefined;
    return this.categories.find((c) => c.id === id);
  }

  onAddCategory(): void {
    this.formError = '';

    if (!this.formData.companyName.trim()) {
      this.formError = 'Company name is required.';
      return;
    }

    if (!this.formData.billingType) {
      this.formError = 'Billing type is required.';
      return;
    }

    if (!this.formData.price || parseFloat(this.formData.price) <= 0) {
      this.formError = 'Price must be a valid positive number.';
      return;
    }

    this.formLoading = true;

    const request = {
      companyName: this.formData.companyName.trim(),
      billingType: this.formData.billingType,
      price: parseFloat(this.formData.price)
    };

    this.subscriptionsService.createSubscriptionCategory(request).subscribe({
      next: () => {
        this.successMessage = 'Subscription category created successfully!';
        this.loadData();
        this.resetForm();
        this.formLoading = false;

        setTimeout(() => {
          this.successMessage = '';
        }, 3000);
      },
      error: (error) => {
        this.formError =
          error?.error?.message || 'Failed to create subscription category.';
        this.formLoading = false;
      }
    });
  }

  onDeleteCategory(category: SubscriptionCategory): void {
    if (!confirm(`Are you sure you want to delete the category "${category.companyName}"?`)) {
      return;
    }

    this.subscriptionsService.deleteSubscriptionCategory(category.id).subscribe({
      next: () => {
        this.successMessage = 'Subscription category deleted successfully!';
        this.loadData();

        setTimeout(() => {
          this.successMessage = '';
        }, 3000);
      },
      error: (error: any) => {
        this.errorMessage =
          error?.error?.message || 'Failed to delete subscription category.';
      }
    });
  }

  private resetForm(): void {
    this.formData.companyName = '';
    this.formData.price = '';
    if (this.billingTypes.length > 0) {
      this.formData.billingType = this.billingTypes[0];
    }
  }

  openEditModal(category: SubscriptionCategory): void {
    this.editingCategory = category;
    this.modalFormData.companyName = category.companyName;
    this.modalFormData.billingType = category.billingType;
    this.modalFormData.price = category.price.toString();
    this.modalFormError = '';
    this.modalSuccessMessage = '';
    this.isModalOpen = true;
  }

  closeModal(): void {
    this.isModalOpen = false;
    this.editingCategory = null;
    this.resetModalForm();
  }

  onUpdateCategory(): void {
    this.modalFormError = '';

    if (!this.modalFormData.companyName.trim()) {
      this.modalFormError = 'Company name is required.';
      return;
    }

    if (!this.modalFormData.billingType) {
      this.modalFormError = 'Billing type is required.';
      return;
    }

    if (!this.modalFormData.price || parseFloat(this.modalFormData.price) <= 0) {
      this.modalFormError = 'Price must be a valid positive number.';
      return;
    }

    if (!this.editingCategory) {
      this.modalFormError = 'No category selected for editing.';
      return;
    }

    this.modalFormLoading = true;

    const request = {
      id: this.editingCategory.id,
      companyName: this.modalFormData.companyName.trim(),
      billingType: this.modalFormData.billingType,
      price: parseFloat(this.modalFormData.price)
    };

    this.subscriptionsService.updateSubscriptionCategory(request).subscribe({
      next: () => {
        this.modalSuccessMessage = 'Subscription category updated successfully!';
        this.loadData();
        this.modalFormLoading = false;

        setTimeout(() => {
          this.closeModal();
        }, 1500);
      },
      error: (error) => {
        this.modalFormError =
          error?.error?.message || 'Failed to update subscription category.';
        this.modalFormLoading = false;
      }
    });
  }

  private resetModalForm(): void {
    this.modalFormData.companyName = '';
    this.modalFormData.price = '';
    this.modalFormError = '';
    this.modalSuccessMessage = '';
  }

  private async loadData() {
    this.checkAdminRole();
    this.loadCategories();
    this.loadBillingTypes();

    if (!this.isAdmin) {
      this.loadUserSubscriptions();
      this.loadUserPaymentAlerts();
    }
  }

  onCalculateCosts() {
    this.reportsService.getSubscriptionsCost().subscribe({
      next: (data) => {
        this.showMessageModal(`You have payed $${data.value.toFixed(2)} in total.`);
      },
      error: (error) => {
        this.errorMessage = error?.error?.message || 'Failed to calculate subscription costs.';
      }
    });
  }

  costModalMessage = '';
  isMessageModalOpen = false;
  showMessageModal(message: string): void {
    this.costModalMessage = message;
    this.isMessageModalOpen = true;
  }

  closeShowCostModal() {
    this.isMessageModalOpen = false;
  }

  onExportReport() {
    this.reportsService.getPdfExport().subscribe({
      next: (blob: Blob) => {
        const nav: any = (window as any).navigator;
        if (nav && typeof nav.msSaveOrOpenBlob === 'function') {
          nav.msSaveOrOpenBlob(blob, 'subscriptions-report.pdf');
          return;
        }

        const url = window.URL.createObjectURL(blob);
        window.open(url, '_blank');

        setTimeout(() => {
          try {
            window.URL.revokeObjectURL(url);
          } catch (e) {
          }
        }, 10000);
      },
      error: (error) => {
        this.errorMessage = error?.error?.message || 'Failed to export pdf subscription report.';
      }
    })
  }

  isPaymentAlertModalOpen = false;
  paymentAlertData = {
    subscription: null as Subscription | null,
    date: ''
  };
  modalAlertSuccess = '';
  modalAlertError = '';
  openPaymentAlertModal(s: Subscription) {
    this.isPaymentAlertModalOpen = true;
    this.paymentAlertData.subscription = s;
  }

  closePaymentAlertModal() {
    this.isPaymentAlertModalOpen = false;
  }

  addPaymentAlert() {
    this.subscriptionsService.createPaymentAlert({
      subscriptionId: this.paymentAlertData.subscription!.id,
      date: this.paymentAlertData.date
    }).subscribe({
      next: () => {
        this.modalAlertSuccess = 'Payment alert created successfully!';
        setTimeout(() => {
          this.modalAlertSuccess = '';
          this.closePaymentAlertModal();
        }, 2000);
      },
      error: (error) => {
        this.modalAlertError = error?.error?.message || 'Failed to create payment alert.';
      }
    });
  }

  paymentAlerts: PaymentAlert[] = [];
  loadUserPaymentAlerts() {
    return this.subscriptionsService.getPaymentAlerts().subscribe({
      next: (data) => {
        this.paymentAlerts = data;
        this.processPaymentAlerts();
      },
      error: (error) => {
          this.errorMessage = error?.error?.message || 'Failed to load your payment alerts.';
      }
    })
  }

  paymentAlertsMessage = '';
  private processPaymentAlerts() {
    if(localStorage.getItem('paymentAlertsShown')) {
      return;
    }

    const now = new Date();
    const todayAlerts: PaymentAlert[] = [];

    this.paymentAlerts.forEach(alert => {
      const alertDate = new Date(alert.date);
      if(alertDate.toDateString() == now.toDateString()) {
        todayAlerts.push(alert);
      }
    });

    if(todayAlerts.length > 0) {
      localStorage.setItem('paymentAlertsShown', 'true');

      this.paymentAlertsMessage = `You have ${todayAlerts.length} payment alert(s) for today!`;
      todayAlerts.forEach(alert => {
        this.paymentAlertsMessage += `\n   * ${alert.subscriptionDetail}`;
      });
      this.showMessageModal(this.paymentAlertsMessage);
    }
  }
}
