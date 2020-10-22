package com.app.erladmin.injection.module;



import com.app.erladmin.injection.scope.UserScope;
import com.app.erladmin.model.state.UserAuthenticationServiceInterface;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 */
@Module
public class UserAuthenticationModule {
    @UserScope
    @Provides
    public UserAuthenticationServiceInterface provideUserAuthenticationService(Retrofit retrofit){
        return retrofit.create(UserAuthenticationServiceInterface.class);
    }

}
