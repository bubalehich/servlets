package controller;

import controller.model.CashReceiptMutationModel;
import factory.ServiceFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CashReceiptService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

@WebServlet(urlPatterns = "/cash-receipt")
public class CashReceiptController extends AbstractGsonServlet {

    protected final transient CashReceiptService cashReceiptService;

    CashReceiptController() {
        super();
        cashReceiptService = ServiceFactory.getInstance().getCashReceiptService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter printWriter = response.getWriter()) {
            var itemQuantityList = request.getParameter("itemQuantityList");
            var cardNumber = request.getParameter("cardNumber");

            CashReceiptMutationModel model = new CashReceiptMutationModel();
            model.setCardNumber(cardNumber);
            model.setItemQuantityList(Arrays.stream(itemQuantityList.split(" ")).toList());

            var cashReceipt = cashReceiptService.create(model);

            printWriter.println(converter.toJson(cashReceipt));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter printWriter = response.getWriter()) {
            var id = request.getParameter("id");
            var cashReceipt = cashReceiptService.getById(Long.parseLong(id));

            printWriter.println(converter.toJson(cashReceipt));
        }
    }
}
