import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { apiRoutes } from '../constants/routes';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'token';

  constructor(private http: HttpClient) {}

  saveToken(token: string) {
    localStorage.setItem(this.tokenKey, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  login(email: string, password: string) {
    return this.http.post(apiRoutes.login, { email, password });
  }

  register(firstName: string, lastName: string, email: string, password: string, confirmPassword: string) {
    return this.http.post(apiRoutes.register, { firstName, lastName, email, password, confirmPassword });
  }

  decodeToken(): any {
    const token = this.getToken();

    if (!token) {
      return null;
    }

    try {
      const parts = token.split('.');
      if (parts.length !== 3) {
        return null;
      }

      const payload = parts[1];
      const decoded = JSON.parse(atob(payload));
      return decoded;
    } catch (error) {
      console.error('Failed to decode token:', error);
      return null;
    }
  }

  getTokenClaim(key: string): any {
    const decoded = this.decodeToken();
    return decoded?.[key] ?? null;
  }

  hasRole(role: string): boolean {
    const roles = this.getTokenClaim('groups');
    return Array.isArray(roles) && roles.includes(role.toUpperCase());
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
  }
}
