import {useEffect, useRef, useState} from "react";
import {Client} from "@stomp/stompjs";
import SockJS from "sockjs-client";
import {getToken} from "../api/AuthenticationService.js";

const apiUrl = import.meta.env.VITE_API_BASE_URL;

function useUpdates() {
    const [messages, setMessages] = useState([]);
    const [privateMessages, setPrivateMessages] = useState([]);
    const [connected, setConnected] = useState(false);
    const clientRef = useRef(null);

    useEffect(() => {
        const token = getToken();

        const client = new Client({
            webSocketFactory: () => new SockJS(`${apiUrl}/ws`),
            reconnectDelay: 5000,
            connectHeaders: token ? { Authorization: `Bearer ${token}` } : {},

            onConnect: () => {
                setConnected(true);

                client.subscribe("/topic/updates", (message) => {
                    setMessages((prev) => [...prev, JSON.parse(message.body)]);
                });

                client.subscribe("/user/queue/private", (message) => {
                    setPrivateMessages((prev) => [...prev, JSON.parse(message.body)]);
                });
            },
            onDisconnect: () => {
                setConnected(false);
            },
            onStompError: (frame) => {
                console.error("STOMP error:", frame.headers["message"], frame.body);
            },
        });

        client.activate();
        clientRef.current = client;

        return () => {
            client.deactivate();
        };
    }, []);

    return { messages, privateMessages, connected };
}

export default useUpdates;