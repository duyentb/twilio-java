package com.twilio.sdk.fetchers.api.v2010.account.call;

import com.twilio.sdk.clients.TwilioRestClient;
import com.twilio.sdk.exceptions.ApiConnectionException;
import com.twilio.sdk.exceptions.ApiException;
import com.twilio.sdk.fetchers.Fetcher;
import com.twilio.sdk.http.HttpMethod;
import com.twilio.sdk.http.Request;
import com.twilio.sdk.http.Response;
import com.twilio.sdk.resources.RestException;
import com.twilio.sdk.resources.api.v2010.account.call.Feedback;

public class FeedbackFetcher extends Fetcher<Feedback> {
    private final String accountSid;
    private final String callSid;

    /**
     * Construct a new FeedbackFetcher
     * 
     * @param accountSid The account_sid
     * @param callSid The call sid that uniquely identifies the call
     */
    public FeedbackFetcher(final String accountSid, final String callSid) {
        this.accountSid = accountSid;
        this.callSid = callSid;
    }

    /**
     * Make the request to the Twilio API to perform the fetch
     * 
     * @param client TwilioRestClient with which to make the request
     * @return Fetched Feedback
     */
    @Override
    public Feedback execute(final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            TwilioRestClient.Domains.API,
            "/2010-04-01/Accounts/" + this.accountSid + "/Calls/" + this.callSid + "/Feedback.json",
            client.getAccountSid()
        );
        
        Response response = client.request(request);
        
        if (response == null) {
            throw new ApiConnectionException("Feedback fetch failed: Unable to connect to server");
        } else if (response.getStatusCode() != TwilioRestClient.HTTP_STATUS_CODE_OK) {
            RestException restException = RestException.fromJson(response.getStream(), client.getObjectMapper());
            throw new ApiException(
                restException.getMessage(),
                restException.getCode(),
                restException.getMoreInfo(),
                restException.getStatus(),
                null
            );
        }
        
        return Feedback.fromJson(response.getStream(), client.getObjectMapper());
    }
}