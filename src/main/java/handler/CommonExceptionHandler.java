package handler;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;

import static jakarta.servlet.RequestDispatcher.ERROR_EXCEPTION;

@Slf4j
@WebServlet(urlPatterns = "/error")
public class CommonExceptionHandler extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(404);
        Exception exception = (Exception) request.getAttribute(ERROR_EXCEPTION);
        log.error(exception.getMessage());
        PrintWriter printWriter = response.getWriter();
        printWriter.print("{\"error\": \"" + exception.getMessage() + "\"}");
        printWriter.flush();
    }
}
