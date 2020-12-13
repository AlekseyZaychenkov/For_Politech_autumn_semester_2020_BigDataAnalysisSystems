import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;

public class Main {
    private final static int REQUIRED_NUMBER_OF_ARGUMENTS = 3;
    private static Mode mode;
    private static ObfuscationProcessor obfuscationProcessor = new ObfuscationProcessor();

    /*Arguments order: mode (OB/DEOB), input file path, output file path */
    public static void main(String[] args) {
        if (args.length != REQUIRED_NUMBER_OF_ARGUMENTS) {
            System.out.println("Wrong number of arguments, " +
                    "there should be arguments: mode (OB/DEOB), " +
                    "input file path, output file path.");
            return;
        }

        if (args[0].equals("OB")) {
            mode = Mode.OBFUSCATION;
        } else if (args[0].equals("DEOB")) {
            mode = Mode.DEOBFUSCATION;
        } else {
            System.out.println("Wrong modes argument, " +
                    "there should be arguments: mode (OB/DEOB), " +
                    "input file path, output file path.");
            return;
        }

        File inputFile = new File(args[1]);
        File outputFile = new File(args[2]);

        if (!inputFile.exists()) {
            System.out.println("Input file \"" + args[1] + "\" not found");
        }

        try {
            obfuscationProcessor.proceed(inputFile, outputFile, mode);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException saxe) {
            saxe.printStackTrace();
        }
    }
}