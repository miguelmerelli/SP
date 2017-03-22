package shopTesting;

import com.dogma.busClass.ApiaAbstractClass;
import com.dogma.busClass.BusClassException;
import com.dogma.busClass.object.Attribute;
import com.dogma.busClass.object.PossibleValue;
import com.dogma.busClass.object.Process;
import com.dogma.busClass.object.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

public class confirmarAprobacion extends ApiaAbstractClass {
	@Override
	protected void executeClass() throws BusClassException {

		String estado = this.getCurrentEntity().getAttribute("SH_ESTADO_APROBACION_COMPRA_STR").getValueAsString();
		String comentario = this.getCurrentEntity().getAttribute("SH_COMENTARIO_APROBACION_COMPRA_STR")
				.getValueAsString();

		if (estado.equals("2")) {
			// VUELVE A PRESUPUESTO
			this.getCurrentEntity().getAttribute("SH_COMENTARIO_VUELTAPRESUPUESTO_COMPRA_STR").setValue(comentario);
			this.getCurrentEntity().getAttribute("SH_FINPRESUPUESTO_PRESUPUESTO_COMPRA_STR").setValue("false");

		} else {
			if (estado.equals("3")) {
				// DORMIDO

				String fechaEstimada = this.getCurrentEntity().getAttribute("SH_FECHA_FIN_PENDIENTE_FEC ")
						.getValueAsString();

				int diaEstimada = Integer.parseInt(fechaEstimada.substring(0, 2));
				int mesEstimada = Integer.parseInt(fechaEstimada.substring(3, 5));
				int aniEstimada = Integer.parseInt(fechaEstimada.substring(6, 10));

				DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
				Date fechaEst = null;
				Date fechaAct = null;

				Date fechaActual = new Date();
				fechaActual.setHours(0);
				fechaActual.setMinutes(0);
				fechaActual.setSeconds(0);

				try {
					fechaEst = formatoFecha.parse(fechaEstimada);
					fechaAct = formatoFecha.parse(fechaActual.toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}

				if (fechaEst.before(fechaActual)) {
					throw new BusClassException("Fecha no puede ser anterior a la actual");
				}

			} else {
				if (estado.equals("4")) {
					// CANCELADO
					System.out.println("Se cancela");
				} else {
					// AVANZA
					Collection favs = this.getCurrentEntity().getAttribute("SH_FAVORITODIREC_PRESUPUESTO_INFO_STR")
							.getValues();
					ArrayList favoritos = new ArrayList();
					favoritos.addAll(favs);
					int cont = 0;

					for (int i = 0; i < favoritos.size(); i++) {

						if (favoritos.get(i).toString().compareTo("true") == 0) {
							cont = cont + 1;

						}
					}

					if (cont != 1) {
						throw new BusClassException("Debe seleccionar solamente 1 favorito");
					}
				}
			}
		}
	}
}