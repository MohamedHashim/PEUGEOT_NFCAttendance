package com.NFCFZLLE.eventmanagement.entities;

import com.NFCFZLLE.eventmanagement.constants.Globals;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class RequestParams
{

    private String endpointUrl;
    private String binding; //SOAP OR REST
    private String webMethodName;
    private int httpMethod;
    private String jsonRequest;
    private Enum<RequestKeys> requestKey;
    private Boolean isAuthenticated;
    private Boolean isCollection;
    private Map<String, String> params = new HashMap<>();


    public Boolean getAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(Boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public Boolean getCollection() {
        return isCollection;
    }

    public void setIsCollection(Boolean collection) {
        isCollection = collection;
    }

    public int getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(int httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getJsonRequest() {
        return jsonRequest;
    }

    public void setJsonRequest(String jsonRequest) {
        this.jsonRequest = jsonRequest;
    }

    //in case of collection , we need to specific the Type here because of reification (REF http://stackoverflow.com/questions/17436201/json-deserialization-with-gson-doesnt-work-correctly)
    private Type resultType;



	public RequestParams()
	{
		
	}
	
    public String getEndpointUrl() {
        if(endpointUrl==null || endpointUrl.length()==0)
            endpointUrl = Globals.WebServices.ENDPOINT_BASE_URL;
        return endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public Enum<RequestKeys> getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(Enum<RequestKeys> requestKey) {
        this.requestKey = requestKey;
    }

    public String getWebMethodName() {
        return webMethodName;
    }

    public void setWebMethodName(String webMethodName) {
        this.webMethodName = webMethodName;
    }

    public Type getResultType() {
        return resultType;
    }

    public void setResultType(Type resultType) {
        this.resultType = resultType;
    }

    @Override
    public String toString() {
        return "RequestParams{" +
                "endpointUrl='" + endpointUrl + '\'' +
                ", binding='" + binding + '\'' +
                ", webMethodName='" + webMethodName + '\'' +
                ", httpMethod=" + httpMethod +
                ", jsonRequest='" + jsonRequest + '\'' +
                ", requestKey=" + requestKey +
                ", isAuthenticated=" + isAuthenticated +
                ", isCollection=" + isCollection +
                ", resultType=" + resultType +
                '}';
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
