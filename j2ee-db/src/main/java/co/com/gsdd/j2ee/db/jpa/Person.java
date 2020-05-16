package co.com.gsdd.j2ee.db.jpa;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "person")
public class Person extends AbstractJPAEntity {

    private static final long serialVersionUID = 7739324464853818357L;

    @ApiModelProperty(value = "Primary key on database", required = false, hidden = true)
    @Id
    @Column(name = "person_id", nullable = false, length = 100)
    public String personId;

    @ApiModelProperty(required = true, example = "123")
    @Size(min = 3, max = 100, message = "IdNumber must have from 3 to 100 characters.")
    @Pattern(regexp = "[0-9]+", message = "IdNumber must be only numbers")
    @Column(name = "id_number", nullable = false, length = 100)
    public String idNumber;

    @ApiModelProperty(required = true, example = "Juan")
    @Size(min = 3, max = 100, message = "Name must have from 3 to 100 characters.")
    @Column(name = "name", nullable = false, length = 100)
    public String name;

    @ApiModelProperty(required = true, example = "Einstein")
    @Size(min = 3, max = 100, message = "Lastname must have from 3 to 100 characters.")
    @Column(name = "last_name", nullable = false, length = 100)
    public String lastName;

    public Person() {
        super();
        this.personId = UUID.randomUUID().toString();
    }

    public Person(Person u) {
        super();
        this.personId = u.getPersonId();
        this.idNumber = u.getIdNumber();
        this.name = u.getName();
        this.lastName = u.getLastName();
    }

}
