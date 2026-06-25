const url = "http://localhost:8080";
const apiUrl = `${url}/api`;

export const apiRoutes = {
  login: `${apiUrl}/auth/login`,
  register: `${apiUrl}/auth/register`,
  subscriptionCategories: `${apiUrl}/subscriptions/categories`,
  billingTypes: `${apiUrl}/subscriptions/categories/billingTypes`,
  subscriptions: `${apiUrl}/subscriptions`,
  reports: `${apiUrl}/reports`,
  paymentAlerts: `${apiUrl}/subscriptions/alerts`,
}
