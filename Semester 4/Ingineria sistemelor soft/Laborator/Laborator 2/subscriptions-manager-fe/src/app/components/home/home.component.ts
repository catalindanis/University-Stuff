import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { SubscriptionsService, SubscriptionCategory } from '../../services/subscriptions.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule],
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

  constructor(
    private authService: AuthService,
    private subscriptionsService: SubscriptionsService
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
      next: (newCategory) => {
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
      next: (updatedCategory) => {
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

  private loadData() {
    this.checkAdminRole();
    if (this.isAdmin) {
      this.loadCategories();
      this.loadBillingTypes();
    }
  }
}
