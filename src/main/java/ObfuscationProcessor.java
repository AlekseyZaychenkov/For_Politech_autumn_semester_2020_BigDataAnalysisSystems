import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ObfuscationProcessor {
    private static String source = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 `~!@#$%^*()_-+={[}]:;\"',.?\\|<>/";
    private static String target = "ePQa- $\\?q:*x8mF0{7RrwZDK|zkoVy}[\"gjchpbEnBUNA<GI`~LTuvs/l,(3M5_H2+OY>ft.#;!S@'1C]%9iWd4X^J=)6";

    public void proceed(File inputFile, File outputFile, Mode mode) throws IOException, TransformerException, ParserConfigurationException, SAXException {
        XMLReader xmlReader = new XMLFilterImpl(SAXParserFactory.newInstance().newSAXParser().getXMLReader()) {
            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                String information = new String(ch, start, length).trim();
                if (information.length() > 0) {
                    String ob = (mode == Mode.OBFUSCATION) ? obfuscateString(information) : deobfuscateString(information);
                    ch = ob.toCharArray();
                    start = 0;
                    length = ch.length;
                }
                super.characters(ch, start, length);
            }
        };

        Source src = new SAXSource(xmlReader, new InputSource(new FileReader(inputFile)));
        Result res = new StreamResult(new FileWriter(outputFile));
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(src, res);
    }

    static String obfuscateString(String inputString) {
        char[] outputSequence = new char[inputString.length()];
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            int index = source.indexOf(c);
            outputSequence[i] = target.charAt(index);
        }
        return new String(outputSequence);
    }

    static String deobfuscateString(String inputString) {
        char[] outputSequence = new char[inputString.length()];
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            int index = target.indexOf(c);
            outputSequence[i] = source.charAt(index);
        }
        return new String(outputSequence);
    }
}
