package ubb.scs.map.laborator_6nou.domain.validation;

import ubb.scs.map.laborator_6nou.domain.User;

public class UserValidation implements Validation<User>{
    @Override
    public void validate(User entity){
        if(entity.getFirstName().isEmpty() || entity.getLastName().isEmpty()){
            throw new ValidationException("First Name and Last Name cannot be empty");
        }
        if(!entity.getEmail().contains("@")){
            throw new ValidationException("Email address is not a valid email address");
        }
        if(entity.getPassword().length()<6){
            throw new ValidationException("Password must be at least 6 characters");
        }
        if(entity.getFirstName().matches("[a-zA-Z]+")){
            throw new ValidationException("First name contains only letters");
        }
        if(entity.getLastName().matches("[a-zA-Z]+")){
            throw new ValidationException("Last name contains only letters");
        }
        if(!entity.getEmail().contains(".com")){
            throw new ValidationException("Email address is not a valid email address");
        }
    }
    public void validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters.");
        }
    }
    public void validateFirstName(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            throw new ValidationException("First Name cannot be empty");
        }
        if (!firstName.matches("[a-zA-Z]+")) {
            throw new ValidationException("First Name must contain only letters");
        }
    }
    public void validateLastName(String lastName) {
        if (lastName == null || lastName.isEmpty()) {
            throw new ValidationException("Last Name cannot be empty");
        }
        if (!lastName.matches("[a-zA-Z]+")) {
            throw new ValidationException("Last Name must contain only letters");
        }
    }
    public void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new ValidationException("Email cannot be empty");
        }
        if (!email.contains("@")) {
            throw new ValidationException("Email address must contain '@'");
        }
        if (!email.contains(".com")) {
            throw new ValidationException("Email address must contain '.com'");
        }
    }

}
