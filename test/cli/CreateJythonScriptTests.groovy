import griffon.test.AbstractCliTestCase

/**
 * Test case for the "create-jython-script" Griffon command.
 */
class CreateJythonScriptTests extends AbstractCliTestCase {
    void testDefault() {
        execute(["create-jython-script TestScript"])

        assertEquals 0, waitForProcess()
        verifyHeader()

        // Make sure that the script was found.
        assertFalse "CreateJythonScript script not found.", output.contains("Script not found:")

		def newJythonScriptPath = 'griffon-app/resources/TestScript.py'
		def newJythonScriptFile = new File(newJythonScriptPath)
		if (!newJythonScriptFile.exists()) {
			fail "Expected new Jython script ${newJythonScriptPath}"
		}
		newJythonScriptFile.delete()
    }
}
