package com.twilio.sdk.deleters;

import com.twilio.sdk.clients.TwilioRestClient;
import com.twilio.sdk.exceptions.ApiConnectionException;
import com.twilio.sdk.exceptions.ApiException;
import com.twilio.sdk.http.HttpMethod;
import com.twilio.sdk.http.Request;
import com.twilio.sdk.http.Response;
import com.twilio.sdk.resources.OutgoingCallerId;

public class OutgoingCallerIdDeleter extends Deleter<OutgoingCallerId> {

    private final String sid;

    public OutgoingCallerIdDeleter(final String sid) {
        this.sid = sid;
    }

    public OutgoingCallerIdDeleter(final OutgoingCallerId target) {
        this(target.getSid());
    }

    @Override
    public void execute(final TwilioRestClient client) {
        Request request = new Request(HttpMethod.DELETE, "/OutgoingCallerIds/" + sid + ".json");
        Response response = client.request(request);

        if (response == null) {
            throw new ApiConnectionException("OutgoingCallerId delete failed: Unable to connect to server");
        } else if (response.getStatusCode() != TwilioRestClient.HTTP_STATUS_CODE_NO_CONTENT) {
            throw new ApiException(
                    "OutgoingCallerId delete failed: [" + response.getStatusCode() + "] " + response.getContent());
        }
    }
}
