package com.NFCFZLLE.eventmanagement.interfaces;

import com.NFCFZLLE.eventmanagement.entities.RequestKeys;

public interface ValueCallback<T> {
    /**
     * Invoked when the value is available.
     * @param value The value.
     */
    public void onReceiveValue(T value, String message, RequestKeys requestKey);

    /**
     * Invoked when Error Received.
     * @param error The error.
     */
    public void onReceiveError(String error, RequestKeys requestKey);
};
