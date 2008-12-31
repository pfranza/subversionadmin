package com.gorthaur.svnadmin.client.rpcinterface;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface AuthenticationInterfaceAsync {
    void authenticate(String username, String password, AsyncCallback<Boolean> result);
    void isAdmin(String username, AsyncCallback<Boolean> result);
    void authenticateBasic(String username, String password, AsyncCallback<Boolean> result);
    void changePasswordBasic(String username, String oldPassword, String newPassword, AsyncCallback<String> result);
}
