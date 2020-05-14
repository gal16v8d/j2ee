package co.com.gsdd.j2ee.db.jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
public abstract class AbstractJPAEntity implements Serializable {

    private static final long serialVersionUID = -7724134438374697539L;

    @Column(name = "last_modification", nullable = false)
    @Version
    public Timestamp lastModification;

    @PrePersist
    public void prePersist() {
        asignLastModTime();
    }

    @PreUpdate
    public void preUpdate() {
        asignLastModTime();
    }

    public void asignLastModTime() {
        Date date = new Date();
        setLastModification(new Timestamp(date.getTime()));
    }

}
