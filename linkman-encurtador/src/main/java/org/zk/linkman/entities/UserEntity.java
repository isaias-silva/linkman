package org.zk.linkman.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import org.zk.linkman.dto.UserDto;

import java.util.List;
import java.util.Set;

@Entity
public class UserEntity extends DefaultEntity<UserDto> {
    private String name;
    private String mail;
    private String password;
    private Set<String> rules;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LinkEntity> links;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRules() {
        return rules;
    }

    public void setRules(Set<String> rules) {
        this.rules = rules;
    }

    public List<LinkEntity> getLinks() {
        return links;
    }

    public void setLinks(List<LinkEntity> links) {
        this.links = links;
    }

    @Override
    public UserDto dto() {
        return new UserDto(getId(),name, mail, links.stream().map(LinkEntity::dto).toList());
    }
}
