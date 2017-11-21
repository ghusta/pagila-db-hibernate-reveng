package fr.husta.test.dto;

public class ActorNamesDtoImpl implements ActorNamesDto {

    private String firstName;
    private String lastName;

    public ActorNamesDtoImpl() {
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
