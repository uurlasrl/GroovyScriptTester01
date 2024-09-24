import com.sap.gateway.ip.core.customdev.util.Message

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream
//import org.apache.ivy.util.MessageLogger

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets;
import java.util.HashMap;



class ConvertEDIToXML {

// Static TreeMap declaration and initialization
    static TreeMap<String, Object> messageHierarchy = new TreeMap<>();
    static HashMap<String, Vector<String>> segmentStructure = new HashMap<>();

    static{
        TreeMap<String, Object> pci1Map = new TreeMap<>();
        pci1Map.put("QTY", new TreeMap<>());
        pci1Map.put("GIN", new TreeMap<>());

        TreeMap<String, Object> rffMap = new TreeMap<>();
        rffMap.put("DTM", new TreeMap<>());

        TreeMap<String, Object> linMap = new TreeMap<>();
        linMap.put("PIA", new TreeMap<>());
        linMap.put("MEA", new TreeMap<>());
        linMap.put("QTY", new TreeMap<>());
        linMap.put("GIN", new TreeMap<>());
        linMap.put("DLM", new TreeMap<>());
        linMap.put("DTM", new TreeMap<>());
        linMap.put("FTX", new TreeMap<>());
        linMap.put("RFF", rffMap);
        linMap.put("PCI", pci1Map);

        TreeMap<String, Object> pciMap = new TreeMap<>();
        pciMap.put("RFF", new TreeMap<>());
        pciMap.put("DTM", new TreeMap<>());
        pciMap.put("GIN", new TreeMap<>());

        TreeMap<String, Object> hanMap = new TreeMap<>();
        hanMap.put("FTX", new TreeMap<>());

        TreeMap<String, Object> pacMap = new TreeMap<>();
        pacMap.put("MEA", new TreeMap<>());
        pacMap.put("HAN", hanMap);
        pacMap.put("PCI", pciMap);

        TreeMap<String, Object> cpsMap = new TreeMap<>();
        cpsMap.put("FTX", new TreeMap<>());
        cpsMap.put("PAC", pacMap);
        cpsMap.put("LIN", linMap);

        TreeMap<String, Object> eqdMap = new TreeMap<>();
        eqdMap.put("MEA", new TreeMap<>());
        eqdMap.put("HAN", hanMap);

        TreeMap<String, Object> todMap = new TreeMap<>();
        eqdMap.put("FTX", new TreeMap<>());

        TreeMap<String, Object> ctaMap = new TreeMap<>();
        ctaMap.put("COM", new TreeMap<>());

        TreeMap<String, Object> nadMap = new TreeMap<>();
        nadMap.put("RFF", rffMap);
        nadMap.put("CTA", ctaMap);

        TreeMap<String, Object> bgmMap = new TreeMap<>();
        bgmMap.put("DTM", new TreeMap<>());
        bgmMap.put("MEA", new TreeMap<>());
        bgmMap.put("MOA", new TreeMap<>());
        bgmMap.put("RFF", rffMap);
        bgmMap.put("NAD", nadMap);
        bgmMap.put("TOD", todMap);
        bgmMap.put("TDT", new TreeMap<>());
        bgmMap.put("EQD", eqdMap);
        bgmMap.put("CPS", cpsMap);

        messageHierarchy.put("UNB", new TreeMap<>());
        messageHierarchy.put("UNH", new TreeMap<>());
        messageHierarchy.put("BGM", bgmMap);
        messageHierarchy.put("UNT", new TreeMap<>());
        messageHierarchy.put("UNZ", new TreeMap<>());

//        messageHierarchy.put("UNA",unaMap);

        Vector<String> unbStructure = new Vector<String>();
        unbStructure.add("S001");
        unbStructure.add("_0001");
        unbStructure.add("_0002");
        unbStructure.add("S002");
        unbStructure.add("_0004");
        unbStructure.add("_0007");
        unbStructure.add("_0008");
        unbStructure.add("S003");
        unbStructure.add("_0010");
        unbStructure.add("_0007");
        unbStructure.add("_0014");
        unbStructure.add("S004");
        unbStructure.add("_0017");
        unbStructure.add("_0019");
        unbStructure.add("0020");
        unbStructure.add("S005");
        unbStructure.add("_0022");
        unbStructure.add("_0025");
        unbStructure.add("0026");
        unbStructure.add("0029");
        unbStructure.add("0031");
        unbStructure.add("0032");
        unbStructure.add("0035");

        Vector<String> unhStructure = new Vector<String>();
        unhStructure.add("0062");
        unhStructure.add("S009");
        unhStructure.add("_0065");
        unhStructure.add("_0052");
        unhStructure.add("_0054");
        unhStructure.add("_0051");
        unhStructure.add("_0057");
        unhStructure.add("0068");
        unhStructure.add("S010");
        unhStructure.add("_0070");
        unhStructure.add("_0073");

        Vector<String> bgmStructure = new Vector<String>();
        bgmStructure.add("C002");
        bgmStructure.add("_1001");
        bgmStructure.add("_1131");
        bgmStructure.add("_3055");
        bgmStructure.add("_1000");
        bgmStructure.add("1004");
        bgmStructure.add("1225");
        bgmStructure.add("4343");

        Vector<String> dtmStructure = new Vector<String>();
        dtmStructure.add("C507");
        dtmStructure.add("_2005");
        dtmStructure.add("_2380");
        dtmStructure.add("_2379");

        Vector<String> moaStructure = new Vector<String>();
        moaStructure.add("C516");
        moaStructure.add("_5025");
        moaStructure.add("_5004");
        moaStructure.add("_6345");
        moaStructure.add("_6343");
        moaStructure.add("_4405");

        Vector<String> rffStructure = new Vector<String>();
        rffStructure.add("C506");
        rffStructure.add("_1153");
        rffStructure.add("_1154");
        rffStructure.add("_1156");
        rffStructure.add("_4000");

        Vector<String> nadStructure = new Vector<String>();
        nadStructure.add("3035");
        nadStructure.add("C082");
        nadStructure.add("_3039");
        nadStructure.add("_1131");
        nadStructure.add("_3055");
        nadStructure.add("C058");
        nadStructure.add("_3124");
        nadStructure.add("_3124");
        nadStructure.add("_3124");
        nadStructure.add("_3124");
        nadStructure.add("_3124");
        nadStructure.add("C080");
        nadStructure.add("_3036");
        nadStructure.add("_3036");
        nadStructure.add("_3036");
        nadStructure.add("_3036");
        nadStructure.add("_3036");
        nadStructure.add("_3045");
        nadStructure.add("C059");
        nadStructure.add("_3042");
        nadStructure.add("_3042");
        nadStructure.add("_3042");
        nadStructure.add("_3042");
        nadStructure.add("3164");
        nadStructure.add("3229");
        nadStructure.add("3251");
        nadStructure.add("3207");

        Vector<String> cpsStructure = new Vector<String>();
        cpsStructure.add("7164");
        cpsStructure.add("7166");
        cpsStructure.add("7075");

        Vector<String> ctaStructure = new Vector<String>();
        ctaStructure.add("3139");
        ctaStructure.add("C056");
        ctaStructure.add("_3413");
        ctaStructure.add("_3412");

        Vector<String> comStructure = new Vector<String>();
        comStructure.add("C076");
        comStructure.add("_3148");
        comStructure.add("_3155");

        Vector<String> todStructure = new Vector<String>();
        todStructure.add("4055");
        todStructure.add("4215");
        todStructure.add("C100");
        todStructure.add("_4053");
        todStructure.add("_1131");
        todStructure.add("_3055");
        todStructure.add("_4052");
        todStructure.add("_4052");

        Vector<String> tdtStructure = new Vector<String>();
        tdtStructure.add("8051");
        tdtStructure.add("8028");
        tdtStructure.add("C220");
        tdtStructure.add("_8067");
        tdtStructure.add("_8066");
        tdtStructure.add("C228");
        tdtStructure.add("_8179");
        tdtStructure.add("_8178");
        tdtStructure.add("C040");
        tdtStructure.add("_3127");
        tdtStructure.add("_1131");
        tdtStructure.add("_3055");
        tdtStructure.add("_3128");
        tdtStructure.add("8101");
        tdtStructure.add("C401");
        tdtStructure.add("_8457");
        tdtStructure.add("_8459");
        tdtStructure.add("_7130");
        tdtStructure.add("C222");
        tdtStructure.add("_8213");
        tdtStructure.add("_1131");
        tdtStructure.add("_3055");
        tdtStructure.add("_8212");
        tdtStructure.add("_8453");
        tdtStructure.add("8281");

        Vector<String> eqdStructure = new Vector<String>();
        eqdStructure.add("8053");
        eqdStructure.add("C237");
        eqdStructure.add("_8260");
        eqdStructure.add("_1131");
        eqdStructure.add("_3055");
        eqdStructure.add("_3207");
        eqdStructure.add("C224");
        eqdStructure.add("_8155");
        eqdStructure.add("_1131");
        eqdStructure.add("_3055");
        eqdStructure.add("_8154");
        eqdStructure.add("8077");
        eqdStructure.add("8249");
        eqdStructure.add("8169");

        Vector<String> meaStructure = new Vector<String>();
        meaStructure.add("6311");
        meaStructure.add("C502");
        meaStructure.add("_6313");
        meaStructure.add("_6321");
        meaStructure.add("_6155");
        meaStructure.add("_6154");
        meaStructure.add("C174");
        meaStructure.add("_6411");
        meaStructure.add("_6314");
        meaStructure.add("_6162");
        meaStructure.add("_6152");
        meaStructure.add("_6432");
        meaStructure.add("7383");

        Vector<String> hanStructure = new Vector<String>();
        hanStructure.add("C524");
        hanStructure.add("_4079");
        hanStructure.add("_1131");
        hanStructure.add("_3055");
        hanStructure.add("_4078");
        hanStructure.add("C218");
        hanStructure.add("_7419");
        hanStructure.add("_1131");
        hanStructure.add("_3055");

        Vector<String> pciStructure = new Vector<String>();
        pciStructure.add("4233");
        pciStructure.add("C210");
        pciStructure.add("_7102");
        pciStructure.add("_7102");
        pciStructure.add("_7102");
        pciStructure.add("_7102");
        pciStructure.add("_7102");
        pciStructure.add("_7102");
        pciStructure.add("_7102");
        pciStructure.add("_7102");
        pciStructure.add("_7102");
        pciStructure.add("_7102");
        pciStructure.add("8275");
        pciStructure.add("C827");
        pciStructure.add("_7511");
        pciStructure.add("_1131");
        pciStructure.add("_3055");

        Vector<String> ginStructure = new Vector<String>();
        ginStructure.add("7405");
        ginStructure.add("C208");
        ginStructure.add("_7402");
        ginStructure.add("_7402");
        ginStructure.add("C208");
        ginStructure.add("_7402");
        ginStructure.add("_7402");
        ginStructure.add("C208");
        ginStructure.add("_7402");
        ginStructure.add("_7402");
        ginStructure.add("C208");
        ginStructure.add("_7402");
        ginStructure.add("_7402");
        ginStructure.add("C208");
        ginStructure.add("_7402");
        ginStructure.add("_7402");

        Vector<String> linStructure = new Vector<String>();
        linStructure.add("1082");
        linStructure.add("1229");
        linStructure.add("C212");
        linStructure.add("_7140");
        linStructure.add("_7143");
        linStructure.add("_1131");
        linStructure.add("_3055");
        linStructure.add("C829");
        linStructure.add("_5495");
        linStructure.add("_1082");
        linStructure.add("1222");
        linStructure.add("7083");

        Vector<String> piaStructure = new Vector<String>();
        piaStructure.add("4347");
        piaStructure.add("C212");
        piaStructure.add("_7140");
        piaStructure.add("_7143");
        piaStructure.add("_1131");
        piaStructure.add("_3055");
        piaStructure.add("C212");
        piaStructure.add("_7140");
        piaStructure.add("_7143");
        piaStructure.add("_1131");
        piaStructure.add("_3055");
        piaStructure.add("C212");
        piaStructure.add("_7140");
        piaStructure.add("_7143");
        piaStructure.add("_1131");
        piaStructure.add("_3055");
        piaStructure.add("C212");
        piaStructure.add("_7140");
        piaStructure.add("_7143");
        piaStructure.add("_1131");
        piaStructure.add("_3055");
        piaStructure.add("C212");
        piaStructure.add("_7140");
        piaStructure.add("_7143");
        piaStructure.add("_1131");
        piaStructure.add("_3055");

        Vector<String> qtyStructure = new Vector<String>();
        qtyStructure.add("C186");
        qtyStructure.add("_6063");
        qtyStructure.add("_6060");
        qtyStructure.add("_6411");

        Vector<String> dlmStructure = new Vector<String>();
        dlmStructure.add("4455");
        dlmStructure.add("C522");
        dlmStructure.add("_4403");
        dlmStructure.add("_4401");
        dlmStructure.add("_1131");
        dlmStructure.add("_3055");
        dlmStructure.add("_4400");
        dlmStructure.add("C214");
        dlmStructure.add("_7161");
        dlmStructure.add("_1131");
        dlmStructure.add("_3055");
        dlmStructure.add("_7160");
        dlmStructure.add("_7160");
        dlmStructure.add("4457");

        Vector<String> ftxStructure = new Vector<String>();
        ftxStructure.add("4451");
        ftxStructure.add("4453");
        ftxStructure.add("C107");
        ftxStructure.add("_4441");
        ftxStructure.add("_1131");
        ftxStructure.add("_3055");
        ftxStructure.add("C108");
        ftxStructure.add("_4440");
        ftxStructure.add("_4440");
        ftxStructure.add("_4440");
        ftxStructure.add("_4440");
        ftxStructure.add("_4440");
        ftxStructure.add("3453");

        Vector<String> pacStructure = new Vector<String>();
        pacStructure.add("7224");
        pacStructure.add("C531");
        pacStructure.add("_7075");
        pacStructure.add("_7233");
        pacStructure.add("_7073");
        pacStructure.add("C202");
        pacStructure.add("_7065");
        pacStructure.add("_1131");
        pacStructure.add("_3055");
        pacStructure.add("_7064");
        pacStructure.add("C402");
        pacStructure.add("_7077");
        pacStructure.add("_7064");
        pacStructure.add("_7143");
        pacStructure.add("_7064");
        pacStructure.add("_7143");
        pacStructure.add("C532");
        pacStructure.add("_8395");
        pacStructure.add("_8393");

        Vector<String> untStructure = new Vector<String>();
        untStructure.add("0074");
        untStructure.add("0062");

        Vector<String> unzStructure = new Vector<String>();
        unzStructure.add("0036");
        unzStructure.add("0020");

        segmentStructure.put("UNB", unbStructure);
        segmentStructure.put("UNH", unhStructure);
        segmentStructure.put("BGM", bgmStructure);
        segmentStructure.put("DTM", dtmStructure);
        segmentStructure.put("MOA", moaStructure);
        segmentStructure.put("RFF", rffStructure);
        segmentStructure.put("NAD", nadStructure);
        segmentStructure.put("CTA", ctaStructure);
        segmentStructure.put("CPS", cpsStructure);
        segmentStructure.put("COM", comStructure);
        segmentStructure.put("TOD", todStructure);
        segmentStructure.put("TDT", tdtStructure);
        segmentStructure.put("EQD", eqdStructure);
        segmentStructure.put("PAC", pacStructure);
        segmentStructure.put("MEA", meaStructure);
        segmentStructure.put("HAN", hanStructure);
        segmentStructure.put("PCI", pciStructure);
        segmentStructure.put("GIN", ginStructure);
        segmentStructure.put("LIN", linStructure);
        segmentStructure.put("PIA", piaStructure);
        segmentStructure.put("QTY", qtyStructure);
        segmentStructure.put("DLM", dlmStructure);
        segmentStructure.put("FTX", ftxStructure);
        segmentStructure.put("UNT", untStructure);
        segmentStructure.put("UNZ", unzStructure);
    }


    def String convert(InputStream is_in) {

        //audit.addLog(AuditLogStatus.WARNING, "executeModule() method called");

        //String originalFileName = dynCfg.get(nsFileNameContext, vFileNameContext);


        // UNA:+.? '
        //Payload inputPayload = msg.getMainPayload();
        InputStream is = is_in;

        // convert the is in a buffered is and read the first row
        // then we reset the buffer and we restart the reading
        BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
        bufferedInputStream.mark(13);


        String segment;
        StringBuilder outputString = new StringBuilder();
        byte[] firstRow = new byte[13];
        byte[] rowDelimiterInByte;
        char fieldSeparator;
        char subFieldSeparator;
        int bytesRead = bufferedInputStream.read(firstRow, 0, 13);
        if (bytesRead < 13) {
            throw new Exception("Could not read 13 characters from the input edi file");
        }
        bufferedInputStream.reset();
        is = bufferedInputStream;


        // first line should be UNA:+.? ' followed by U
        if (firstRow.length < 13 || firstRow[0] == 'U' || firstRow[1] == 'N' || firstRow[2] == 'A') {
            if (firstRow[9] == 'U') {
                rowDelimiterInByte = new byte[1];
                rowDelimiterInByte[0] = firstRow[8];
            } else if (firstRow[10] == 'U') {
                rowDelimiterInByte = new byte[2];
                rowDelimiterInByte[0] = firstRow[8];
                rowDelimiterInByte[1] = firstRow[9];
            } else if (firstRow[11] == 'U') {
                rowDelimiterInByte = new byte[3];
                rowDelimiterInByte[0] = firstRow[8];
                rowDelimiterInByte[1] = firstRow[9];
                rowDelimiterInByte[2] = firstRow[10];
            } else {
                throw new Exception("error UNA segment of the file is not valid");
            }
            fieldSeparator = (char) firstRow[4];
            subFieldSeparator = (char) firstRow[3];
        } else {
            throw new Exception("error UNA segment of the file is not valid");
        }

        //get the UMA segment we do not need it animore
        getNextSegment(is, rowDelimiterInByte);

        //header of the xml
        outputString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><ns1:MTFileMultiEdi xmlns:ns1=\"urn:aptar.com:3pl\">");
        TreeMap<String, Object> activeEdiContext = messageHierarchy;
        Stack<TreeMap<String, Object>> ediContextStack = new Stack<>();
        Stack<String> ediTagStack = new Stack<>();
        while ((segment = getNextSegment(is, rowDelimiterInByte)) != null) {
            String segmantName = segment.substring(0, 3);

            while (!activeEdiContext.containsKey(segmantName)) {
                // if the tag does not exist at this level
                // go back to the parent tag
                if (ediContextStack.empty()) {
                    throw ModuleException("error in segment " + segment);
                }
                String oldParent = ediTagStack.pop();
                outputString.append("</").append("F" + oldParent).append(">");
                activeEdiContext = ediContextStack.pop();
            }
            //the next segment is a subsegment of the current one
            outputString.append("<").append("F" + segmantName).append(">");
            ediTagStack.push(segmantName);
            ediContextStack.push(activeEdiContext);
            generateSegmentTags(outputString, segment, fieldSeparator, subFieldSeparator);
            activeEdiContext = (TreeMap<String, Object>) activeEdiContext.get(segmantName);
        }
        while (!ediTagStack.empty()) {
            String oldParent = ediTagStack.pop();
            outputString.append("</").append("F" + oldParent).append(">");
        }

        // end of xml
        outputString.append("</ns1:MTFileMultiEdi>");

        return outputString.toString();
    }

/**
 * get the inputStream and reaturn the next string befor the separator
 * or null if nothing was there
 *
 * @param inputStream
 * @param rowDelimiterInByte
 * @return null if nothing is in the inputStream or the string without the separator
 * @throws IOException
 */
    public String getNextSegment(InputStream inputStream, byte[] rowDelimiterInByte) {
        List<String> lines = new ArrayList<>();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nextByte;
        int delimiterIndex = 0;

        while ((nextByte = inputStream.read()) != -1) {

            if (nextByte == rowDelimiterInByte[delimiterIndex]) {
                delimiterIndex++;
                if (delimiterIndex == rowDelimiterInByte.length) {
                    return buffer.toString("UTF-8");
                }
            } else {
                if (delimiterIndex > 0) {
                    for (int i = 0; i < delimiterIndex; i++) {
                        buffer.write(rowDelimiterInByte[i]);
                    }
                }
                buffer.write(nextByte);
                delimiterIndex = 0; // Reset rowDelimiterInByte matching if any character does not match
            }
        }

        // Add the last line if there's any remaining content in the buffer
        if (buffer.size() > 0) {
            if (delimiterIndex > 0) {
                for (int i = 0; i < delimiterIndex; i++) {
                    buffer.write(rowDelimiterInByte[i]);
                }
            }
            return buffer.toString("UTF-8");
        }
        return null;
    }

    private void generateSegmentTags(StringBuilder outputString, String segment, char fieldSeparator, char subFieldSeparator) {
        String[] listOfFieldValues = segment.split("\\" + String.valueOf(fieldSeparator));
        if (!segmentStructure.containsKey(listOfFieldValues[0])) {
            throw new Exception("error in the segment " + segment);
        }
        Vector<String> currentSegmentStructure = segmentStructure.get(listOfFieldValues[0]);
        int numOfField = listOfFieldValues.length;
        int maxNumOfField = currentSegmentStructure.size();
        int i = 1;
        int j = 0;
        while (i < numOfField) {

            if (j >= maxNumOfField) {
                throw new Exception("error in the segment " + segment);
            }
            String currentFiledName = currentSegmentStructure.get(j);
            if (currentFiledName.charAt(0) != '_') {
                outputString.append("<").append("F" + currentFiledName).append(">");
                // return the number of subfield
                if ((j + 1) < maxNumOfField && currentSegmentStructure.get(j + 1).charAt(0) == '_') {
                    String[] subValues = listOfFieldValues[i].split("\\" + String.valueOf(subFieldSeparator));
                    for (int s = 0; s < subValues.length; s++) {
                        outputString.append("<").append("F" + currentSegmentStructure.get(j + s + 1).substring(1)).append(">");
                        outputString.append(subValues[s]);
                        outputString.append("</").append("F" + currentSegmentStructure.get(j + s + 1).substring(1)).append(">");
                    }
                    j++;
                    while (j < maxNumOfField && currentSegmentStructure.get(j).charAt(0) == '_') {
                        j++;
                    }
                } else {
                    outputString.append(listOfFieldValues[i]);
                    j++;
                }
                outputString.append("</").append("F" + currentFiledName).append(">");
            } else {
                j++; continue;
            }
            i++;
        }
    }

}



def Message processData(Message message) {

    // Assemble the hierarchical structure


    //get Body
    def body = message.getBody() as String;

    InputStream is = new ByteInputStream(body.getBytes());

    def pp = messageLogFactory
    try {


        ConvertEDIToXML converttObj = new ConvertEDIToXML();

        def result = converttObj.convert(is);

        message.setBody(result);


    } catch (Exception e) {
        println(e, e.getMessage());
    }

/*    message.setBody(body + "Body is modified");
    //Headers
    def map = message.getHeaders();
    def value = map.get("oldHeader");
    message.setHeader("oldHeader", value + "modified");
    message.setHeader("newHeader", "newHeader");

    //Properties
    map = message.getProperties();
    value = map.get("oldProperty");
    message.setProperty("oldProperty", value + "modified");
    message.setProperty("newProperty", "newProperty");*/


    return message;
}


