package listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {

    private static final String SESSION_ATTRIBUTE = "USER_ROLE";
    private static final String DEFAULT_ROLE = "GUEST";

    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        sessionEvent.getSession().setAttribute(SESSION_ATTRIBUTE, DEFAULT_ROLE);
    }
}
