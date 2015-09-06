package io.interact.mohamedbenarbia.benmycontacts.Util;

/**
 * This class regroups all the attributes that are used by different classes.
 * It principally contains server code status and URLs.
 */
public class SharedAttributes {


    // Define code states sent by Server
    public static final int OK_RESPONSE = 200;
    public static final int NOT_FOUND__RESPONSE = 404;
    public static final int UNAUTHORIZED__RESPONSE = 401;
    public static final int NO_CONTENT = 204;

    // Used if an internal error occured during  login (i.e JSON exception)
    public static final int INTERNAL_ERROR = 100;

    // Used if the user is not found
    public static final int USER_NOT_FOUND = 301;

    // Used if the pass word is incorrect
    public static final int INCORRECT_PASSWORD = 302;

    // User not found message
    public static final String USER_NOT_FOUND_MESSAGE = "login.user.not_found";
    public static final String INCORRECT_PASSWORD_MESSAGE = "login.user.invalid_password";

    /**
     * url of the web base web service
     */
    public static String BASE_URL="https://api.mycontacts.io/v2/";
    /**
     * Url of the mock web service, used for testing
     */
    public static String BASE_MOCK_URL= "https://private-anon-24dd0b27b-interactapiprod.apiary-mock.com/v2/";
    /**
     * login service uri
     */
    public static String LOGIN_URI="login";
    /**
     * logout service uri
     */
    public static String LOGOUT_URI="logout";
    /**
     * uri for reqesting the interaction list from the Interaction service
     */
    public static String INTERACTIONS_LIST_URI="interactions/list";





}