import griffon.test.AbstractCliTestCase

/**
 * Test case for the "create-jython-class" Griffon command.
 */
class CreateJythonClassTests extends AbstractCliTestCase {
    void testDefault() {
        execute(["create-jython-class griffonjython.Test"])

        assertEquals 0, waitForProcess()
        verifyHeader()

        // Make sure that the script was found.
        assertFalse "CreateJythonClass script not found.", output.contains("Script not found:")

		def newJythonClassPath = 'src/jython/griffonjython/Test.py'
		def newJythonClassFile = new File(newJythonClassPath)
		if (!newJythonClassFile.exists()) {
			fail "Expected new Jython class ${newJythonClassPath}"
		}
		newJythonClassFile.delete()
		(new File('src/jython/griffonjython')).delete()
    }
}
