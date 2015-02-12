package com.tienda.musica;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class PerfilCliente
 */
public class PerfilCliente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PerfilCliente() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String iden = request.getParameter("ident");
		int ident = Integer.parseInt(iden);
		System.out.println("ident" + ident);		
		request.setAttribute("ident", ident);
		
		request.getRequestDispatcher("TodosCorreos.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession sesion = request.getSession();
		String cli = (String) sesion.getAttribute("rol");
		Integer idLog = (Integer) sesion.getAttribute("ident");
		
		ResultSet rs = null;
		String obNom = null,obApe = null,obNif = null,obEma = null,obDir=null;
		int obIde=0,obTel=0,obLog=0;
		if (!cli.equals("cliente")) {
			System.out.println("no es cliente");
			sesion.invalidate();
			response.sendRedirect("index.jsp");
		}
		
		String sSQL = "SELECT ID_CLIENTE,NOMBRE,APELLIDOS,NIF,DIRECCION,TELEFONO,EMAIL,ID_LOGIN FROM CLIENTE";
		ConexOracle sentencia = new ConexOracle();
		try {
			rs = sentencia.consultaQuery(sSQL);
			while (rs.next()) {
				if (rs.getInt("ID_LOGIN")==idLog) {
					obIde = rs.getInt("ID_CLIENTE"); 
					obNom = rs.getString("NOMBRE"); 
					obApe = rs.getString("APELLIDOS"); 
					obNif = rs.getString("NIF"); 
					obDir = rs.getString("DIRECCION");
					obTel = rs.getInt("TELEFONO"); 
					obEma = rs.getString("EMAIL"); 
					obLog = rs.getInt("ID_LOGIN"); 
				}	
			}
			
		} catch (SQLException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		request.setAttribute("obsIde",obIde);
		request.setAttribute("obsNom",obNom);
		request.setAttribute("obsApe",obApe);
		request.setAttribute("obsNif",obNif);
		request.setAttribute("obsDir",obDir);
		request.setAttribute("obsTel",obTel);
		request.setAttribute("obsEma",obEma);
		request.setAttribute("obsLog",obLog);
		
		request.getRequestDispatcher("perfilCliente.jsp").forward(request, response);
	}

}
