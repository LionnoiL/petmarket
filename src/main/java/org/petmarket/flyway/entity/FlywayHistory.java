package org.petmarket.flyway.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@EqualsAndHashCode(of = {"installedRank"})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flyway_schema_history")
public class FlywayHistory {
    @Id
    @Column(name = "installed_rank")
    private Integer installedRank;

    @Column(name = "version")
    private String version;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Column(name = "script")
    private String script;

    @Column(name = "checksum")
    private Integer checksum;

    @Column(name = "installed_by")
    private String installedBy;

    @Column(name = "installed_on")
    private String installedOn;

    @Column(name = "execution_time")
    private Integer executionTime;

    @Column(name = "success")
    private Boolean success;
}
