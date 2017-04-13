package mx.ipn.edu.practica30_webserviceclient;

import android.app.Activity;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class MainActivity extends Activity {
    private EditText entrada;
    private TextView salida;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        entrada = (EditText) findViewById(R.id.EditText01);
        salida = (TextView) findViewById(R.id.TextView01);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.
                Builder().permitNetwork().build());
    }

    public void buscar(View view){
        try {
            String palabras = entrada.getText().toString();
            String resultado = resultadosGoogle(palabras);
            salida.append(palabras + "--" + resultado + "\n");
        } catch (Exception e) {
            salida.append("Error al conectar\n" + e.getMessage() + e + "\n");
            Log.e("HTTP", e.getMessage(), e);
        }
    }

    String resultadosGoogle(String palabras) throws Exception {
//        URL url = new URL("http://books.google.com/books/feeds/volumes?q=\"" + URLEncoder.encode(palabras, "UTF-8") + "\"");
        URL url = new URL("http://www.webservicex.net/geoipservice.asmx/GetGeoIP?IPAddress="  + URLEncoder.encode(palabras, "UTF-8"));
        SAXParserFactory fabrica = SAXParserFactory.newInstance();
        SAXParser parser = fabrica.newSAXParser();
        XMLReader lector = parser.getXMLReader();
        ManejadorXML manejadorXML = new ManejadorXML();
        lector.setContentHandler(manejadorXML);
        lector.parse(new InputSource(url.openStream()));
        return manejadorXML.getTotalResults();
    }

    public class ManejadorXML extends DefaultHandler {

        private String totalResults;
        private String countryName;
        private StringBuilder cadena = new StringBuilder();

        public String getTotalResults() {
//            return totalResults;
            return countryName;
        }

        @Override
        public void startElement(String uri, String nombreLocal, String
                nombreCualif, Attributes atributos) throws SAXException {
            cadena.setLength(0);
        }

        @Override
        public void characters(char ch[], int comienzo, int lon){
            cadena.append(ch, comienzo, lon);
        }

        @Override
        public void endElement(String uri, String nombreLocal,
                               String nombreCualif) throws SAXException {
/*            if (nombreLocal.equals("totalResults")) {
                totalResults = cadena.toString();
        }
*/            if (nombreLocal.equals("CountryName")) {
                countryName = cadena.toString();
            }
        }
    }
}
