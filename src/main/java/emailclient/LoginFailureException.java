package emailclient;

public class LoginFailureException extends Exception
{
    public LoginFailureException(String msg)
    {
        super(msg);
    }
}
