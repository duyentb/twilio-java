package com.twilio.sdk.fetchers.api.v2010.account.conference;

import com.twilio.sdk.clients.TwilioRestClient;
import com.twilio.sdk.exceptions.ApiConnectionException;
import com.twilio.sdk.exceptions.ApiException;
import com.twilio.sdk.fetchers.Fetcher;
import com.twilio.sdk.http.HttpMethod;
import com.twilio.sdk.http.Request;
import com.twilio.sdk.http.Response;
import com.twilio.sdk.resources.RestException;
import com.twilio.sdk.resources.api.v2010.account.conference.Participant;

public class ParticipantFetcher extends Fetcher<Participant> {
    private final String accountSid;
    private final String conferenceSid;
    private final String callSid;

    /**
     * Construct a new ParticipantFetcher
     * 
     * @param accountSid The account_sid
     * @param conferenceSid The string that uniquely identifies this conference
     * @param callSid The call_sid
     */
    public ParticipantFetcher(final String accountSid, final String conferenceSid, final String callSid) {
        this.accountSid = accountSid;
        this.conferenceSid = conferenceSid;
        this.callSid = callSid;
    }

    /**
     * Make the request to the Twilio API to perform the fetch
     * 
     * @param client TwilioRestClient with which to make the request
     * @return Fetched Participant
     */
    @Override
    public Participant execute(final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            TwilioRestClient.Domains.API,
            "/2010-04-01/Accounts/" + this.accountSid + "/Conferences/" + this.conferenceSid + "/Participants/" + this.callSid + ".json",
            client.getAccountSid()
        );
        
        Response response = client.request(request);
        
        if (response == null) {
            throw new ApiConnectionException("Participant fetch failed: Unable to connect to server");
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
        
        return Participant.fromJson(response.getStream(), client.getObjectMapper());
    }
}