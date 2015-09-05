package io.interact.mohamedbenarbia.benmycontacts;

/**
 *
 *  Shared resource that define the different codes returned by the server or defined.
 * Created by MohamedBenArbia on 05/09/15.
 */
public class StatusCodes {

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



}
