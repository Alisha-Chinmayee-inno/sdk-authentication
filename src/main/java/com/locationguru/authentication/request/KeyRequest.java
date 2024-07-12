package com.locationguru.authentication.request;

public class KeyRequest {

    private String key;
    private String organisationName;
    
    

    public KeyRequest() {
		super();
	}

	public KeyRequest(String key, String organisationName) {
		super();
		this.key = key;
		this.organisationName = organisationName;
	}

	public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }
}
