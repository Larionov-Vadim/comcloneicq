import layers.datalink.DatalinkLayer;
import layers.physical.PhysicalLayer;
import layers.physical.Settings.ComPortSettings;
import layers.physical.Settings.DataBitsEnum;
import layers.physical.Settings.ParityEnum;
import layers.physical.Settings.StopBitsEnum;

/**
 * Created by Vadim on 01.05.2015.
 */
public class TestWriter {
    private static DatalinkLayer datalinkLayer = new DatalinkLayer();

    public static void main(String[] args) {
        String portName = PhysicalLayer.getAvailablePorts().get(3);
        int baudRate = ComPortSettings.getAvailableBaudRates().get(5);
        DataBitsEnum databits = ComPortSettings.getAvailableDataBits().get(3);
        StopBitsEnum stopbits = ComPortSettings.getAvailableStopBits().get(0);
        ParityEnum parity = ComPortSettings.getAvailableParity().get(0);

        ComPortSettings settings = new ComPortSettings(portName, baudRate, databits, stopbits, parity);
        datalinkLayer.connect(settings);

        String msg = "The article is entitled “Search engines for scientific and academic information”. The author of this article is Lluis Codina. This article was written in 2007.\n" +
                "The general idea is the Web is not only here to stay, but to make a positive and real impact on the spread of academic and scientific information.\n" +
                "The text can be divided into 4 parts and conclusion.\n" +
                "In the first part author says about contradiction which appears upon joining the words \"Web” and \"science”. This contradiction arises from the academic and professional sector's suspicion.\n" +
                "In the second part we learn that there is then the difficulty in finding academic or scientific results when terms with the same name (but different meaning) are also used in commercial or popular culture. For example, if somebody interested in cloning searches for information on the famous cloning experiment with the sheep Dolly, search engines like Google will probably only bring up information about the singer Dolly Parton.\n" +
                "The same goes searching for information with key words coinciding with popular Internet forum discussions.\n" +
                "In the third part we learn about classification documents. [Перечисления типов] These six types of documents overlap.\n" +
                "In the fourth part we get information about search engines such as Scirus, Live Search Academic, Google Scholar, they are compared in a table. The main principle of these three systems is that they only index websites associated to academia.";
        datalinkLayer.send(msg);
        // datalinkLayer.disconnect();
    }
}
