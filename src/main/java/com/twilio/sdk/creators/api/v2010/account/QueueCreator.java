package com.twilio.sdk.creators.api.v2010.account;

import com.twilio.sdk.clients.TwilioRestClient;
import com.twilio.sdk.creators.Creator;
import com.twilio.sdk.exceptions.ApiConnectionException;
import com.twilio.sdk.exceptions.ApiException;
import com.twilio.sdk.http.HttpMethod;
import com.twilio.sdk.http.Request;
import com.twilio.sdk.http.Response;
import com.twilio.sdk.resources.RestException;
import com.twilio.sdk.resources.api.v2010.account.Queue;

public class QueueCreator extends Creator<Queue> {
    private final String accountSid;
    private String friendlyName;
    private Integer maxSize;

    /**
     * Construct a new QueueCreator
     * 
     * @param accountSid The account_sid
     */
    public QueueCreator(final String accountSid) {
        this.accountSid = accountSid;
    }

    /**
     * A user-provided string that identifies this queue.
     * 
     * @param friendlyName A user-provided string that identifies this queue.
     * @return this
     */
    public QueueCreator setFriendlyName(final String friendlyName) {
        this.friendlyName = friendlyName;
        return this;
    }

    /**
     * The upper limit of calls allowed to be in the queue. The default is 100. The
     * maximum is 1000.
     * 
     * @param maxSize The max number of calls allowed in the queue
     * @return this
     */
    public QueueCreator setMaxSize(final Integer maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    /**
     * Make the request to the Twilio API to perform the create
     * 
     * @param client TwilioRestClient with which to make the request
     * @return Created Queue
     */
    @Override
    public Queue execute(final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.POST,
            TwilioRestClient.Domains.API,
            "/2010-04-01/Accounts/" + this.accountSid + "/Queues.json",
            client.getAccountSid()
        );
        
        addPostParams(request);
        Response response = client.request(request);
        
        if (response == null) {
            throw new ApiConnectionException("Queue creation failed: Unable to connect to server");
        } else if (response.getStatusCode() != TwilioRestClient.HTTP_STATUS_CODE_CREATED) {
            RestException restException = RestException.fromJson(response.getStream(), client.getObjectMapper());
            throw new ApiException(
                restException.getMessage(),
                restException.getCode(),
                restException.getMoreInfo(),
                restException.getStatus(),
                null
            );
        }
        
        return Queue.fromJson(response.getStream(), client.getObjectMapper());
    }

    /**
     * Add the requested post parameters to the Request
     * 
     * @param request Request to add post params to
     */
    private void addPostParams(final Request request) {
        if (friendlyName != null) {
            request.addPostParam("FriendlyName", friendlyName);
        }
        
        if (maxSize != null) {
            request.addPostParam("MaxSize", maxSize.toString());
        }
    }
}