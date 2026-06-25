import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { getToken } from './AuthService';

let stompClient = null;
let showsSubscription = null;

const getWebSocketUrl = () => {
    const apiBaseUrl = process.env.REACT_APP_API_BASE_URL || window.location.origin;
    return `${apiBaseUrl.replace(/\/$/, '')}/ws`;
};

const parseRefreshMessage = (payload) => {
    let data = payload;

    try {
        data = JSON.parse(payload);
    } catch {
        // Payload can also be sent as plain string.
    }

    if (typeof data === 'string') {
        return data.trim().toLowerCase() === 'refresh';
    }

    if (data && typeof data === 'object') {
        const value = data.type || data.action || data.message;
        return typeof value === 'string' && value.trim().toLowerCase() === 'refresh';
    }

    return false;
};

export const connectShowsTopic = (onRefresh) => {
    const wsUrl = getWebSocketUrl();
    const token = getToken();
    console.log("Token at WS connect time:", token);

    if (stompClient) {
        disconnectShowsTopic();
    }

    stompClient = new Client({
        webSocketFactory: () => new SockJS(wsUrl),
        reconnectDelay: 5000,
        connectHeaders: token ? { Authorization: `Bearer ${token}` } : {},
        onConnect: () => {
            showsSubscription = stompClient.subscribe('/topic/shows', (message) => {
                if (parseRefreshMessage(message.body) && typeof onRefresh === 'function') {
                    onRefresh();
                }
            });
        },
        onStompError: (frame) => {
            console.error('STOMP error:', frame.headers?.message || frame.body);
        },
        onWebSocketError: (event) => {
            console.error('WebSocket error:', event);
        }
    });

    stompClient.activate();
};

export const disconnectShowsTopic = () => {
    if (showsSubscription) {
        showsSubscription.unsubscribe();
        showsSubscription = null;
    }

    if (stompClient) {
        stompClient.deactivate();
        stompClient = null;
    }
};

