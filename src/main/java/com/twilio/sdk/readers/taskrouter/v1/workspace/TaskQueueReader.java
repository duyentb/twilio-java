package com.twilio.sdk.readers.taskrouter.v1.workspace;

import com.twilio.sdk.clients.TwilioRestClient;
import com.twilio.sdk.exceptions.ApiConnectionException;
import com.twilio.sdk.exceptions.ApiException;
import com.twilio.sdk.http.HttpMethod;
import com.twilio.sdk.http.Request;
import com.twilio.sdk.http.Response;
import com.twilio.sdk.readers.Reader;
import com.twilio.sdk.resources.Page;
import com.twilio.sdk.resources.ResourceSet;
import com.twilio.sdk.resources.RestException;
import com.twilio.sdk.resources.taskrouter.v1.workspace.TaskQueue;

public class TaskQueueReader extends Reader<TaskQueue> {
    private final String workspaceSid;
    private String friendlyName;
    private String evaluateWorkerAttributes;

    /**
     * Construct a new TaskQueueReader
     * 
     * @param workspaceSid The workspace_sid
     */
    public TaskQueueReader(final String workspaceSid) {
        this.workspaceSid = workspaceSid;
    }

    /**
     * The friendly_name
     * 
     * @param friendlyName The friendly_name
     * @return this
     */
    public TaskQueueReader byFriendlyName(final String friendlyName) {
        this.friendlyName = friendlyName;
        return this;
    }

    /**
     * The evaluate_worker_attributes
     * 
     * @param evaluateWorkerAttributes The evaluate_worker_attributes
     * @return this
     */
    public TaskQueueReader byEvaluateWorkerAttributes(final String evaluateWorkerAttributes) {
        this.evaluateWorkerAttributes = evaluateWorkerAttributes;
        return this;
    }

    /**
     * Make the request to the Twilio API to perform the read
     * 
     * @param client TwilioRestClient with which to make the request
     * @return TaskQueue ResourceSet
     */
    @Override
    public ResourceSet<TaskQueue> execute(final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            TwilioRestClient.Domains.TASKROUTER,
            "/v1/Workspaces/" + this.workspaceSid + "/TaskQueues",
            client.getAccountSid()
        );
        
        addQueryParams(request);
        
        Page<TaskQueue> page = pageForRequest(client, request);
        
        return new ResourceSet<>(this, client, page);
    }

    /**
     * Retrieve the next page from the Twilio API
     * 
     * @param nextPageUri URI from which to retrieve the next page
     * @param client TwilioRestClient with which to make the request
     * @return Next Page
     */
    @Override
    public Page<TaskQueue> nextPage(final String nextPageUri, final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            nextPageUri,
            client.getAccountSid()
        );
        return pageForRequest(client, request);
    }

    /**
     * Generate a Page of TaskQueue Resources for a given request
     * 
     * @param client TwilioRestClient with which to make the request
     * @param request Request to generate a page for
     * @return Page for the Request
     */
    protected Page<TaskQueue> pageForRequest(final TwilioRestClient client, final Request request) {
        Response response = client.request(request);
        
        if (response == null) {
            throw new ApiConnectionException("TaskQueue read failed: Unable to connect to server");
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
        
        Page<TaskQueue> result = new Page<>();
        result.deserialize("task_queues", response.getContent(), TaskQueue.class, client.getObjectMapper());
        
        return result;
    }

    /**
     * Add the requested query string arguments to the Request
     * 
     * @param request Request to add query string arguments to
     */
    private void addQueryParams(final Request request) {
        if (friendlyName != null) {
            request.addQueryParam("FriendlyName", friendlyName);
        }
        
        if (evaluateWorkerAttributes != null) {
            request.addQueryParam("EvaluateWorkerAttributes", evaluateWorkerAttributes);
        }
        
        request.addQueryParam("PageSize", Integer.toString(getPageSize()));
    }
}