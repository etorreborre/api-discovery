package org.zalando.stups.twintip.storage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "api")
@JsonIgnoreProperties(ignoreUnknown = true)
class ApiDefinition {

    @Id
    @JsonProperty(value = "application_id")
    private String applicationId;

    @Column(nullable = false)
    private String status;

    @JsonProperty(value = "lifecycle_state")
    @Column(nullable = false)
    private String lifecycleState;

    private String type;

    private String name;

    private String version;

    @JsonProperty(value = "service_url")
    private String serviceUrl;

    private String url;

    private String ui;

    private String definition;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime created;

    @JsonProperty(value = "last_changed")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastChanged;

    @JsonProperty(value = "last_persisted")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastPersisted;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLifecycleState() {
        return lifecycleState;
    }

    public void setLifecycleState(String lifecycleState) {
        this.lifecycleState = lifecycleState;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUi() {
        return ui;
    }

    public void setUi(String ui) {
        this.ui = ui;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public DateTime getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(DateTime lastChanged) {
        this.lastChanged = lastChanged;
    }

    public DateTime getLastPersisted() {
        return lastPersisted;
    }

    public void setLastPersisted(DateTime lastPersisted) {
        this.lastPersisted = lastPersisted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiDefinition that = (ApiDefinition) o;

        if (!applicationId.equals(that.applicationId)) return false;
        if (!status.equals(that.status)) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;
        if (serviceUrl != null ? !serviceUrl.equals(that.serviceUrl) : that.serviceUrl != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (ui != null ? !ui.equals(that.ui) : that.ui != null) return false;
        return definition != null ? definition.equals(that.definition) : that.definition == null;

    }

    @Override
    public int hashCode() {
        int result = applicationId.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (serviceUrl != null ? serviceUrl.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (ui != null ? ui.hashCode() : 0);
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        return result;
    }
}
