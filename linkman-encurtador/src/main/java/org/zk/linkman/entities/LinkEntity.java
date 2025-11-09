package org.zk.linkman.entities;

import jakarta.persistence.*;
import org.zk.linkman.dto.LinkDto;

@Entity
public class LinkEntity extends DefaultEntity<LinkDto> {

    private String originalUrl;
    private String url;
    private String title;

    @ManyToOne
    private UserEntity user;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public LinkDto dto() {
        return new LinkDto(getId(), title, url, originalUrl);
    }
}
