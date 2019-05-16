package io.pyxis.api.domain.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
public class Role extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_level")
    private Access accessLevel;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<ApplicationUser> users = new ArrayList<>();
}
