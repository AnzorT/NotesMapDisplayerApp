package com.example.moveotask;

/**
 * <p>
 *     Author: Anzor Torikashvili.
 *     <br>
 *     This class describes User.
 * </p>
 */
public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    /**
     * empty constructor of User.
     */
    public User(){

    }

    /**
     * Constructor of User.
     * @param firstName name of the user.
     * @param lastName family name of the user.
     * @param email email of the user.
     * @param password user's login password for the app
     */
    public User(String firstName, String lastName, String email, String password) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setPassword(password);
    }

    /**
     * set firsName filed to the value this function receives.
     * @param firstName the value we will input into firstName.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * set lastName filed to the value this function receives.
     * @param lastName the value we will input into lastName.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * set email filed to the value this function receives.
     * @param email the value we will input into email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * set password filed to the value this function receives.
     * @param password the value we will input into password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Return String type.
     * @return first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Return String type.
     * @return last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Return String type.
     * @return password of the user for login.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Return String type.
     * @return email of the user for login.
     */
    public String getEmail() {
        return email;
    }
}
