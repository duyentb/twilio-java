package com.twilio.sdk.fetchers.api.v2010.account;

import com.twilio.sdk.clients.TwilioRestClient;
import com.twilio.sdk.exceptions.ApiConnectionException;
import com.twilio.sdk.exceptions.ApiException;
import com.twilio.sdk.fetchers.Fetcher;
import com.twilio.sdk.http.HttpMethod;
import com.twilio.sdk.http.Request;
import com.twilio.sdk.http.Response;
import com.twilio.sdk.resources.RestException;
import com.twilio.sdk.resources.api.v2010.account.Address;

public class AddressFetcher extends Fetcher<Address> {
    private final String accountSid;
    private final String sid;

    /**
     * Construct a new AddressFetcher
     * 
     * @param accountSid The account_sid
     * @param sid The sid
     */
    public AddressFetcher(final String accountSid, final String sid) {
        this.accountSid = accountSid;
        this.sid = sid;
    }

    /**
     * Make the request to the Twilio API to perform the fetch
     * 
     * @param client TwilioRestClient with which to make the request
     * @return Fetched Address
     */
    @Override
    public Address execute(final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            TwilioRestClient.Domains.API,
            "/2010-04-01/Accounts/" + this.accountSid + "/Addresses/" + this.sid + ".json",
            client.getAccountSid()
        );
        
        Response response = client.request(request);
        
        if (response == null) {
            throw new ApiConnectionException("Address fetch failed: Unable to connect to server");
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
        
        return Address.fromJson(response.getStream(), client.getObjectMapper());
    }
}