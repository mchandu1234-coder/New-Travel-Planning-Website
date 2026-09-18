import React, { createContext, useContext, useEffect, useState, useRef } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { useAuth } from './AuthContext';

const WebSocketContext = createContext(null);

export const WebSocketProvider = ({ children }) => {
  const { token } = useAuth();
  const [stompClient, setStompClient] = useState(null);
  const [connected, setConnected] = useState(false);
  const subscriptionsRef = useRef({});

  useEffect(() => {
    if (!token) {
      if (stompClient) {
        stompClient.deactivate();
        setConnected(false);
      }
      return;
    }

    const client = new Client({
      webSocketFactory: () => new SockJS('/ws-travel'),
      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },
      debug: (str) => {
        // console.log('STOMP:', str);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    client.onConnect = () => {
      setConnected(true);
    };

    client.onDisconnect = () => {
      setConnected(false);
    };

    client.onStompError = (frame) => {
      console.error('STOMP error:', frame.headers['message']);
    };

    client.activate();
    setStompClient(client);

    return () => {
      client.deactivate();
    };
  }, [token]);

  const subscribeToTrip = (tripId, onItineraryUpdate, onChatMessage) => {
    if (!stompClient || !connected) return null;

    const sub1 = stompClient.subscribe(`/topic/trips/${tripId}/itinerary`, (message) => {
      const body = JSON.parse(message.body);
      if (onItineraryUpdate) onItineraryUpdate(body);
    });

    const sub2 = stompClient.subscribe(`/topic/trips/${tripId}/chat`, (message) => {
      const body = JSON.parse(message.body);
      if (onChatMessage) onChatMessage(body);
    });

    return () => {
      sub1.unsubscribe();
      sub2.unsubscribe();
    };
  };

  const sendChatMessage = (tripId, content) => {
    if (stompClient && connected) {
      stompClient.publish({
        destination: `/app/trips/${tripId}/chat`,
        body: JSON.stringify({ content }),
      });
    }
  };

  const sendItineraryUpdate = (tripId, action, data) => {
    if (stompClient && connected) {
      stompClient.publish({
        destination: `/app/trips/${tripId}/itinerary.update`,
        body: JSON.stringify({ action, data }),
      });
    }
  };

  return (
    <WebSocketContext.Provider
      value={{
        connected,
        subscribeToTrip,
        sendChatMessage,
        sendItineraryUpdate,
      }}
    >
      {children}
    </WebSocketContext.Provider>
  );
};

export const useWebSocket = () => useContext(WebSocketContext);
