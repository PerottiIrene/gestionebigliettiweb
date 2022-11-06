package it.prova.gestionebigliettiweb.web.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;

import it.prova.gestionebigliettiweb.model.Biglietto;
import it.prova.gestionebigliettiweb.service.BigliettoService;
import it.prova.gestionebigliettiweb.service.MyServiceFactory;


/**
 * Servlet implementation class ExecuteDeleteServlet
 */
@WebServlet("/ExecuteDeleteServlet")
public class ExecuteDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public ExecuteDeleteServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String parametroIdDelBigliettoCheVoglioEliminare = request.getParameter("idBigliettoDaEliminare");

		BigliettoService bigliettoServiceInstance = MyServiceFactory.getBigliettoServiceInstance();
		
		if (!NumberUtils.isCreatable(parametroIdDelBigliettoCheVoglioEliminare)) {
			// qui ci andrebbe un messaggio nei file di log costruito ad hoc se fosse attivo
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return;
		}

		try {
			Biglietto bigliettoDaEliminare = bigliettoServiceInstance
					.caricaSingoloElemento(Long.parseLong(parametroIdDelBigliettoCheVoglioEliminare));

			bigliettoServiceInstance.rimuovi(bigliettoDaEliminare.getId());
			request.setAttribute("successMessage", "Eliminazione effettuata con successo");
			request.setAttribute("listaAttribute", bigliettoServiceInstance.listAll());

			String destinazione = "/biglietto/results.jsp";

			RequestDispatcher rd = request.getRequestDispatcher(destinazione);
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		}
	}

}
