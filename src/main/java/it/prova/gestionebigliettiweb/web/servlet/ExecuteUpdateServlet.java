package it.prova.gestionebigliettiweb.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;

import it.prova.gestionebigliettiweb.model.Biglietto;
import it.prova.gestionebigliettiweb.service.MyServiceFactory;
import it.prova.gestionebigliettiweb.utility.UtilityBigliettoForm;

/**
 * Servlet implementation class ExecuteUpdateServlet
 */
@WebServlet("/admin/ExecuteUpdateServlet")
public class ExecuteUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ExecuteUpdateServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// estraggo input
		String idInput=request.getParameter("id");
		String provenienzaInputParam = request.getParameter("provenienza");
		String destinazioneInputParam = request.getParameter("destinazione");
		String prezzoInputStringParam = request.getParameter("prezzo");
		String dataStringParam = request.getParameter("data");

		// preparo un bean (che mi serve sia per tornare in pagina
		// che per modificare) e faccio il binding dei parametri
		Biglietto bigliettoInstance = UtilityBigliettoForm.createBigliettoFromParams(provenienzaInputParam,
				destinazioneInputParam, prezzoInputStringParam, dataStringParam);
		
		if (!NumberUtils.isCreatable(idInput)) {
			// qui ci andrebbe un messaggio nei file di log costruito ad hoc se fosse attivo
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return;
		}
		
		bigliettoInstance.setId(Long.parseLong(idInput));

		// se la validazione non risulta ok
		if (!UtilityBigliettoForm.validateBigliettoBean(bigliettoInstance)) {
			request.setAttribute("insert_biglietto_attr", bigliettoInstance);
			request.setAttribute("errorMessage", "Attenzione sono presenti errori di validazione");
			request.getRequestDispatcher("/biglietto/update.jsp").forward(request, response);
			return;
		}

		// se sono qui i valori sono ok quindi posso creare l'oggetto da modificare
		// occupiamoci delle operazioni di business
		try {
			MyServiceFactory.getBigliettoServiceInstance().aggiorna(bigliettoInstance);
			request.setAttribute("listaAttribute", MyServiceFactory.getBigliettoServiceInstance().listAll());
			request.setAttribute("successMessage", "Operazione effettuata con successo");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return;
		}

		// andiamo ai risultati
		request.getRequestDispatcher("/biglietto/results.jsp").forward(request, response);

	}

}
