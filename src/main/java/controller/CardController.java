package controller;

import factory.ServiceFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CardService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/card")
public class CardController extends AbstractGsonServlet {

    protected final transient CardService cardService;

    CardController() {
        super();
        cardService = ServiceFactory.getInstance().getCardService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter printWriter = response.getWriter()) {
            var id = request.getParameter("id");
            var card = cardService.getById(Long.parseLong(id));

            printWriter.println(converter.toJson(card));
        }
    }
}
