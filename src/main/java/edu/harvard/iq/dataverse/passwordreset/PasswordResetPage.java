package edu.harvard.iq.dataverse.passwordreset;

import edu.harvard.iq.dataverse.DataverseSession;
import edu.harvard.iq.dataverse.authorization.AuthenticationServiceBean;
import edu.harvard.iq.dataverse.authorization.providers.builtin.BuiltinAuthenticationProvider;
import edu.harvard.iq.dataverse.authorization.providers.builtin.BuiltinUser;
import edu.harvard.iq.dataverse.authorization.providers.builtin.BuiltinUserServiceBean;
import edu.harvard.iq.dataverse.authorization.users.AuthenticatedUser;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ViewScoped
@Named("PasswordResetPage")
public class PasswordResetPage implements java.io.Serializable {

    private static final Logger logger = Logger.getLogger(PasswordResetPage.class.getCanonicalName());

    @EJB
    PasswordResetServiceBean passwordResetService;
    @EJB
    BuiltinUserServiceBean dataverseUserService;
    @EJB
    AuthenticationServiceBean authSvc;
    @Inject
    DataverseSession session;

    /**
     * The unique string used to look up a user and continue the password reset
     * process.
     */
    String token;

    /**
     * The user looked up by the token who will be setting a new password.
     */
    BuiltinUser user;

    /**
     * The email address that is entered to initiate the password reset process.
     */
    String emailAddress;

    /**
     * The link that is emailed to the user to reset the password that contains
     * a token.
     */
    String passwordResetUrl;

    /**
     * The new password the user enters.
     */
    String newPassword;

    public void init() {
        if (token != null) {
            PasswordResetExecResponse passwordResetExecResponse = passwordResetService.processToken(token);
            PasswordResetData passwordResetData = passwordResetExecResponse.getPasswordResetData();
            if (passwordResetData != null) {
                user = passwordResetData.getDataverseUser();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password Reset Link", "Your password reset link is not valid."));
            }
        }
    }

    public void sendPasswordResetLink() {
        logger.info("Send link button clicked. Email address provided: " + emailAddress);
        try {
            PasswordResetInitResponse passwordResetInitResponse = passwordResetService.requestReset(emailAddress);
            PasswordResetData passwordResetData = passwordResetInitResponse.getPasswordResetData();
            if (passwordResetData != null) {
                BuiltinUser user = passwordResetData.getDataverseUser();
                passwordResetUrl = passwordResetInitResponse.getResetUrl();
                logger.info("Found single account using " + emailAddress + ": " + user.getUserName() + " and sending link " + passwordResetUrl);
            } else {
                /**
                 * @todo remove "single" when it's no longer necessary. See
                 * https://github.com/IQSS/dataverse/issues/844 and
                 * https://github.com/IQSS/dataverse/issues/1141
                 */
                logger.info("Couldn't find single account using " + emailAddress);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password Reset Initiated", ""));
        } catch (PasswordResetException ex) {
            /**
             * @todo do we really need a special exception for this??
             */
            logger.info("Error: " + ex);
        }
    }

    public String resetPassword() {
        PasswordChangeAttemptResponse response = passwordResetService.attemptPasswordReset(user, newPassword, this.token);
        if (response.isChanged()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, response.getMessageSummary(), response.getMessageDetail()));
            String builtinAuthProviderId = BuiltinAuthenticationProvider.PROVIDER_ID;
            AuthenticatedUser au = authSvc.lookupUser(builtinAuthProviderId, user.getUserName());
            session.setUser(au);
            return "/dataverse.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, response.getMessageSummary(), response.getMessageDetail()));
            return null;
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BuiltinUser getUser() {
        return user;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPasswordResetUrl() {
        return passwordResetUrl;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

}
