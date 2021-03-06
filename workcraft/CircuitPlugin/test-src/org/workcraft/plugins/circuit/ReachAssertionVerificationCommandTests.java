package org.workcraft.plugins.circuit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.workcraft.Framework;
import org.workcraft.exceptions.DeserialisationException;
import org.workcraft.plugins.circuit.commands.ReachAssertionVerificationCommand;
import org.workcraft.plugins.mpsat_verification.MpsatVerificationSettings;
import org.workcraft.plugins.pcomp.PcompSettings;
import org.workcraft.plugins.punf.PunfSettings;
import org.workcraft.utils.BackendUtils;
import org.workcraft.utils.PackageUtils;
import org.workcraft.workspace.WorkspaceEntry;

import java.net.URL;

public class ReachAssertionVerificationCommandTests {

    @BeforeAll
    public static void init() {
        final Framework framework = Framework.getInstance();
        framework.init();
        PcompSettings.setCommand(BackendUtils.getTemplateToolPath("UnfoldingTools", "pcomp"));
        PunfSettings.setCommand(BackendUtils.getTemplateToolPath("UnfoldingTools", "punf"));
        MpsatVerificationSettings.setCommand(BackendUtils.getTemplateToolPath("UnfoldingTools", "mpsat"));
    }

    @Test
    public void testVmeVerification() throws DeserialisationException {
        String workName = PackageUtils.getPackagePath(getClass(), "vme-tm.circuit.work");

        final Framework framework = Framework.getInstance();
        final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL url = classLoader.getResource(workName);
        WorkspaceEntry we = framework.loadWork(url.getFile());

        ReachAssertionVerificationCommand command = new ReachAssertionVerificationCommand();
        Assertions.assertNull(command.execute(we, command.deserialiseData("incorrect - expression")));
        Assertions.assertFalse(command.execute(we, command.deserialiseData("$S\"dsr\" ^ $S\"dsw\"")));
        Assertions.assertTrue(command.execute(we, command.deserialiseData("$S\"dsr\" & $S\"dsw\"")));

        // Should be True because of the inversePredicate=false
        Assertions.assertTrue(command.execute(we, command.deserialiseData(
                "<settings inversePredicate=\"false\"><reach>$S\"dsr\" ^ $S\"dsw\"</reach></settings>")));

        // Should be False because of the inversePredicate=false
        Assertions.assertFalse(command.execute(we, command.deserialiseData(
                "<settings inversePredicate=\"false\"><reach>$S\"dsr\" &amp; $S\"dsw\"</reach></settings>")));
    }

}
