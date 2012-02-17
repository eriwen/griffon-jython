import griffon.test.AbstractCliTestCase

/**
 * Test case for the "jython-repl" Griffon command.
 */
class JythonReplTests extends AbstractCliTestCase {
    void testDefault() {
        execute(["jython-repl"])

        assertEquals 0, waitForProcess()
        verifyHeader()

        // Make sure that the script was found.
        assertFalse "JythonRepl script not found.", output.contains("Script not found:")
    }
}
