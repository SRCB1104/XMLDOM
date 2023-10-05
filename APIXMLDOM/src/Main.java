import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String file = "src\\sales.xml";
        String newFile = "src\\new_sales.xml";

        Scanner sc = new Scanner(System.in);

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(file));

            float porcentaje;
            do {
                System.out.print("Ingrese el porcentaje para aplicar el incremento (entre 5% y 15%): ");
                porcentaje = sc.nextFloat();
            } while (porcentaje < 5 || porcentaje > 15);

            sc.nextLine();

            System.out.print("Ingrese el departamento a modificar: ");
            String departamento = sc.nextLine();

            NodeList saleRecords = document.getElementsByTagName("sale_record");

            for (int i = 0; i < saleRecords.getLength(); i++) {
                Element saleRecord = (Element) saleRecords.item(i);
                String departmentValue = saleRecord.getElementsByTagName("department").item(0).getTextContent();

                if (departmentValue.equals(departamento)) {
                    Element salesElement = (Element) saleRecord.getElementsByTagName("sales").item(0);
                    double sales = Double.parseDouble(salesElement.getTextContent());

                    double nuevoValorVentas = sales * (1 + (porcentaje / 100));

                    salesElement.setTextContent(String.format("%.2f", nuevoValorVentas));
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(newFile));
            transformer.transform(source, result);

            System.out.println("Archivo creado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al procesar el archivo");
        }
    }
}
