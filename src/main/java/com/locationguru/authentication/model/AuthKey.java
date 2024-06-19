package com.locationguru.authentication.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "auth_keys")
public class AuthKey {

	@Id
	@Column(name= "id")
	private String id;

    @Column(name = "key")
    private String key;

    @Column(name = "organisation_name")
    private String organisationName;

    @Column(name = "active")
    private Boolean active;

    public AuthKey() {
       
    }

    public AuthKey(String key, String organisationName, Boolean active) {
        this.key = key;
        this.organisationName = organisationName;
        this.active = active;
    }

  
    public String getId() {
 		return id;
 	}

 	public void setId(String id) {
 		this.id = id;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
