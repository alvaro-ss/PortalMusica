package com.acciones;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.backend.Cancion;
import com.backend.ListaModificacion;
import com.tienda.musica.ConexOracle;

/**
 * Servlet implementation class AddCancion
 */
public class AddCancion extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddCancion() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String cancion = request.getParameter("cancion");
		String[] info = cancion.split(" - ");

		if (info.length > 1) {
			String autor = info[0];
			String titulo = info[1];
			ConexOracle conexion = new ConexOracle();
			Cancion c = null;
			System.out.println("Autor : " + autor);
			System.out.println("Titulo : " + titulo);
			String id_lista = request.getParameter("id_lista");
			boolean encuentraCancion = false, esNueva = true;

			try {
				ResultSet filaCancion = conexion
						.consultaQuery("Select * from canciones where cantante='"
								+ autor + "' AND TITULO = '" + titulo + "'");

				if (filaCancion.next()) {
					c = new Cancion(filaCancion.getInt(1),
							filaCancion.getString(2), filaCancion.getString(3),
							filaCancion.getString(4), filaCancion.getString(5),
							filaCancion.getInt(6), filaCancion.getString(7));
					encuentraCancion = true;
				}
				filaCancion.close();

				if (encuentraCancion) {
					ResultSet nueva = conexion
							.consultaQuery("Select * from listas_cliente where id_cancion= "
									+ c.getId_cancion() + "");

					while (nueva.next()) {
						esNueva = false;
					}

					if (esNueva) {
						ListaModificacion.getInstancia().getLista().add(c);

						conexion.actualizarQuery("Insert into listas_cliente values(1,"
								+ id_lista
								+ ","
								+ c.getId_cancion()
								+ ",sysdate)");
					}
				}

				conexion.finalizarConexion();
				request.getRequestDispatcher("/ModificaLista.jsp").forward(
						request, response);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		request.getRequestDispatcher("/ModificaLista.jsp").forward(
				request, response);

	}

}
