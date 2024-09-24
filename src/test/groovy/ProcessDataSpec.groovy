
import com.sap.gateway.ip.core.customdev.util.Message
import spock.lang.Specification
import java.nio.file.Files
import java.nio.file.Paths

class ProcessDataSpec extends Specification {

    def "test processData with DESADV_example01.edi"() {
        given:
        def message = Mock(Message)
        def exampleEdiFilePath = Paths.get("src/test/resources/DESADV_example01.edi");

        def objToTest = new ConvertEDIToXML();

        when:
        def resultMessage = objToTest.convert(new FileInputStream(exampleEdiFilePath.toFile()));

        then:
        resultMessage.getBody() != ediContent  // Assuming convertEdiToXml changes the body content
    }

    // Placeholder for processData function, ensure this part is replaced with the actual implementation
    private Message processData(Message message) {
        // Actual function logic goes here
        // For now, let's call the actual function
        return MainClass.processData(message)
    }
}