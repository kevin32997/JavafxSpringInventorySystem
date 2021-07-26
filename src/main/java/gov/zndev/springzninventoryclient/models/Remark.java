package gov.zndev.springzninventoryclient.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Remark {

    private int id;
    private int itemId;
    private String description;
    private int userId;
    private String type;
    private Date dateCreated;

}
