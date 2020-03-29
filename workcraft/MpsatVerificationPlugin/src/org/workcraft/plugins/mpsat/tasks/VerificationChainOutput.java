package org.workcraft.plugins.mpsat.tasks;

import org.workcraft.plugins.mpsat.VerificationParameters;
import org.workcraft.plugins.pcomp.tasks.PcompOutput;
import org.workcraft.plugins.punf.tasks.PunfOutput;
import org.workcraft.tasks.ExportOutput;
import org.workcraft.tasks.Result;

public class VerificationChainOutput {
    private Result<? extends ExportOutput> exportResult;
    private Result<? extends PcompOutput> pcompResult;
    private Result<? extends PunfOutput> punfResult;
    private Result<? extends VerificationOutput> mpsatResult;
    private VerificationParameters verificationParameters;
    private String message;

    public VerificationChainOutput(Result<? extends ExportOutput> exportResult,
            Result<? extends PcompOutput> pcompResult,
            Result<? extends PunfOutput> punfResult,
            Result<? extends VerificationOutput> mpsatResult,
            VerificationParameters verificationParameters, String message) {

        this.exportResult = exportResult;
        this.pcompResult = pcompResult;
        this.punfResult = punfResult;
        this.mpsatResult = mpsatResult;
        this.verificationParameters = verificationParameters;
        this.message = message;
    }

    public VerificationChainOutput(Result<? extends ExportOutput> exportResult,
            Result<? extends PcompOutput> pcompResult,
            Result<? extends PunfOutput> punfResult,
            Result<? extends VerificationOutput> mpsatResult,
            VerificationParameters verificationParameters) {

        this(exportResult, pcompResult, punfResult, mpsatResult, verificationParameters, null);
    }

    public VerificationParameters getVerificationParameters() {
        return verificationParameters;
    }

    public Result<? extends ExportOutput> getExportResult() {
        return exportResult;
    }

    public Result<? extends PcompOutput> getPcompResult() {
        return pcompResult;
    }

    public Result<? extends PunfOutput> getPunfResult() {
        return punfResult;
    }

    public Result<? extends VerificationOutput> getMpsatResult() {
        return mpsatResult;
    }

    public String getMessage() {
        return message;
    }

}
