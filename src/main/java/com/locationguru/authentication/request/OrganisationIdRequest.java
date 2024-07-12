package com.locationguru.authentication.request;

public class OrganisationIdRequest {
    private String organisationId;

 
    public OrganisationIdRequest() {}


    public OrganisationIdRequest(String organisationId) {
        this.organisationId = organisationId;
    }

    public String getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }
}
