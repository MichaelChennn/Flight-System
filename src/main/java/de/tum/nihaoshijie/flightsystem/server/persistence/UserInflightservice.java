package de.tum.nihaoshijie.flightsystem.server.persistence;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user_inflightservice")
public class UserInflightservice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inflightservice_id")
    private InFlightService inflightservice;

    @Column(name = "timestamp")
    private Instant timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public InFlightService getInflightservice() {
        return inflightservice;
    }

    public void setInflightservice(InFlightService inflightservice) {
        this.inflightservice = inflightservice;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public UserInflightservice(User user, InFlightService inflightservice, Instant timestamp) {
        this.user = user;
        this.inflightservice = inflightservice;
        this.timestamp = timestamp;
    }

    public UserInflightservice() {
    }
}