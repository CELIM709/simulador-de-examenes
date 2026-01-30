package com.sistema.logica;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.sistema.modelos.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class PersistenciaDatos {

    private static final String ARCHIVO = "resultados_examen.txt";

    public static void guardarResultado(Resultado res) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO, true))) {
            writer.println("USUARIO:" + res.getUsuario().getNombre() + "|" + res.getUsuario().getCedula());
            writer.println("TEMA:" + res.getDetalles().get(0).getPregunta().getTema());
            writer.println("NIVEL:" + res.getDetalles().get(0).getPregunta().getNivel().name());
            writer.println("PUNTAJE:" + res.getPuntaje());
            writer.println("ACIERTOS:" + res.getAciertos() + "/" + res.getTotalPreguntas());
            writer.println("FECHA:" + res.getFecha().toString());
            writer.println("--------------------------------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> leerHistorial() {
        List<String> lineas = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return lineas;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineas;
    }

    public static void exportarReporteAgrupadoPDF(File destino) throws Exception {
        // 1. Leer y agrupar datos del archivo TXT
        Map<String, List<String[]>> agrupados = cargarYAgruparDatos();

        // 2. Crear el documento PDF
        Document documento = new Document(PageSize.A4);
        PdfWriter.getInstance(documento, new FileOutputStream(destino));
        documento.open();

        // Estilos de fuente
        Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLUE);
        Font fuenteSubtitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.DARK_GRAY);
        Font fuenteTablaCabecera = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);

        // Título del PDF
        Paragraph titulo = new Paragraph("REPORTE HISTÓRICO DE EVALUACIONES", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        documento.add(titulo);

        // 3. Recorrer el Mapa agrupado para crear secciones por usuario
        for (Map.Entry<String, List<String[]>> entrada : agrupados.entrySet()) {
            String[] datosUser = entrada.getKey().split("\\|");
            String nombre = (datosUser.length > 0) ? datosUser[0] : "Anónimo";
            String cedula = (datosUser.length > 1) ? datosUser[1] : "N/A";

            // Encabezado de Usuario
            documento.add(new Paragraph("ESTUDIANTE: " + nombre.toUpperCase() + " | CÉDULA: " + cedula, fuenteSubtitulo));
            documento.add(new Paragraph(" ")); // Espacio

            PdfPTable tabla = new PdfPTable(5); // 5 columnas
            tabla.setWidthPercentage(100);
            tabla.setSpacingAfter(20);

            String[] cabeceras = {"Fecha", "Tema", "Nivel", "Aciertos", "Puntaje"};
            for (String h : cabeceras) {
                PdfPCell cell = new PdfPCell(new Phrase(h, fuenteTablaCabecera));
                cell.setBackgroundColor(BaseColor.GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(cell);
            }

            for (String[] fila : entrada.getValue()) {
                for (String dato : fila) {
                    tabla.addCell(new Phrase(dato, new Font(Font.FontFamily.HELVETICA, 9)));
                }
            }
            documento.add(tabla);
        }

        documento.close();
    }

    private static Map<String, List<String[]>> cargarYAgruparDatos() {
        Map<String, List<String[]>> agrupados = new TreeMap<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return agrupados;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            String usuario = "", tema = "", nivel = "", puntaje = "", aciertos = "", fecha = "";

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.startsWith("USUARIO:")) {
                    usuario = linea.substring(8);
                } else if (linea.startsWith("TEMA:")) {
                    tema = linea.substring(5);
                } else if (linea.startsWith("NIVEL:")) {
                    nivel = linea.substring(6);
                } else if (linea.startsWith("PUNTAJE:")) {
                    puntaje = linea.substring(8) + "%";
                } else if (linea.startsWith("ACIERTOS:")) {
                    aciertos = linea.substring(9);
                } else if (linea.startsWith("FECHA:")) {
                    fecha = linea.substring(6);
                    if (fecha.length() > 16) {
                        fecha = fecha.substring(0, 16).replace("T", " ");
                    }
                } else if (linea.startsWith("----------------")) {
                    if (!usuario.isEmpty()) {
                        String[] datosExamen = {fecha, tema, nivel, aciertos, puntaje};
                        agrupados.computeIfAbsent(usuario, k -> new ArrayList<>()).add(datosExamen);
                    }
                    // Reset para el siguiente
                    usuario = "";
                    tema = "";
                    nivel = "";
                    puntaje = "";
                    aciertos = "";
                    fecha = "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return agrupados;
    }

    public static List<Resultado> recuperarResultadosHistoricos() {
        List<Resultado> historial = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return historial;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            Usuario user = null;
            String tema = "";
            NivelDificultad nivel = null;
            double puntaje = 0;
            int aciertos = 0, total = 0;
            LocalDateTime fecha = null;

            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("USUARIO:")) {
                    String[] partes = linea.substring(8).split("\\|");
                    user = new Usuario(partes[0], partes[1]);
                } else if (linea.startsWith("TEMA:")) {
                    tema = linea.substring(5);
                } else if (linea.startsWith("NIVEL:")) {
                    nivel = NivelDificultad.valueOf(linea.substring(6).trim());
                } else if (linea.startsWith("PUNTAJE:")) {
                    puntaje = Double.parseDouble(linea.substring(8));
                } else if (linea.startsWith("ACIERTOS:")) {
                    String[] partes = linea.substring(9).split("/");
                    aciertos = Integer.parseInt(partes[0]);
                    total = Integer.parseInt(partes[1]);
                } else if (linea.startsWith("FECHA:")) {
                    fecha = LocalDateTime.parse(linea.substring(6).trim());
                } else if (linea.startsWith("----------------")) {
                    Pregunta dummy = new Pregunta("", new ArrayList<>(), 0, tema, nivel, "");
                    RegistroRespuesta reg = new RegistroRespuesta(dummy, 0);
                    Resultado r = new Resultado(user, aciertos, total, puntaje, Arrays.asList(reg));
                    r.fecha = fecha;
                    historial.add(r);
                }
            }
        } catch (Exception e) {
            System.err.println("Error procesando historial para estadísticas: " + e.getMessage());
        }
        return historial;
    }
}
